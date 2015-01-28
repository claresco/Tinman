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

import com.claresco.tinman.lrs.XapiLanguageMap;

/**
 * XapiLanguageMapSqlWriter.java
 *
 * STATUS:
 * 	GREEN
 *
 *
 *
 * @author rheza
 * on Feb 24, 2014
 * 
 */

class XapiLanguageMapSQLWriter extends SQLWriter{
	
	private PreparedStatement myInsertStatement;
	private PreparedStatement myLanguageInsertStatement;	
	
	private PreparedStatement myUpdateStatement;
	
	private XapiLanguageMapSQLReader myLanguageMapReader;
	
	private String[] myFieldNames = {"languagemapid", "lmdesc"};
	private String[] myLanguageFieldNames = {"languagemapid", "languagecode", "lmldisplay"};
	
	
	
	/**
	 * 
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiLanguageMapSQLWriter(Connection conn, XapiLanguageMapSQLReader theLanguageMapReader) throws SQLException{
		this.myConn = conn;
		
		this.myInsertStatement = SQLUtility.createInsertStatement(super.myConn, "languagemap", myFieldNames);
		this.myLanguageInsertStatement = SQLUtility.createInsertStatement(super.myConn, "languagemaplanguage",
				myLanguageFieldNames);
		this.myUpdateStatement = SQLUtility.createUpdateStatement(myConn, "languagemaplanguage", new String[]{"lmldisplay", 
				"languagecode"}, new String[]{"languagemapid", "languagecode"});
		
		myLanguageMapReader = theLanguageMapReader;
	}
	
	
	
	protected int insertNewLanguageMap(XapiLanguageMap theLanguageMap) throws SQLException{
		int myId = super.fetchId();
		myLanguageInsertStatement.setInt(1, myId);
		String[][] myArray = theLanguageMap.getLanguageMapAsArray();
		
		myInsertStatement.setInt(1, myId);
		myInsertStatement.setString(2, "something");
		myInsertStatement.executeUpdate();
		
		for (int i = 0; i < myArray.length; i++) {
			String temp = myArray[i][0];
			
			myLanguageInsertStatement.setString(2, temp);
			myLanguageInsertStatement.setString(3, myArray[i][1]);
			myLanguageInsertStatement.executeUpdate();
		}

		return myId;
	}
	
	
	
	private void insertOneLanguageEntry(int theLanguageMapID, String theDisplay, String theLang)
			throws SQLException{
		myLanguageInsertStatement.setInt(1, theLanguageMapID);
		myLanguageInsertStatement.setString(2, theDisplay);
		myLanguageInsertStatement.setString(3, theLang);
		myLanguageInsertStatement.executeUpdate();
	}
	
	
	
	protected void updateLanguageMap(XapiLanguageMap theLanguageMap, int theLanguageMapID) throws SQLException{
		ResultSet myRS = myLanguageMapReader.getLanguageMapResultSet(theLanguageMapID);
		
		String[][] myArray = theLanguageMap.getLanguageMapAsArray();
		
		for (int i = 0; i < myArray.length; i++) {
			String lang = myArray[i][0];
			String display = myArray[i][1];
			
			if(myRS.next()){
				myUpdateStatement.setString(1, display);
				myUpdateStatement.setString(2, lang);
				myUpdateStatement.setInt(3, theLanguageMapID);
				// Try to update the entry with same language first
				myUpdateStatement.setString(4, lang);
				int changeAnything = myUpdateStatement.executeUpdate();

				if(changeAnything == 0){
					myUpdateStatement.setString(4, myRS.getString("languagecode"));
					myUpdateStatement.executeUpdate();
				}
			}else{
				insertOneLanguageEntry(theLanguageMapID, lang, display);
			}
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.sql.SqlWriter#close()
	 */
	@Override
	protected void close() throws SQLException {
		super.close();
		SQLUtility.closeStatement(myInsertStatement);
		SQLUtility.closeStatement(myLanguageInsertStatement);
		SQLUtility.closeStatement(myUpdateStatement);
	}
	
	
	
	
	
	
	
	public static void main(String[] args) {
		try{
			Connection conn = SQLUtility.establishDefaultConnection();
			XapiLanguageMapSQLReader theLReader = new XapiLanguageMapSQLReader(conn);
			XapiLanguageMapSQLWriter theWriter = new XapiLanguageMapSQLWriter(conn, theLReader);
			
			XapiLanguageMap theLanguageMap = new XapiLanguageMap();
			theLanguageMap.registerLanguage("en-US", "some MP3");
			theLanguageMap.registerLanguage("en-GB", "some MP3");
			
			theWriter.updateLanguageMap(theLanguageMap, 10381);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
