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
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.claresco.tinman.json.JsonUtility;
import com.claresco.tinman.lrs.XapiAccount;
import com.claresco.tinman.lrs.XapiResult;
import com.claresco.tinman.lrs.XapiScore;
import com.google.gson.Gson;

/**
 * XapiResultSQLWriter.java
 *
 * Write the result part of the statement to the database
 *
 *
 *
 * @author rheza
 * on Mar 10, 2014
 * 
 */

public class XapiResultSQLWriter extends SQLWriter{
	
	private PreparedStatement myInsertStatement;
	private String[] myFieldNames = {"resultid", "rsltrawscore", "rsltminscore", "rsltmaxscore",
			"rsltscaled", "rsltsuccess", "rsltcomplete", "rsltresponse", "rsltduration"};
	private String myTableName = "result";
	
	/**
	 * Description:
	 * 	Constructor
	 * Params:
	 *
	 */
	protected XapiResultSQLWriter(Connection conn) throws SQLException{
		myConn = conn;
		myInsertStatement = SQLUtility.createInsertStatement(super.myConn, myTableName, myFieldNames);
	}
	
	/**
	 * 
	 * Description:
	 *	
	 * Params:
	 *
	 */
	protected int insertNewResult(XapiResult theResult) throws SQLException{
		int theId = super.fetchId();

		// Default values
		myInsertStatement.setInt(1, theId);
		myInsertStatement.setNull(2, Types.NUMERIC);
		myInsertStatement.setNull(3, Types.NUMERIC);
		myInsertStatement.setNull(4, Types.NUMERIC);
		myInsertStatement.setNull(5, Types.NUMERIC);
		myInsertStatement.setNull(6, Types.NUMERIC);
		myInsertStatement.setNull(7, Types.NUMERIC);
		myInsertStatement.setNull(8, Types.CHAR);
		myInsertStatement.setNull(9, Types.NUMERIC);
		
		if(theResult.hasScore()){
			XapiScore theScore = theResult.getScore();
			if(theScore.hasRawScore()){
				myInsertStatement.setDouble(2, theScore.getRawScore());
			}
			if(theScore.hasMinScore()){
				myInsertStatement.setDouble(3, theScore.getMinScore());
			}
			if(theScore.hasMaxScore()){
				myInsertStatement.setDouble(4, theScore.getMaxScore());
			}
			if(theScore.hasScaledScore()){
				myInsertStatement.setDouble(5, theScore.getScaledScore());
			}
		}
		
		if(theResult.hasSuccess()){
			myInsertStatement.setInt(6, theResult.getSuccessAsInt());
		}
		
		if(theResult.hasCompletion()){
			myInsertStatement.setInt(7, theResult.getCompletionAsInt());
		}
		
		if(theResult.hasResponse()){
			myInsertStatement.setString(8, theResult.getResponse());
		}
		
		if(theResult.hasDuration()){
			myInsertStatement.setLong(9, theResult.getDuration().getMillis());
		}
		
		int result = myInsertStatement.executeUpdate();
		
		if(result == 1){
			return theId;
		}
		return -1;
	}
	
	/**
	 * Description:
	 * 	Close everything here
	 * 
	 */
	protected void close() throws SQLException{
		super.close();
		if (myInsertStatement != null){
			myInsertStatement.close();
		}
		
	}
	
	public static void main(String[] args) {
		
		try{
			Connection conn = SQLUtility.establishDefaultConnection();
			Gson gson = JsonUtility.createGson();

			String path = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/Result.json";

			BufferedReader bf = new BufferedReader(new FileReader(path));
			XapiResult r = gson.fromJson(bf, XapiResult.class);

			System.out.println(r);

			String json = gson.toJson(r);
			System.out.println(json);

			XapiResultSQLWriter theWriter = new XapiResultSQLWriter(conn);
			theWriter.insertNewResult(r);
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
