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
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.claresco.tinman.json.JsonUtility;
import com.claresco.tinman.lrs.XapiStatement;

import com.claresco.tinman.lrs.XapiVerb;
import com.google.gson.Gson;


/**
 * XapiVerbSqlWriter.java
 *
 * To Write verb to database, may not needed though
 *
 *
 *
 * @author rheza
 * on Feb 24, 2014
 * 
 */

class XapiVerbSQLWriter extends SQLWriter {
	
	private PreparedStatement myInsertStatement;
	
	private XapiLanguageMapSQLWriter myLanguageMapWriter;
	private XapiVerbSQLReader myVerbReader;
	
	private String myTableName = "verb";
	private String[] myFieldNames = new String[]{"verbid", "verbiri", "languagemapid"};
	
	/**
	 * 
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiVerbSQLWriter(Connection conn, XapiLanguageMapSQLWriter theLanguageMapWriter, 
			XapiVerbSQLReader theVerbReader) throws SQLException {
		// TODO Auto-generated constructor stub
		myConn = conn;
		myInsertStatement = SQLUtility.createInsertStatement(super.myConn, myTableName, myFieldNames);
		myLanguageMapWriter = theLanguageMapWriter;
		myVerbReader = theVerbReader;
	}

	
	
	protected int insertNewVerb(XapiVerb theVerb) throws SQLException, XapiDataIntegrityException{
		int verbIDFromReader = myVerbReader.retrieveIDByValue(theVerb.getId().toString());
		
		// Check if the verb already in the database
		if(verbIDFromReader != -1){
			return verbIDFromReader;
		// If it is not, throw an error
		}else{
			throw new XapiVerbInvalidException("This verb is not currently accepted, please contact your supervisor");
		}

	}
	
	
	
	protected void close() throws SQLException{
		super.close();
		SQLUtility.closeStatement(myInsertStatement);
	}

	
	
	public static void main(String[] args) {
		try {
			Gson gson = JsonUtility.createGson();
			
			 String path = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/VerbDatabaseTesting.json";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
