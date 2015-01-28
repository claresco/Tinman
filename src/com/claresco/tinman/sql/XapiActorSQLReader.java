/**
 * ClarescoExperienceAPI
 * Copyright 
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * Please contact Claresco, www.claresco.com, if you have any questions.
 **/

package com.claresco.tinman.sql;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.claresco.tinman.lrs.XapiAccount;
import com.claresco.tinman.lrs.XapiActor;
import com.claresco.tinman.lrs.XapiAgent;
import com.claresco.tinman.lrs.XapiGroup;
import com.claresco.tinman.lrs.XapiInverseFunctionalIdentifier;
import com.hp.hpl.jena.sdb.core.sqlnode.SqlUnion;

/**
 * XapiActorSQLReader.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Feb 27, 2014
 * 
 */

class XapiActorSQLReader extends SQLReader {
	
	private String myTableName = "actor";
	private String myMemberTableName = "groupactors";
	
	private PreparedStatement myRetrievalByIDStatement;
	private PreparedStatement myMemberRetrievalStatement;
	private PreparedStatement myRetrievalByAccountStatement;
	private PreparedStatement myRetrievalByMboxStatement;
	private PreparedStatement myRetrievalBySha1sumStatement;
	private PreparedStatement myRetrievalByOpenIDStatement;
	private PreparedStatement myRetrievalOfGroupByAgentStatement;
	
	private XapiAccountSQLReader myAccountReader;
	
	private String myIDField = "actorid";
	
	/**
	 * 
	 * Description:
	 *	Constructor
	 *
	 * Params:
	 *
	 */
	public XapiActorSQLReader(Connection conn, XapiAccountSQLReader theAccountReader) throws SQLException{
		this.myConn = conn;
		this.myRetrievalByIDStatement = SQLUtility.createRetrievalStatement(super.myConn, this.myTableName, "actorid");
		this.myMemberRetrievalStatement = SQLUtility.createRetrievalStatement(super.myConn, this.myMemberTableName, "groupid");
		this.myRetrievalByAccountStatement = SQLUtility.createRetrievalStatement(super.myConn, myTableName, new String[]{"accountid"});
		this.myRetrievalByMboxStatement = SQLUtility.createRetrievalStatement(myConn, myTableName, "actmbox");
		this.myRetrievalBySha1sumStatement = SQLUtility.createRetrievalStatement(myConn, myTableName, "atcmbox_sha1");
		this.myRetrievalByOpenIDStatement = SQLUtility.createRetrievalStatement(myConn, myTableName, "openid");
		this.myRetrievalOfGroupByAgentStatement = SQLUtility.createRetrievalStatement(myConn, "groupactors", "agentid");
		this.myAccountReader = theAccountReader;
	}
	
	/**
	 * 
	 * Description:
	 *	Retrieve XapiActor based on its ID
	 *
	 * Params:
	 *
	 */
	protected XapiActor retrieveByID(int theID) throws SQLException{
		this.myRetrievalByIDStatement.setInt(1, theID);
		
		myResult = this.myRetrievalByIDStatement.executeQuery();
				
		// How to make sure there is no duplicate
		
		handleNoData();
		
		int theGroupID;
		String theType;
		String theName;
		String theMBox;
		String theMBoxSHA1;
		String theOpenID;
		int theAccountID;
		XapiAccount theAccount = null;
		
		// Open result set
		myResult.next();
		
		theGroupID = myResult.getInt(myIDField);
		theType = myResult.getString("actortypecode");
		theName = myResult.getString("actname");
		theMBox = myResult.getString("actmbox");
		theMBoxSHA1 = myResult.getString("atcmbox_sha1");
		theOpenID = myResult.getString("openid");
		theAccountID = myResult.getInt("accountid");		
		ArrayList<XapiAgent> theMember = new ArrayList<XapiAgent>();
		
		// Retrieve account if account exists, the accountID is -1 if its value in the database is null
		if(theAccountID > 0){
			theAccount = myAccountReader.retrieveByID(theAccountID);
		}
		
		XapiInverseFunctionalIdentifier theIdentifier = new XapiInverseFunctionalIdentifier(theMBox, theMBoxSHA1, theOpenID, theAccount);
		
		// If actor is an agent, then this function is complete
		if(theType.equals("AGT")){
			return new XapiAgent(theName, theIdentifier);
		}
		
		// Unless, it is a group, then we need to retrieve the member
		this.myMemberRetrievalStatement.setInt(1, theGroupID);
		ResultSet theRSet = this.myMemberRetrievalStatement.executeQuery();
		
		while(theRSet.next()){
			// Recursion, for every member, we will return an XapiActor by calling this function
			theMember.add((XapiAgent) this.retrieveByID(theRSet.getInt("agentid")));
		}
		
		theRSet.close();
		
		return new XapiGroup(theName, theMember, theIdentifier);
	}
	
	
	
	protected int retrieveActorID(XapiActor theActor) throws SQLException{
		XapiInverseFunctionalIdentifier theActorID = theActor.getInverseFuncId();
		
		int theDatabaseID = -1;
		
		// Check if the actor already exists in the database
		switch(theActorID.getIdentifierIndex()){
			case 1: theDatabaseID = retrieveByMbox(theActorID.getMbox().toString());
					break;
			case 2: theDatabaseID = retrieveBySha1Sum(theActorID.getMboxSha1Sum());
					break;
			case 3: theDatabaseID = retrieveByOpenID(theActorID.getOpenId().toString());
					break;
			case 4: theDatabaseID = retrieveIDByAccount(theActorID.getAccount());
					break;
		}
		
		return theDatabaseID;
	}
	
	/**
	 * 
	 * Description:
	 *	Retrieve the ID of the actor based on its account
	 *
	 * Params:
	 *	theHomepage :
	 *	theName :	
	 *	
	 */
	protected int retrieveIDByAccount(String theHomepage, String theName) throws SQLException{
		this.myRetrievalByAccountStatement.setInt(1, this.myAccountReader.retrieveIDByValue(theHomepage, theName));
		
		myResult = this.myRetrievalByAccountStatement.executeQuery();
		
		if(!myResult.isBeforeFirst()){
			return -1;
		}
		
		myResult.next();
		return myResult.getInt(myIDField);
	}
	
	
	
	protected int retrieveIDByAccount(XapiAccount theAccount) throws SQLException{
		if(theAccount == null){
			return -1;
		}
		return retrieveIDByAccount(theAccount.getHomePage().toString(), theAccount.getName());
	}
	
	
	
	protected boolean doesActorExists(int theID) throws SQLException{
		myResult = SQLUtility.executeRetrievalByIDQuery(myRetrievalByIDStatement, theID);
		
		if(SQLUtility.isResultEmpty(myResult)){
			return false;
		}
		return true;
	}
	
	
	
	protected int retrieveByInverseFuncID(XapiInverseFunctionalIdentifier theInvFuncID) throws SQLException{
		int theDatabaseID = -1;
		
		// Check if the actor already exists in the database
		switch(theInvFuncID.getIdentifierIndex()){
			case 1: theDatabaseID = this.retrieveByMbox(theInvFuncID.getMbox().toString());
					break;
			case 2: theDatabaseID = this.retrieveBySha1Sum(theInvFuncID.getMboxSha1Sum());
					break;
			case 3: theDatabaseID = this.retrieveByOpenID(theInvFuncID.getOpenId().toString());
					break;
			case 4: theDatabaseID = this.retrieveIDByAccount(theInvFuncID.getAccount());
					break;
		}
		
		return theDatabaseID;
	}
	
	
	
	protected int retrieveByMbox(String theMbox) throws SQLException{
		myRetrievalByMboxStatement.setString(1, theMbox);
		myResult = myRetrievalByMboxStatement.executeQuery();
		
		if(SQLUtility.isResultEmpty(myResult)){
			return -1;
		}
		
		myResult.next();
		return myResult.getInt(myIDField);
	}
	
	
	
	protected int retrieveBySha1Sum(String theSha1Sum) throws SQLException{
		myRetrievalBySha1sumStatement.setString(1, theSha1Sum);
		myResult = myRetrievalBySha1sumStatement.executeQuery();
		
		if(SQLUtility.isResultEmpty(myResult)){
			return -1;
		}
		
		myResult.next();
		return myResult.getInt(myIDField);
	}
	
	
	
	protected int retrieveByOpenID(String theOpenID) throws SQLException{
		myRetrievalByOpenIDStatement.setString(1, theOpenID);
		myResult = myRetrievalByOpenIDStatement.executeQuery();
		
		if(SQLUtility.isResultEmpty(myResult)){
			return -1;
		}
		
		myResult.next();
		return myResult.getInt(myIDField);
	}
	
	
	
	protected ArrayList<Integer> retrieveGroupsOfAgent(int theAgentID) throws SQLException{
		ArrayList<Integer> groupIDArray = new ArrayList<Integer>();
		
		myRetrievalOfGroupByAgentStatement.setInt(1, theAgentID);
		myResult = myRetrievalOfGroupByAgentStatement.executeQuery();
		
		handleNoData();
		
		while(myResult.next()){
			groupIDArray.add(myResult.getInt(1));
		}
		
		return groupIDArray;
	}
	
	
	
	protected boolean isActorAgent(int theID) throws SQLException, XapiDataIntegrityException{
		myResult = SQLUtility.executeRetrievalByIDQuery(myRetrievalByIDStatement, theID);
		
		if(SQLUtility.isResultEmpty(myResult)){
			throw new XapiDataIntegrityException("Actor with that ID does not exists");
		}
		
		myResult.next();
		String theType = myResult.getString("actortypecode");
		if (theType.equals("AGT")) {
			return true;
		}
		return false;
	}
	
	
	
	protected boolean isActorGroup(int theID) throws SQLException, XapiDataIntegrityException{
		return !isActorAgent(theID);
	}
	
	
	
	/**
	 * Description:
	 * 	Close everything
	 * 
	 */
	protected void close() throws SQLException{
		super.close();
		SQLUtility.closeStatement(myMemberRetrievalStatement);
		SQLUtility.closeStatement(myRetrievalByAccountStatement);
		SQLUtility.closeStatement(myRetrievalByAccountStatement);
		SQLUtility.closeStatement(myRetrievalByMboxStatement);
		SQLUtility.closeStatement(myRetrievalByOpenIDStatement);
		SQLUtility.closeStatement(myRetrievalBySha1sumStatement);
		SQLUtility.closeStatement(myRetrievalOfGroupByAgentStatement);
	}
		
	
	
	public static void main(String[] args) {
		try {
			Connection conn = SQLUtility.establishDefaultConnection();
			XapiAccountSQLReader theAccountReader = new XapiAccountSQLReader(conn);
			XapiActorSQLReader theReader = new XapiActorSQLReader(conn, theAccountReader);
			
			/**
			XapiGroup g = (XapiGroup) theReader.retrieveByID(10099);
			System.out.println(g);
			System.out.println(theReader.retrieveIDByAccount("http://example.com/homePage", "GroupAccount"));
			
			if(theReader.doesActorExists(11111)){
				System.out.println(true);
			}else{
				System.out.println(false);
			}
			**/
			
			System.out.println(theReader.retrieveByMbox("mailto:xapi@adlnet.gov"));
			System.out.println(theReader.retrieveByOpenID("aaron.openid.example.org"));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
