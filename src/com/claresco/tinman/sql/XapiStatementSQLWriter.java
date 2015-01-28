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

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

import org.joda.time.DateTime;

import com.claresco.tinman.json.JsonUtility;
import com.claresco.tinman.lrs.XapiObject;
import com.claresco.tinman.lrs.XapiStatement;
import com.claresco.tinman.lrs.XapiStatementRef;
import com.google.gson.Gson;

/**
 * XapiStatementSqlWriter.java
 *
 * Write Statement to Database, the only class that should be visible 
 * to other classes outside the package
 *
 * Status:
 * 	YELLOW
 *
 * @author rheza
 * on Feb 24, 2014
 * 
 */

class XapiStatementSQLWriter extends SQLWriter {

	private PreparedStatement myInsertStatement;
	private PreparedStatement myVoidingStatement;

	private XapiActorSQLWriter myActorWriter;
	private XapiVerbSQLWriter myVerbWriter;
	private XapiObjectSQLWriter myObjectWriter;
	private XapiStatementSQLReader myStatementReader;
	private XapiContextSQLWriter myContextWriter;
	private XapiResultSQLWriter myResultWriter;
	
	private String myTableName = "statement";
	private String[] myFieldNames = {"statementid", "actorid", "verbid", "objectid",
			"resultid", "contextid", "sttime", "ststored" , "authorityid", "stversion",
			"statementuuid"};
	
	/**
	 * 
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiStatementSQLWriter(Connection conn, XapiActorSQLWriter theActorWriter, 
			XapiVerbSQLWriter theVerbWriter, XapiObjectSQLWriter theObjectWriter,
			XapiStatementSQLReader theStatementReader, XapiContextSQLWriter theContextSQLWriter,
			XapiResultSQLWriter theResultWriter) throws SQLException{
		this.myConn = conn;
		this.myInsertStatement = SQLUtility.createInsertStatement(super.myConn, myTableName, 
				myFieldNames);
		this.myActorWriter = theActorWriter;
		this.myVerbWriter = theVerbWriter;
		this.myObjectWriter = theObjectWriter;
		this.myStatementReader = theStatementReader;
		this.myContextWriter = theContextSQLWriter;
		this.myResultWriter = theResultWriter;
		
		this.myVoidingStatement = SQLUtility.createUpdateStatement(myConn, myTableName, new 
				String[]{"isvoided"}, new String[]{"statementid"});
	}
	
	
	
	/**
	 * 
	 * Description:
	 * 	Insert a new statement to the database
	 * 
	 * Params:
	 * 	theStatement : the Statement object
	 */
	protected int insertNewStatement(XapiStatement theStatement, boolean newActivityAllowed, boolean generateRandomID) throws
			 SQLException, XapiDataIntegrityException{
		int theId = super.fetchId();
			
		this.myInsertStatement.setInt(1, theId);
		this.myInsertStatement.setInt(2, this.myActorWriter.insertNewActor(theStatement.getActor()));
		this.myInsertStatement.setInt(3, this.myVerbWriter.insertNewVerb(theStatement.getVerb()));
		this.myInsertStatement.setInt(4, this.myObjectWriter.insertNewObject(theStatement.getObject(), 
				newActivityAllowed));
		this.myInsertStatement.setNull(5, Types.NUMERIC);
		this.myInsertStatement.setNull(6, Types.NUMERIC);
		this.myInsertStatement.setNull(7, Types.TIMESTAMP);
		this.myInsertStatement.setNull(8, Types.TIMESTAMP);
		this.myInsertStatement.setNull(9, Types.NUMERIC);
		this.myInsertStatement.setNull(10, Types.CHAR);
		this.myInsertStatement.setString(11, theStatement.getId());
		
		if(theStatement.hasResult()){
			this.myInsertStatement.setInt(5, this.myResultWriter.insertNewResult(theStatement.getResult()));
		}
		
		if(theStatement.hasContext()){
			this.myInsertStatement.setInt(6, this.myContextWriter.insertNewContext
					(theStatement.getContext(), newActivityAllowed));
		}
		
		// Set timestamp
		if(theStatement.hasTimeStamp()){
			Timestamp theTimestamp = SQLUtility.getTimestamp(theStatement.getTimeStamp());
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			this.myInsertStatement.setTimestamp(7, theTimestamp, cal);
		}
		
		DateTime theStoredTime = DateTime.now();
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		this.myInsertStatement.setTimestamp(8, SQLUtility.getTimestamp(theStoredTime), cal);
		
		// Check if a statement with that UUID already exists in the database
		if(!generateRandomID){
			if (myStatementReader.doesStatementExists(theStatement.getId())) {
				// If so, throw an error
				throw new XapiDuplicateStatementIDException("Statement with that UUID already exists");
			}
		}else{
			this.myInsertStatement.setString(11, UUID.randomUUID().toString());
		}
		
		// If the statement voids another statement
		if(theStatement.isVoiding()){
			voidStatement(theStatement);
		}
		
		this.myInsertStatement.executeUpdate();
		
		return theId;
	}
	
	
	
	private void voidStatement(XapiStatement theStatement) throws XapiStatementReferenceInvalidException, 
			SQLException{
		XapiObject theObject = theStatement.getObject();
		
		if(!theObject.getObjectType().equals("StatementRef") || !(theObject instanceof XapiStatementRef)){
			throw new XapiStatementReferenceInvalidException("the Object must be a statement reference");
		}
		
		XapiStatementRef theStatementRef = (XapiStatementRef) theObject;
		
		int id = myStatementReader.findIDByUUID(theStatementRef.getId().toString());
		
		if(id == -1){
			throw new XapiStatementReferenceInvalidException("Could not find the statement being referenced");
		}
		
		myVoidingStatement.setInt(1, 1);
		myVoidingStatement.setInt(2, id);
		myVoidingStatement.executeUpdate();
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.sql.SQLWriter#close()
	 */
	@Override
	protected void close() throws SQLException {
		super.close();
		SQLUtility.closeStatement(myInsertStatement);
		SQLUtility.closeStatement(myVoidingStatement);
	}
	
	
	
	public static void main(String[] args) {
		Gson gson = JsonUtility.createGson();

		 String path1 = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/Statement5.json";
		 
		 try{
			 BufferedReader bf = new BufferedReader(new FileReader(path1));
			 XapiStatement s = gson.fromJson(bf, XapiStatement.class);
			 
			 Connection conn = SQLUtility.establishDefaultConnection();
			 
		 } catch(Exception e){
			 e.printStackTrace();
		 } finally{
		 }
	}
}
