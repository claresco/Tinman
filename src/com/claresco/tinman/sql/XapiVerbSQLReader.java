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
import java.util.ArrayList;

import com.claresco.tinman.lrs.XapiVerb;
import com.claresco.tinman.lrs.XapiLanguageMap;

/**
 * XapiVerbSQLReader.java
 *
 * This class reads verb from the database
 *
 *
 *
 * @author rheza
 * on Feb 27, 2014
 * 
 */

class XapiVerbSQLReader extends SQLReader {

	private String myDatabaseName = "verb";
	private PreparedStatement myRetrievalByIDStatement;
	private XapiLanguageMapSQLReader myLanguageMapReader;
	private PreparedStatement myRetrievalByValueStatement;
	private String[] myFieldNames = {"verbiri"};
	
	/**
	 * Description:
	 *
	 * Params:
	 *
	 */
	protected XapiVerbSQLReader(Connection conn, XapiLanguageMapSQLReader theLanguageMapReader) throws SQLException{
		// TODO Auto-generated constructor stub
		this.myConn = conn;
		this.myRetrievalByIDStatement = SQLUtility.createRetrievalStatement(myConn, myDatabaseName, "verbid");
		this.myRetrievalByValueStatement = SQLUtility.createRetrievalStatement(myConn, myDatabaseName, myFieldNames);
		this.myLanguageMapReader = theLanguageMapReader;
	}
	
	
	
	/**
	 * 
	 * Description:
	 *	Retrieve a verb by its ID
	 *
	 * Params:
	 *
	 */
	protected XapiVerb retrieveByID(int theID) throws SQLException{
		myRetrievalByIDStatement.setInt(1, theID);
		
		myResult = myRetrievalByIDStatement.executeQuery();
		
		if(!myResult.isBeforeFirst()){
			return null;
		}
		
		myResult.next();
		
		String theIRI = myResult.getString("verbiri");
		int theLanguageMapID = myResult.getInt("languagemapid");
		
		XapiLanguageMap theLanguageMap = myLanguageMapReader.retrieveByID(theLanguageMapID);
		
		return new XapiVerb(theIRI, theLanguageMap);
	}

	
	
	/**
	 * 
	 * Description:
	 *	Find the ID based on the IRI
	 *	Returns all the instances' id that uses that IRI
	 *
	 * Params:
	 *
	 */
	protected int retrieveIDByValue(String theIRI) throws SQLException{
		myResult = setIRIAndExecute(myRetrievalByValueStatement, theIRI);
		
		if(SQLUtility.isResultEmpty(myResult)){
			return -1;
		}
		
		myResult.next();
		return myResult.getInt(1);
	}
	
	
	
	protected ArrayList<XapiVerb> retrieveVerbByValue(String theIRI) throws SQLException{
		myResult = setIRIAndExecute(myRetrievalByValueStatement, theIRI);
		
		handleNoData();
		
		ArrayList<XapiVerb> verbArray = new ArrayList<XapiVerb>();
		while(myResult.next()){
			
			verbArray.add(getOneXapiVerbFromResult());
		}
		return verbArray;
	}
	
	
	
	private XapiVerb getOneXapiVerbFromResult() throws SQLException{
		String theIRI = myResult.getString(2);
		int theLanguageMapID = myResult.getInt(3);
		
		XapiLanguageMap theLanguageMap = myLanguageMapReader.retrieveByID(theLanguageMapID);
		
		return new XapiVerb(theIRI, theLanguageMap);
	}
	
	
	
	private ResultSet setIRIAndExecute(PreparedStatement theStatement, String theIRI) throws SQLException{
		return SQLUtility.setStringAndExecute(theStatement, theIRI, 1);
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.sql.SQLReader#close()
	 */
	@Override
	protected void close() throws SQLException {
		// TODO Auto-generated method stub
		super.close();
		SQLUtility.closeStatement(myRetrievalByIDStatement);
		SQLUtility.closeStatement(myRetrievalByValueStatement);
	}
	
	
	
	protected static void main(String[] args) {
		try {
			Connection conn = SQLUtility.establishDefaultConnection();
			XapiLanguageMapSQLReader theLMapReader = new XapiLanguageMapSQLReader(conn);
			XapiVerbSQLReader theReader = new XapiVerbSQLReader(conn, theLMapReader);
			XapiVerb v = theReader.retrieveByID(10044);
			System.out.println(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
