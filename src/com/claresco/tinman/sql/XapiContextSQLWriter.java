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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.claresco.tinman.json.JsonUtility;
import com.claresco.tinman.lrs.XapiAccount;
import com.claresco.tinman.lrs.XapiContext;
import com.google.gson.Gson;

/**
 * XapiContextSQLWriter.java
 *
 * Write XapiContext into database
 *
 *
 * STATUS:
 * 	YELLOW
 *
 * @author rheza
 * on Mar 10, 2014
 * 
 */

public class XapiContextSQLWriter extends SQLWriter{

	private PreparedStatement myInsertStatement;

	private XapiActorSQLWriter myActorWriter;
	private XapiStatementSQLReader myStatementReader;
	private XapiContextActivitiesSQLWriter myContextActivitiesWriter;
	
	private String[] myFieldNames = {"contextid", "statementid", "ctxtregistration", 
			"ctxtinstructor", "ctxtteam", "ctxtrevision", "ctxtplatform", "languagecode"};
	
	/**
	 * Description:
	 *	Constructor
	 *
	 * Params:
	 *
	 */
	protected XapiContextSQLWriter(Connection conn, XapiActorSQLWriter theActorWriter,
			XapiStatementSQLReader theStatementReader, XapiContextActivitiesSQLWriter 
			theContextActivitiesWriter) throws SQLException{
		myConn = conn;
		myInsertStatement = SQLUtility.createInsertStatement(super.myConn, "context", myFieldNames);
		myActorWriter = theActorWriter;
		myStatementReader = theStatementReader;
		myContextActivitiesWriter = theContextActivitiesWriter;
	}
	
	/**
	 * 
	 * Description:
	 *	Insert an instance of XapiContext into the database	
	 *
	 * Params:
	 *
	 */
	protected int insertNewContext(XapiContext theContext, boolean newActivityAllowed) throws SQLException,
			XapiDataIntegrityException{
		int theId = super.fetchId();

		//default value
		myInsertStatement.setInt(1, theId);
		myInsertStatement.setNull(2, Types.NUMERIC);
		myInsertStatement.setNull(3, Types.CHAR);
		myInsertStatement.setNull(4, Types.NUMERIC);
		myInsertStatement.setNull(5, Types.NUMERIC);
		myInsertStatement.setNull(6, Types.CHAR);
		myInsertStatement.setNull(7, Types.CHAR);
		myInsertStatement.setNull(8, Types.CHAR);

		if(theContext.hasStatementReference()){
			int theStatementID = myStatementReader.findIDByUUID(theContext.getStatementReference().
					getId().toString());
			if(theStatementID != -1){
				myInsertStatement.setInt(2, theStatementID);
			}else{
				throw new XapiStatementReferenceInvalidException("There is no statement with that UUID" +
						" in the database");
			}
			
		}
		if(theContext.hasRegistration()){
			myInsertStatement.setString(3, theContext.getRegistration().toString());
		}
		if(theContext.hasInstructor()){
			myInsertStatement.setInt(4, myActorWriter.insertNewActor(theContext.getInstructor()));
		}
		if(theContext.hasTeam()){
			myInsertStatement.setInt(5, myActorWriter.insertNewActor(theContext.getTeam()));
		}
		if(theContext.hasRevision()){
			myInsertStatement.setString(6, theContext.getRevision());
		}
		
		if(theContext.hasPlatform()){
			myInsertStatement.setString(7, theContext.getPlatform());
		}

		if(theContext.hasLanguage()){
			myInsertStatement.setString(8, theContext.getLanguage());
		}
		
		int result = myInsertStatement.executeUpdate();

		if(theContext.hasContextActivities()){
			myContextActivitiesWriter.insertNewContextActivities(theContext.getContextActivities(),
					theId, newActivityAllowed);
		}
		
		
		if(result == 1){
			return theId;
		}
		return -1;
	}
	
	/**
	 * Description:
	 * 	Close everything
	 * 
	 */
	protected void close() throws SQLException{
		super.close();
		SQLUtility.closeStatement(myInsertStatement);
	}
	
	public static void main(String[] args) {
		
		try{
			Gson gson = JsonUtility.createGson();
			String path = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/Context.json";
			 
			BufferedReader bf = new BufferedReader(new FileReader(path));
			XapiContext s = gson.fromJson(bf, XapiContext.class);
				 
			System.out.println(s);
				 
			String json = gson.toJson(s);
			System.out.println(json);
			
			Connection conn = SQLUtility.establishDefaultConnection();
			//XapiContextSQLWriter theWriter = new XapiContextSQLWriter(conn);
			//theWriter.insertNewContext(s, 10195);
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
