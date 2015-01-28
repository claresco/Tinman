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

import com.claresco.tinman.json.JsonUtility;

/**
 * SqlReader.java
 *
 * Read data from database
 *
 *
 *
 * @author rheza
 * on Feb 24, 2014
 * 
 */

abstract class SQLReader {

	protected Connection myConn;
	private PreparedStatement  myRetrieveStatement;
	protected ResultSet myResult;
	
	protected ResultSet retrieveRow(String databaseName, String rowIdName, int rowIdValue) throws SQLException{
		this.myRetrieveStatement = SQLUtility.createRetrievalStatement(this.myConn, "actor", "actorid");
		
		this.myRetrieveStatement.setInt(1, rowIdValue);
		
		ResultSet myResult = this.myRetrieveStatement.executeQuery();
		
		return myResult;
	}
	
	
	/**
	 *	Close connection, statements, results
	 */
	
	protected void close() throws SQLException{
		if(myConn != null){
			myConn.close();
		}
		if(this.myRetrieveStatement != null){
			this.myRetrieveStatement.close();
		}
		if(this.myResult != null){
			this.myResult.close();
		}
	}
	
	
	protected void handleNoData() throws SQLException{
		if(SQLUtility.isResultEmpty(myResult)){
		}
	}
	
	
	protected boolean isResulEmpty() throws SQLException{
		return SQLUtility.isResultEmpty(myResult);
	}
	
	
	
}
