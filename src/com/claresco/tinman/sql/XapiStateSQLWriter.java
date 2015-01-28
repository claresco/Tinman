/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiStateSQLWriter.java	May 6, 2014
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

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.TimeZone;

import org.joda.time.DateTime;

import com.claresco.tinman.lrs.XapiActivity;
import com.claresco.tinman.lrs.XapiState;
import com.hp.hpl.jena.sdb.sql.MySQLEngineType;

/**
 * XapiStateSQLWriter
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiStateSQLWriter extends SQLWriter {
	
	private PreparedStatement myInsertStatement;
	private PreparedStatement myUpdateStatement;
	
	private String myTableName = "state";
	
	private XapiActivitySQLReader myActivityReader;
	
	private XapiActorSQLWriter myActorWriter;
	private XapiDocumentSQLWriter myDocumentWriter;
	private XapiActivitySQLWriter myActivityWriter;
	private XapiStateSQLReader myStateReader;
	
	private String[] myFieldNames = new String[]{"stateid", "activityid", "actorid", "statekey",
			"registrationuuid", "documentid", "stored"};
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiStateSQLWriter(Connection conn, XapiActorSQLWriter theActorWriter, XapiActivitySQLReader
			theActivityReader, XapiDocumentSQLWriter theDocumentWriter, XapiStateSQLReader theStateReader,
			 XapiActivitySQLWriter theActivityWriter) throws SQLException{
		this.myConn = conn;
		
		this.myActorWriter = theActorWriter;
		this.myActivityReader = theActivityReader;
		this.myActivityWriter = theActivityWriter;
		this.myDocumentWriter = theDocumentWriter;
		this.myStateReader = theStateReader;
		
		this.myInsertStatement = SQLUtility.createInsertStatement(myConn, myTableName, myFieldNames);
		this.myUpdateStatement = SQLUtility.createUpdateStatement(myConn, myTableName, new String[]
				{"activityid", "actorid", "registrationuuid"}, new String[]{"stateid"});
	}
	
	
	
	/**
	 * 
	 * Definition:
	 *	Insert new state. If the state with that statekey already exists,
	 *	update it.
	 *
	 * Params:
	 *
	 *
	 */
	protected int insertState(XapiState theState) throws SQLException, XapiDataIntegrityException, 
			XapiSQLOperationProblemException{
		int theStateID = myStateReader.retrieveIDByStatekey(theState);

		ResultSet myRS = myStateReader.getStateResultSet(theStateID);
		myRS.next();
		
		if(theStateID != -1){
			myDocumentWriter.updateDocument(theState.getDocument(), myRS.getInt("documentid"));
			updateState(theState, theStateID);
			
			myRS.close();
			return theStateID;
		}else{
			int theID = super.fetchId();

			myInsertStatement.setInt(1, theID);

			// Check the true value!!!!
			myInsertStatement.setInt(2, myActivityWriter.insertActivity(new XapiActivity
					(theState.getActivityIRI()), true));
			myInsertStatement.setInt(3, myActorWriter.insertNewActor(theState.getActor()));
			myInsertStatement.setString(4, theState.getID());

			myInsertStatement.setNull(5, Types.CHAR);
			if(theState.hasRegistration()){
				myInsertStatement.setString(5, theState.getRegistration().toString());
			}

			myInsertStatement.setInt(6, myDocumentWriter.insertNewDocument(theState.getDocument()));
			
			DateTime theStoredTime = DateTime.now();
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			myInsertStatement.setTimestamp(7, SQLUtility.getTimestamp(theStoredTime), cal);
			
			myInsertStatement.executeUpdate();

			myRS.close();
			
			return theID;
		}
	}
	
	
	
	protected void updateState(XapiState theState, int theStateID) throws SQLException, XapiDataIntegrityException{
		int theActvID = myActivityReader.retrieveIDByValue(theState.getActivityIRI());
		
		if(theActvID == -1){
			theActvID = myActivityWriter.insertNewActivity(new XapiActivity(theState.getActivityIRI()));
		}

		myUpdateStatement.setInt(1, theActvID);
		myUpdateStatement.setInt(2, myActorWriter.insertNewActor(theState.getActor()));
		
		myUpdateStatement.setNull(3, Types.CHAR);
		if(theState.hasRegistration()){
			myUpdateStatement.setString(3, theState.getRegistration().toString());
		}

		myUpdateStatement.setInt(4, theStateID);
		
		myUpdateStatement.executeUpdate();
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.sql.SQLWriter#close()
	 */
	@Override
	protected void close() throws SQLException {
		super.close();
		SQLUtility.closeStatement(myInsertStatement);
		SQLUtility.closeStatement(myUpdateStatement);
	}
}
