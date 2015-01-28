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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import com.claresco.tinman.lrs.XapiAccount;
import com.claresco.tinman.lrs.XapiAgent;
import com.claresco.tinman.lrs.XapiInverseFunctionalIdentifier;

/**
 * XapiAgentSQLReader.java
 *
 * May not need it
 *
 *
 *
 * @author rheza
 * on Feb 26, 2014
 * 
 */

class XapiAgentSQLReader extends SQLReader {
	
	private String myTableName = "actor";
	
	private String myIDFieldName = "actorid";

	private PreparedStatement myRetrievalByIDStatement;
	private XapiAccountSQLReader myAccountReader;
	
	/**
	 * 
	 * Description:
	 *	Constructor
	 *
	 * Params:
	 *
	 */
	public XapiAgentSQLReader(Connection conn) throws SQLException{
		this.myConn = conn;
		this.myRetrievalByIDStatement = SQLUtility.createRetrievalStatement(super.myConn, this.myTableName, this.myIDFieldName);
		this.myAccountReader = new XapiAccountSQLReader(myConn);
	}
	
	public XapiAgentSQLReader(Connection conn, XapiAccountSQLReader theAccountReader) throws SQLException{
		this.myConn = conn;
		this.myRetrievalByIDStatement = SQLUtility.createRetrievalStatement(super.myConn, this.myTableName, this.myIDFieldName);
		this.myAccountReader = theAccountReader;
	}
	
	protected XapiAgent retrieveByID(int theID) throws SQLException{
		this.myRetrievalByIDStatement.setInt(1, theID);
		
		ResultSet myResult = this.myRetrievalByIDStatement.executeQuery();
				
		if(!myResult.isBeforeFirst()){
			return null;
		}

		String theName;
		String theMBox;
		String theMBoxSHA1;
		String theOpenID;
		int theAccountID;
		XapiAccount theAccount = null;
		
		myResult.next();
		
		theName = myResult.getString(3);
		theMBox = myResult.getString(4);
		theMBoxSHA1 = myResult.getString(5);
		theOpenID = myResult.getString(6);
		theAccountID = myResult.getInt(7);		
		
		if(theAccountID > 0){
			theAccount = myAccountReader.retrieveByID(theAccountID);
		}
		
		// some group will not have an identifier
		XapiInverseFunctionalIdentifier theIdentifier = new XapiInverseFunctionalIdentifier(theMBox, theMBoxSHA1, theOpenID, theAccount);
		
		return new XapiAgent(theName, theIdentifier);
	}
	
	public static void main(String[] args) {
		try {
			Connection conn = SQLUtility.establishDefaultConnection();
			XapiAgentSQLReader theReader = new XapiAgentSQLReader(conn);
			theReader.retrieveByID(10101);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
