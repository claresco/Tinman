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
import java.util.UUID;

import com.claresco.tinman.lrs.XapiAccount;
import com.claresco.tinman.lrs.XapiContext;
import com.claresco.tinman.lrs.XapiContextActivities;
import com.claresco.tinman.lrs.XapiStatementRef;
import com.hp.hpl.jena.sparql.function.library.print;

/**
 * XapiContextSQLReader.java
 *
 * Read context from database
 *
 *
 * STATUS:
 * 	YELLOW
 *
 * @author rheza
 * on Mar 10, 2014
 * 
 */

public class XapiContextSQLReader extends SQLReader{

	private String myTableName = "context";
	
	private PreparedStatement myIDRetrievalStatement;
	private XapiContextActivitiesSQLReader myContextActivitiesReader;
	
	/**
	 * Description:
	 *
	 * Params:
	 *
	 */	
	public XapiContextSQLReader(Connection conn, XapiContextActivitiesSQLReader theCtxtActvReader)
			throws SQLException{
		this.myConn = conn;
		this.myIDRetrievalStatement = SQLUtility.createRetrievalStatement(super.myConn,
				this.myTableName, "contextid");
		myContextActivitiesReader = theCtxtActvReader;
	}
	
	/**
	 * 
	 * Description:
	 *	Retrieve XapiContext by its id in the database
	 *
	 * Params:
	 *
	 */
	protected XapiContext retrieveByID(int theID) throws SQLException{
		this.myIDRetrievalStatement.setInt(1, theID);
		
		myResult = this.myIDRetrievalStatement.executeQuery();
				
		// How to make sure there is no duplicate
		
		if(isResulEmpty()){
			return null;
		}
		
		
		myResult.next();
		
		String theRegistrationString = myResult.getString("ctxtregistration");
		
		UUID theContextRegistration = null;
		
		if(theRegistrationString != null){
			theContextRegistration = UUID.fromString(theRegistrationString);
		}
		
		String theRevision = myResult.getString("ctxtrevision");
		String thePlatform = myResult.getString("ctxtplatform");
		String theLanguage = myResult.getString("languagecode");
		
		XapiContextActivities theCtxtActv = myContextActivitiesReader.retrieveContextActivities(theID);
		
		XapiStatementRef theStatementRef = null;
		int theStatementID = myResult.getInt("statementid");
		if(!myResult.wasNull()){
		}
		
		return new XapiContext(theContextRegistration, theRevision, thePlatform,
				theLanguage, theCtxtActv);
	}
	
	/**
	 * 
	 * Description:
	 * 	Close everything
	 * 
	 */
	protected void close() throws SQLException{
		super.close();
		SQLUtility.closeStatement(myIDRetrievalStatement);
	}
	
	
	
	public static void main(String[] args) {
		try {
			Connection conn = SQLUtility.establishDefaultConnection();
			XapiLanguageMapSQLReader theLMapReader = new XapiLanguageMapSQLReader(conn);
			XapiExtensionSQLReader theExtReader = new XapiExtensionSQLReader(conn);
			XapiActivitySQLReader theActvReader = new XapiActivitySQLReader(conn, theLMapReader, theExtReader);
			XapiContextActivitiesSQLReader theCtxtActvReader = new XapiContextActivitiesSQLReader(conn, theActvReader);
			XapiContextSQLReader theCtxtReader = new XapiContextSQLReader(conn, theCtxtActvReader);
			
			XapiContext theContext = theCtxtReader.retrieveByID(10498);
			System.out.println(theContext);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
