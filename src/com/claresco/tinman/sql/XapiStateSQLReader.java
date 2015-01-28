/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiStateSQLReader.java	May 8, 2014
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

import java.io.IOError;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.claresco.tinman.lrs.XapiActor;
import com.claresco.tinman.lrs.XapiState;

/**
 * XapiStateSQLReader
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiStateSQLReader extends SQLReader {

	private PreparedStatement myRetrievalStatement;
	private PreparedStatement myRetrievalStatementWithRegistration;
	private PreparedStatement myRetrievalByIDStatement;
	private PreparedStatement myMultipleStatement;
	private PreparedStatement myMultipleStatementWithRegistration;
	private PreparedStatement myMultipleStatementWithSince;
	private PreparedStatement myMultipleStatementWithBoth;
	
	private String myTableName = "state";
	private String[] myFieldNames = new String[]{"activityid", "actorid", "statekey"};
	private String[] myFieldNamesWithRegistration = new String[]{"activityid", "actorid", "statekey", 
			"registrationuuid"};
	private String[] myFieldsToRetrieve = new String[]{"stateid", "documentid"};
	
	private XapiDocumentSQLReader myDocumentReader;
	private XapiActorSQLReader myActorReader;
	private XapiActivitySQLReader myActivityReader;

	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiStateSQLReader(Connection conn, XapiDocumentSQLReader theDocumentReader, 
			XapiActorSQLReader theActorReader, XapiActivitySQLReader theActivityReader)
			throws SQLException{
		myConn = conn;
		myDocumentReader = theDocumentReader;
		myActorReader = theActorReader;
		myActivityReader = theActivityReader;
		myRetrievalByIDStatement = SQLUtility.createRetrievalStatement(myConn, myTableName, "stateid");
		myRetrievalStatement = SQLUtility.createRetrievalStatement(myConn, myTableName, myFieldNames,
				myFieldsToRetrieve);
		myRetrievalStatementWithRegistration = SQLUtility.createRetrievalStatement(myConn, myTableName,
				myFieldNamesWithRegistration, myFieldsToRetrieve);
		myMultipleStatement = SQLUtility.createRetrievalStatement(myConn, myTableName, 
				new String[]{"activityid", "actorid"});
		myMultipleStatementWithRegistration = SQLUtility.createRetrievalStatement(myConn, myTableName, 
				new String[]{"activityid", "actorid", "registrationuuid"});
		myMultipleStatementWithSince = SQLUtility.createRetrievalStatement(myConn, myTableName, new 
				String[]{"activityid", "actorid", "stored"}, new String[]{"*"},
				new String[]{"=", "=", ">="});
		myMultipleStatementWithBoth = SQLUtility.createRetrievalStatement(myConn, myTableName, new 
				String[]{"activityid", "actorid", "registrationuuid","stored"}, new String[]{"*"},
				new String[]{"=", "=", "=",">="});		
	}
	
	
	
	protected String retrieveStateByID(int theID) throws SQLException, XapiSQLOperationProblemException{
		myRetrievalByIDStatement.setInt(1, theID);
		myResult = myRetrievalByIDStatement.executeQuery();
		
		if(isResulEmpty()){
			return null;
		}
		
		myResult.next();
		String theDocument = myDocumentReader.retrieveDocument(myResult.getInt("documentid"));
		
		return theDocument;
	} 
	
	
	
	protected String retrieveState(String theActivityIRI, XapiActor theActor, String theStateID) throws
			SQLException, XapiSQLOperationProblemException{
		myRetrievalStatement.setInt(1, myActivityReader.retrieveIDByValue(theActivityIRI));
		myRetrievalStatement.setInt(2, myActorReader.retrieveActorID(theActor));
		myRetrievalStatement.setString(3, theStateID);
		
		myResult = myRetrievalStatement.executeQuery();
		
		if(isResulEmpty()){
			return null;
		}
		
		myResult.next();
		int theDocumentID = myResult.getInt("documentid");
		return myDocumentReader.retrieveDocument(theDocumentID);
	}
	
	
	
	protected String retrieveState(String theActivityIRI, XapiActor theActor, String theStateID, String
			theRegistration) throws SQLException, XapiSQLOperationProblemException{
		myRetrievalStatementWithRegistration.setInt(1, myActivityReader.retrieveIDByValue(theActivityIRI));
		myRetrievalStatementWithRegistration.setInt(2, myActorReader.retrieveActorID(theActor));
		myRetrievalStatementWithRegistration.setString(3, theStateID);
		myRetrievalStatementWithRegistration.setString(4, theRegistration);

		myResult = myRetrievalStatementWithRegistration.executeQuery();
		
		if(isResulEmpty()){
			return null;
		}
		
		myResult.next();
		int theDocumentID = myResult.getInt("documentid");
	
		return myDocumentReader.retrieveDocument(theDocumentID);
	}
	
	
	
	protected HashMap<String, String> retrieveMultipleState(String theActivityIRI, XapiActor theActor) 
			throws SQLException, XapiSQLOperationProblemException{
		myMultipleStatement.setInt(1, myActivityReader.retrieveIDByValue(theActivityIRI));
		myMultipleStatement.setInt(2, myActorReader.retrieveActorID(theActor));
		
		myResult = myMultipleStatement.executeQuery();
		
		if(SQLUtility.isResultEmpty(myResult)){
			return null;
		}
		
		return getStatesFromResult();
	}
	
	
	
	protected HashMap<String, String> retrieveMultipleState(String theActivityIRI, XapiActor theActor, String 
			theRegistration) throws SQLException, XapiSQLOperationProblemException{
		myMultipleStatementWithRegistration.setInt(1, myActivityReader.retrieveIDByValue(theActivityIRI));
		myMultipleStatementWithRegistration.setInt(2, myActorReader.retrieveActorID(theActor));
		myMultipleStatementWithRegistration.setString(3, theRegistration);
		
		myResult = myMultipleStatementWithRegistration.executeQuery();
		
		if(SQLUtility.isResultEmpty(myResult)){
			return null;
		}
		
		return getStatesFromResult();
	}
	
	
	
	protected HashMap<String, String> retrieveMultipleState(String theActivityIRI, XapiActor theActor,
			DateTime theTimeStamp)  throws SQLException, XapiSQLOperationProblemException{
		
		myMultipleStatementWithSince.setInt(1, myActivityReader.retrieveIDByValue(theActivityIRI));
		myMultipleStatementWithSince.setInt(2, myActorReader.retrieveActorID(theActor));
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		myMultipleStatementWithSince.setTimestamp(3, SQLUtility.getTimestamp(theTimeStamp), cal);
		
		myResult = myMultipleStatementWithSince.executeQuery();
		
		if(SQLUtility.isResultEmpty(myResult)){
			return null;
		}
		
		return getStatesFromResult();
	}
	
	
	
	protected HashMap<String, String> retrieveMultipleState(String theActivityIRI, XapiActor theActor,
			String theRegistration, DateTime theTimeStamp)  throws SQLException, XapiSQLOperationProblemException{
		
		myMultipleStatementWithBoth.setInt(1, myActivityReader.retrieveIDByValue(theActivityIRI));
		myMultipleStatementWithBoth.setInt(2, myActorReader.retrieveActorID(theActor));
		myMultipleStatementWithBoth.setString(3, theRegistration);
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		myMultipleStatementWithBoth.setTimestamp(4, SQLUtility.getTimestamp(theTimeStamp), cal);
		
		myResult = myMultipleStatementWithBoth.executeQuery();
		
		if(SQLUtility.isResultEmpty(myResult)){
			return null;
		}
		
		return getStatesFromResult();
	}
	
	
	
	protected int retrieveIDByStatekey(XapiState theState) throws SQLException{
		if(theState.hasRegistration()){
			myRetrievalStatementWithRegistration.setInt(1, myActivityReader.retrieveIDByValue
					(theState.getActivityIRI()));
			myRetrievalStatementWithRegistration.setInt(2, myActorReader.retrieveActorID(
					theState.getActor()));
			myRetrievalStatementWithRegistration.setString(3, theState.getID());
			myRetrievalStatementWithRegistration.setString(4, theState.getRegistration().toString());

			myResult = myRetrievalStatementWithRegistration.executeQuery();
		}else{
			myRetrievalStatement.setInt(1, myActivityReader.retrieveIDByValue(
					theState.getActivityIRI()));
			myRetrievalStatement.setInt(2, myActorReader.retrieveActorID(theState.getActor()));
			myRetrievalStatement.setString(3, theState.getID());
			
			myResult = myRetrievalStatement.executeQuery();
		}
		
		if(isResulEmpty()){
			return -1;
		}
		myResult.next();
		return myResult.getInt("stateid");
	}
	
	
	
	private HashMap<String, String> getStatesFromResult() throws SQLException, 
			XapiSQLOperationProblemException{
		HashMap<String, String> theKeyDocumentMap = new HashMap<String, String>();
		
		while(myResult.next()){
			String theDocument = myDocumentReader.retrieveDocument(myResult.getInt("documentid"));
			theKeyDocumentMap.put(myResult.getString("statekey"), theDocument);
		}
		
		return theKeyDocumentMap;
	}
	
	
	protected ResultSet getStateResultSet(int theID) throws SQLException{
		myRetrievalByIDStatement.setInt(1, theID);
		return myRetrievalByIDStatement.executeQuery();
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.sql.SQLReader#close()
	 */
	@Override
	protected void close() throws SQLException {
		super.close();
		SQLUtility.closeStatement(myRetrievalByIDStatement);
		SQLUtility.closeStatement(myRetrievalStatement);
		SQLUtility.closeStatement(myRetrievalStatementWithRegistration);
		SQLUtility.closeStatement(myMultipleStatement);
		SQLUtility.closeStatement(myMultipleStatementWithBoth);
		SQLUtility.closeStatement(myMultipleStatementWithRegistration);
		SQLUtility.closeStatement(myMultipleStatementWithSince);
	}
}
