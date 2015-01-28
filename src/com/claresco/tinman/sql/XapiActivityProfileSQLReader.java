/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiActivityProfileSQLReader.java	Jun 4, 2014
 *
 * Copyright 2014 Claresco Corporation, Berkeley, CA 94704. All Rights Reserved.
 *
 * This software is the proprietary information of Claresco Corporation.
 * Use is subject to license terms.
 *
 * Author : Rheza
 *
 */
package com.claresco.tinman.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import org.joda.time.DateTime;

import com.claresco.tinman.lrs.XapiActor;
import com.claresco.tinman.servlet.XapiNotAuthorizedException;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

/**
 * XapiActivityProfileSQLReader
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiActivityProfileSQLReader extends SQLReader {
	
	private PreparedStatement myRetrievalStatement;
	private PreparedStatement myRetrievalByIDStatement;
	
	private PreparedStatement myMultipleStatement;
	private PreparedStatement myMultipleStatementWithSince;
	
	private String myTableName = "activityprofile";
	private String[] myFieldNames = new String[]{"activityid", "profilekey"};
	private String[] myFieldsToRetrieve = new String[]{"activityprofileid", "documentid"};
	
	private XapiDocumentSQLReader myDocumentReader;
	private XapiActivitySQLReader myActivityReader;

	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiActivityProfileSQLReader(Connection conn, XapiDocumentSQLReader theDocumentReader, 
			XapiActivitySQLReader theActivityReader) throws SQLException{
		myConn = conn;
		myDocumentReader = theDocumentReader;
		myActivityReader = theActivityReader;
		
		myRetrievalByIDStatement = SQLUtility.createRetrievalStatement(myConn, myTableName, "activityprofileid");
		myRetrievalStatement = SQLUtility.createRetrievalStatement(myConn, myTableName, myFieldNames,
				myFieldsToRetrieve);
		
		myMultipleStatement = SQLUtility.createRetrievalStatement(myConn, myTableName, 
				new String[]{"activityid"});
		myMultipleStatementWithSince = SQLUtility.createRetrievalStatement(myConn, myTableName, 
				new String[]{"activityid", "stored"}, new String[]{"*"}, new String[]{"=", ">="});
	}
	
	
	
	protected String retrieveByID(int theID) throws SQLException, XapiSQLOperationProblemException{
		myRetrievalByIDStatement.setInt(1, theID);
		myResult = myRetrievalByIDStatement.executeQuery();
		
		myResult.next();
		String theDocument = myDocumentReader.retrieveDocument(myResult.getInt("documentid"));
		
		return theDocument;
	}
	
	
	
	protected int retrieveID(String theActivityIRI, String theProfileID) throws
			SQLException, XapiSQLOperationProblemException{
		int theDatabaseID = -1;
		
		myRetrievalStatement.setInt(1, myActivityReader.retrieveIDByValue(theActivityIRI));
		myRetrievalStatement.setString(2, theProfileID);
		
		myResult = myRetrievalStatement.executeQuery();
		
		if(isResulEmpty()){
			return -1;
		}
		
		myResult.next();
		theDatabaseID = myResult.getInt("activityprofileid");
		
		return theDatabaseID;
	}
	
	
	
	protected ResultSet getResultSet(String theActivityIRI, String theProfileID) throws 
			SQLException, XapiSQLOperationProblemException{
		myRetrievalStatement.setInt(1, myActivityReader.retrieveIDByValue(theActivityIRI));
		myRetrievalStatement.setString(2, theProfileID);
		
		myResult = myRetrievalStatement.executeQuery();
		
		return myResult;
	}
	
	
	protected String retrieveActivityProfile(String theActivityIRI, String theProfileID) throws
			SQLException, XapiDataNotFoundException, XapiSQLOperationProblemException{
		myRetrievalStatement.setInt(1, myActivityReader.retrieveIDByValue(theActivityIRI));
		myRetrievalStatement.setString(2, theProfileID);
		
		myResult = myRetrievalStatement.executeQuery();
		
		if(isResulEmpty()){
			String theMessage = String.format("Can't find the activity profile with activityId:" +
					" %s and profileId: %s", theActivityIRI, theProfileID);
			throw new XapiActivityProfileNotFoundException(theMessage);
		}
		
		myResult.next();
		int theDocumentID = myResult.getInt("documentid");
		return myDocumentReader.retrieveDocument(theDocumentID);
	}
	
	
	
	protected HashMap<String, String> retrieveMultipleActivityProfile(String theActivityID) 
			throws SQLException, XapiSQLOperationProblemException{
		myMultipleStatement.setInt(1, myActivityReader.retrieveIDByValue(theActivityID));
		myResult = myMultipleStatement.executeQuery();
		
		if(SQLUtility.isResultEmpty(myResult)){
			return null;
		}
		
		return getActivityProfileFromResult();
	}
	
	
	
	protected HashMap<String, String> retrieveMultipleActivityProfile(String theActivityID, 
			DateTime theTimestamp) throws SQLException, XapiSQLOperationProblemException{
		myMultipleStatementWithSince.setInt(1, myActivityReader.retrieveIDByValue(theActivityID));
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		myMultipleStatementWithSince.setTimestamp(2, SQLUtility.getTimestamp(theTimestamp), cal);
		
		myResult = myMultipleStatementWithSince.executeQuery();
		
		if(SQLUtility.isResultEmpty(myResult)){
			return null;
		}
		
		return getActivityProfileFromResult();
	}
	
	
	
	protected HashMap<String, String> getActivityProfileFromResult() throws SQLException, 
			XapiSQLOperationProblemException{
		HashMap<String, String> theKeyDocumentMap = new HashMap<String, String>();
		
		while(myResult.next()){
			String theDocument = myDocumentReader.retrieveDocument(myResult.getInt("documentid"));
			theKeyDocumentMap.put(myResult.getString("profilekey"), theDocument);
		}
		
		return theKeyDocumentMap;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.sql.SQLReader#close()
	 */
	@Override
	protected void close() throws SQLException {
		// TODO Auto-generated method stub
		super.close();
		SQLUtility.closeStatement(myRetrievalByIDStatement);
		SQLUtility.closeStatement(myRetrievalStatement);
		SQLUtility.closeStatement(myMultipleStatement);
		SQLUtility.closeStatement(myMultipleStatementWithSince);
	}
}
