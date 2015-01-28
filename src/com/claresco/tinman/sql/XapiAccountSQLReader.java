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

import com.claresco.tinman.lrs.XapiAccount;

/**
 * XapiAccountSQLReader.java
 *
 * Read Xapi Account from database
 *
 *
 *
 * @author rheza
 * on Feb 26, 2014
 * 
 */

class XapiAccountSQLReader extends SQLReader {

	private String myTableName = "account";
	
	private PreparedStatement myIDRetrievalStatement;
	private PreparedStatement myRetrievalByValueStatement;
	
	private String[] fieldNames = {"accthomepage", "acctname"};
	
	/**
	 * Description:
	 *
	 * Params:
	 *
	 */	
	public XapiAccountSQLReader(Connection conn) throws SQLException{
		this.myConn = conn;
		this.myTableName = "account";
		this.myIDRetrievalStatement = SQLUtility.createRetrievalStatement(super.myConn, this.myTableName, "accountid");
		this.myRetrievalByValueStatement = SQLUtility.createRetrievalStatement(super.myConn, myTableName, fieldNames);
	}
	
	protected XapiAccount retrieveByID(int theID) throws SQLException{
		this.myIDRetrievalStatement.setInt(1, theID);
		
		ResultSet myResult = this.myIDRetrievalStatement.executeQuery();
				
		// How to make sure there is no duplicate
		
		if(!myResult.isBeforeFirst()){
			System.out.println("NoData");
		}
		
		String theHomePage = "";
		String theName = "";
		
		// Open the result set
		myResult.next();
		theHomePage = myResult.getString("accthomepage");
		theName = myResult.getString("acctname");
		
		return new XapiAccount(theHomePage, theName);
	}
	
	
	/**
	 * 
	 * Description:
	 *	Return -1 if there is none.
	 * Params:
	 *
	 */
	protected int retrieveIDByValue(String theHomepage, String theName) throws SQLException{
		if(theHomepage == null && theName == null){
			return -1;
		}
		
		this.myRetrievalByValueStatement.setString(1, theHomepage);
		this.myRetrievalByValueStatement.setString(2, theName);
		
		myResult  = this.myRetrievalByValueStatement.executeQuery();
		
		if(!myResult.isBeforeFirst()){
			return -1;
		}
		
		myResult.next();
		return myResult.getInt(1);
	}
	
	
	
	protected void close() throws SQLException{
		super.close();
		this.myIDRetrievalStatement.close();
		this.myRetrievalByValueStatement.close();
	}
	
	
	
	public static void main(String[] args) {
		try {
			Connection conn = SQLUtility.establishDefaultConnection();
			
			XapiAccountSQLReader theReader = new XapiAccountSQLReader(conn);
			XapiAccount act = theReader.retrieveByID(10100);
			System.out.println(act);
			//System.out.println(theReader.retrieveIDByValue("claresco.com", "something"));
			System.out.println(theReader.retrieveIDByValue(null, null));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
}
