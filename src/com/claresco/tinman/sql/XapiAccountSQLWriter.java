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
import java.sql.SQLException;
import com.claresco.tinman.lrs.XapiAccount;

/**
 * XapiAccountSqlWriter.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Feb 20, 2014
 * 
 */

class XapiAccountSQLWriter extends SQLWriter{
	
	private PreparedStatement myInsertStatement;
	
	private XapiAccountSQLReader myAccountReader;
	
	private String[] myFieldNames = {"accountid", "accthomepage", "acctname"};
	
	/**
	 * Description:
	 *
	 * Params:
	 *
	 */
	public XapiAccountSQLWriter(Connection conn, XapiAccountSQLReader theAccountReader)
			throws SQLException{
		myConn = conn;
		
		myAccountReader = theAccountReader;
		
		myInsertStatement = SQLUtility.createInsertStatement(super.myConn, "account", myFieldNames);
	}
	
	
	
	protected int insertNewAccount(XapiAccount theAccount) throws SQLException{
		int theDatabaseID = -1;
		
		String theHomepage = theAccount.getHomePage().toString();
		String theName = theAccount.getName();
		
		theDatabaseID = myAccountReader.retrieveIDByValue(theHomepage, theName);
		
		if(theDatabaseID == -1){
			int theId = super.fetchId();

			myInsertStatement.setInt(1, theId);
			myInsertStatement.setString(2, theAccount.getHomePage().toString());
			myInsertStatement.setString(3, theAccount.getName());	

			myInsertStatement.executeUpdate();
			
			// Set databaseID to be the new account entry ID before return
			theDatabaseID = theId;
		}
		
		return theDatabaseID;
	}
	
	
	
	protected void close() throws SQLException{
		super.close();
		SQLUtility.closeStatement(myInsertStatement);
	}

	
	
	public static void main(String[] args) {
		
		try{
			Connection conn = SQLUtility.establishDefaultConnection();
			XapiAccount theAccount = new XapiAccount("claresco.com", "something");
		} catch(SQLException e){
			e.printStackTrace();
		}
		
	}
}
