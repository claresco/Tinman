/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiAgentProfileSQLReader.java	Jun 25, 2014
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import org.joda.time.DateTime;

import com.claresco.tinman.lrs.XapiActor;

/**
 * XapiAgentProfileSQLReader
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiAgentProfileSQLReader extends SQLReader {
	
	private PreparedStatement mySingleRetrievalStatement;
	private PreparedStatement myMultipleRetrievalStatement;
	private PreparedStatement myMultipleRetrievalStatementWithSince;
	
	private XapiDocumentSQLReader myDocumentReader;
	private XapiActorSQLReader myActorReader;
	
	private String myTableName = "agentprofile";
	private String[] mySingleRetrievalFieldNames = new String[]{"actorid", "profilekey"};
	private String[] myMultipleRetrievalFieldNames = new String[]{"actorid", "stored"};
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiAgentProfileSQLReader(Connection conn, XapiActorSQLReader theActorReader, 
			XapiDocumentSQLReader theDocumentReader) throws SQLException {
		this.myConn = conn;
		this.myActorReader = theActorReader;
		this.myDocumentReader = theDocumentReader;
		
		mySingleRetrievalStatement = SQLUtility.createRetrievalStatement(myConn, myTableName,
				mySingleRetrievalFieldNames);
		myMultipleRetrievalStatement = SQLUtility.createRetrievalStatement(myConn, myTableName, "actorid");
		myMultipleRetrievalStatementWithSince = SQLUtility.createRetrievalStatement(myConn, myTableName, 
				myMultipleRetrievalFieldNames, new String[]{"*"}, new String[]{"=", ">="});
	}
	
	
	
	protected String retrieveSingleAgentProfile(XapiActor theActor, String theProfileId) throws SQLException, 
			XapiSQLOperationProblemException{
		myResult = getResultSet(theActor, theProfileId);
	
		if(myResult == null){
			return null;
		}
		
		if(isResulEmpty()){
			return null;
		}
		
		myResult.next();
		
		return myDocumentReader.retrieveDocument(myResult.getInt("documentid"));
	}
	
	
	
	protected int retrieveID(XapiActor theActor, String theProfileID) throws SQLException{
		myResult = getResultSet(theActor, theProfileID);
		
		if(myResult == null){
			return -1;
		}
		
		if(isResulEmpty()){
			return -1;
		}
		
		myResult.next();
		
		return myResult.getInt("stateid");
	}
	
	
	
	protected ResultSet getResultSet(XapiActor theActor, String theProfileID) throws SQLException{
		int theActorID = myActorReader.retrieveActorID(theActor);
		
		if(theActorID == -1){
			return null;
		}
		
		mySingleRetrievalStatement.setInt(1, theActorID);
		mySingleRetrievalStatement.setString(2, theProfileID);
		
		return mySingleRetrievalStatement.executeQuery();
	}
	
	
	
	protected HashMap<String, String> retrieveMultipleAgentProfile(XapiActor theActor) throws SQLException, 
			XapiDataNotFoundException, XapiSQLOperationProblemException{
		int theActorID = myActorReader.retrieveActorID(theActor);

		if(theActorID == -1){
			throw new XapiActorNotFoundException("Can't find this agent in the database");
		}

		myMultipleRetrievalStatement.setInt(1, theActorID);

		myResult = myMultipleRetrievalStatement.executeQuery();

		if(isResulEmpty()){
			throw new XapiAgentProfileNotFoundException("Can't find the agent profile belonging to this actor");
		}

		return getAgentProfileFromResult();
	}
	
	
	
	protected HashMap<String, String> retrieveMultipleAgentProfile(XapiActor theActor, DateTime theTimestamp) throws SQLException, 
			XapiDataNotFoundException, XapiSQLOperationProblemException{
		int theActorID = myActorReader.retrieveActorID(theActor);

		if(theActorID == -1){
			throw new XapiActorNotFoundException("Can't find this actor in the databases");
		}

		myMultipleRetrievalStatementWithSince.setInt(1, theActorID);
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		myMultipleRetrievalStatementWithSince.setTimestamp(2, SQLUtility.getTimestamp(theTimestamp), cal);

		myResult = myMultipleRetrievalStatementWithSince.executeQuery();

		if(isResulEmpty()){
			throw new XapiAgentProfileNotFoundException("Can't find the agent profile belonging to this actor");
		}

		return getAgentProfileFromResult();
	}
	
	
	
	protected HashMap<String, String> getAgentProfileFromResult() throws SQLException, 
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
		super.close();
		SQLUtility.closeStatement(myMultipleRetrievalStatement);
		SQLUtility.closeStatement(myMultipleRetrievalStatementWithSince);
		SQLUtility.closeStatement(mySingleRetrievalStatement);
	}
}
