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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.claresco.tinman.lrs.XapiAccount;
import com.claresco.tinman.lrs.XapiAgent;
import com.claresco.tinman.lrs.XapiGroup;
import com.claresco.tinman.lrs.XapiInverseFunctionalIdentifier;

/**
 * XapiGroupSQLReader.java
 *
 * May not need this
 *
 *
 *
 * @author rheza
 * on Feb 27, 2014
 * 
 */

class XapiGroupSQLReader extends SQLReader {
	
	private String myTableName = "actor";
	private String myMemberTableName = "groupactors";
	
	private PreparedStatement myRetrievalByIDStatement;
	private PreparedStatement myMemberRetrievalStatement;
	
	private XapiAccountSQLReader myAccountReader;
	private XapiAgentSQLReader myAgentReader;
	
	
	
	public XapiGroupSQLReader(Connection conn) throws SQLException{
		this.myConn = conn;
		this.myRetrievalByIDStatement = SQLUtility.createRetrievalStatement
				(super.myConn, this.myTableName, "actorid");
		this.myMemberRetrievalStatement = SQLUtility.createRetrievalStatement
				(super.myConn, this.myMemberTableName, "groupid");
		this.myAccountReader = new XapiAccountSQLReader(super.myConn);
		this.myAgentReader = new XapiAgentSQLReader(super.myConn);
	}
	
	
	
	protected XapiGroup retrieveByID(int theID) throws SQLException{
		this.myRetrievalByIDStatement.setInt(1, theID);
		
		ResultSet myResult = this.myRetrievalByIDStatement.executeQuery();
				
		// How to make sure there is no duplicate	
		if(!myResult.isBeforeFirst()){
			return null;
		}
		
		int theGroupID;
		String theName;
		String theMBox;
		String theMBoxSHA1;
		String theOpenID;
		int theAccountID;
		XapiAccount theAccount = null;
		
		myResult.next();
		
		theGroupID = myResult.getInt(1);
		theName = myResult.getString(3);
		theMBox = myResult.getString(4);
		theMBoxSHA1 = myResult.getString(5);
		theOpenID = myResult.getString(6);
		theAccountID = myResult.getInt(7);		
		ArrayList<XapiAgent> theMember = new ArrayList<XapiAgent>();
		
		if(theAccountID > 0){
			theAccount = myAccountReader.retrieveByID(theAccountID);
		}
		
		this.myMemberRetrievalStatement.setInt(1, theGroupID);
		myResult = this.myMemberRetrievalStatement.executeQuery();
		
		while(myResult.next()){
			theMember.add(this.myAgentReader.retrieveByID(myResult.getInt("agentid")));
		}
				
		XapiInverseFunctionalIdentifier theIdentifier = new XapiInverseFunctionalIdentifier(theMBox, theMBoxSHA1, theOpenID, theAccount);
		
		return new XapiGroup(theName, theMember, theIdentifier);
	}
	
	
	
	public static void main(String[] args) {
		try {
			Connection conn = SQLUtility.establishDefaultConnection();
			XapiGroupSQLReader theReader = new XapiGroupSQLReader(conn);
			XapiGroup g = theReader.retrieveByID(10099);
			System.out.println(g);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
