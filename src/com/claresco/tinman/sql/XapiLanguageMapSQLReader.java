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

import com.claresco.tinman.lrs.XapiLanguageMap;

/**
 * XapiLanguageMapSQLReader.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Feb 27, 2014
 * 
 */

class XapiLanguageMapSQLReader extends SQLReader {

	private String myTableName = "languagemaplanguage";
	
	private PreparedStatement myRetrievalByIDStatement;
	private PreparedStatement myRetrievalByValueStatement;
	
	private String[] myFieldNames = {"lmldisplay"};
	
	/**
	 * Description:
	 *
	 * Params:
	 *
	 */
	public XapiLanguageMapSQLReader(Connection conn) throws SQLException{
		// TODO Auto-generated constructor stub
		this.myConn = conn;
		this.myRetrievalByIDStatement = SQLUtility.createRetrievalStatement(super.myConn, this.myTableName, "languagemapid");
		this.myRetrievalByValueStatement = SQLUtility.createRetrievalStatement(super.myConn, myTableName, myFieldNames);
	}
	
	/**
	 * 
	 * Description:
	 *	Retrieve XapiLanguageMap by its ID in the database
	 *
	 * Params:
	 *
	 */
	protected XapiLanguageMap retrieveByID(int theID) throws SQLException{
		this.myRetrievalByIDStatement.setInt(1, theID);
		
		super.myResult = this.myRetrievalByIDStatement.executeQuery();
				
		// How to make sure there is no duplicate
		
		if(isResulEmpty()){
			return null;
		}
		
		XapiLanguageMap theLanguageMap = new XapiLanguageMap();
		
		while(myResult.next()){
			theLanguageMap.registerLanguage(myResult.getString("languagecode"), 
					myResult.getString("lmldisplay"));
		}
		
		return theLanguageMap;
	}
	
	/**
	 * 
	 * Description:
	 *	Retrieve all the 
	 *
	 * Params:
	 *
	 */
	protected ArrayList<Integer> retrieveByValue(String theDisplay) throws SQLException{
		this.myRetrievalByValueStatement.setString(1, theDisplay);
		
		myResult = this.myRetrievalByValueStatement.executeQuery();
		
		if(!myResult.isBeforeFirst()){
			System.out.println("NoData");
		}
		
		ArrayList<Integer> idArray = new ArrayList<Integer>();
		
		while(myResult.next()){
			idArray.add(myResult.getInt(1));
		}
		
		return idArray;
	}
	
	
	
	protected ResultSet getLanguageMapResultSet(int theLanguageMapID) throws SQLException{
		this.myRetrievalByIDStatement.setInt(1, theLanguageMapID);
		
		return this.myRetrievalByIDStatement.executeQuery();
	}

	
	
	/**
	 * 
	 * Description:
	 * 	Close everything
	 */
	protected void close() throws SQLException{
		super.close();
		if(this.myRetrievalByIDStatement != null){
			this.myRetrievalByIDStatement.close();
		}
		if(this.myRetrievalByValueStatement != null){
			this.myRetrievalByValueStatement.close();
		}
	}
	
	public static void main(String[] args) {
		try{
			Connection conn = SQLUtility.establishDefaultConnection();
			XapiLanguageMapSQLReader theReader = new XapiLanguageMapSQLReader(conn);
			XapiLanguageMap lMap = theReader.retrieveByID(10045);
			System.out.println(lMap);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
