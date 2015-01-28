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

import org.joda.time.Duration;
import org.joda.time.Period;

import com.claresco.tinman.lrs.XapiAccount;
import com.claresco.tinman.lrs.XapiResult;
import com.claresco.tinman.lrs.XapiScore;

/**
 * XapiResultSQLReader.java
 *
 *
 *
 * STATUS:
 * 	RED
 *
 * @author rheza
 * on Mar 10, 2014
 * 
 */

public class XapiResultSQLReader extends SQLReader{
	
	private String myTableName = "result";
	private PreparedStatement myIDRetrievalStatement;
	
	/**
	 * Description:
	 *
	 * Params:
	 *
	 */	
	public XapiResultSQLReader(Connection conn) throws SQLException{
		this.myConn = conn;
		this.myIDRetrievalStatement = SQLUtility.createRetrievalStatement(super.myConn, this.myTableName, "resultid");
	}
	
	protected XapiResult retrieveByID(int theID) throws SQLException{
		this.myIDRetrievalStatement.setInt(1, theID);
		
		myResult = this.myIDRetrievalStatement.executeQuery();
				
		if(isResulEmpty()){
			return null;
		}
		
		Double theScaled;
		Double theRaw;
		Double theMin;
		Double theMax;
		
		myResult.next();
		
		// Prim short for primitive type
		double thePrimScaled = myResult.getDouble("rsltscaled");
		if(myResult.wasNull()){
			theScaled = null;
		}else {
			theScaled = new Double(thePrimScaled);
		}
		
		double thePrimRaw = myResult.getDouble("rsltrawscore");
		if(myResult.wasNull()){
			theRaw = null;
		}else {
			theRaw = new Double(thePrimRaw);
		}
		
		double thePrimMin = myResult.getDouble("rsltminscore");
		if(myResult.wasNull()){
			theMin = null;
		}else {
			theMin = new Double(thePrimMin);
		}
		
		double thePrimMax = myResult.getDouble("rsltmaxscore");
		if(myResult.wasNull()){
			theMax = null;
		}else {
			theMax = new Double(thePrimMax);
		}
		
		XapiScore theScore = new XapiScore(theScaled, theRaw, theMin, theMax);
		if(theScore.isEmpty()){
			theScore = null;
		}
		
		String theResponse = myResult.getString("rsltresponse");
		
		long msDuration = myResult.getLong("rsltduration");
		Duration theDuration = new Duration(msDuration);
		
		Boolean theSuccess;
		Boolean theComplete;
		boolean thePrimSuccess = myResult.getBoolean("rsltsuccess");
		if(myResult.wasNull()){
			theSuccess = null;
		}else{
			theSuccess = new Boolean(thePrimSuccess);
		}
		
		boolean thePrimComplete = myResult.getBoolean("rsltcomplete");
		if(myResult.wasNull()){
			theComplete = null;
		}else{
			theComplete = new Boolean(thePrimComplete);
		}
		
		return new XapiResult(theScore, theSuccess, theComplete, theResponse, theDuration);
	}

	
	
	protected void close() throws SQLException{
		super.close();
		SQLUtility.closeStatement(myIDRetrievalStatement);
	}
	
	
	
	public static void main(String[] args) {
		try {
			Connection conn = SQLUtility.establishDefaultConnection();
			
			XapiResultSQLReader theReader = new XapiResultSQLReader(conn);
			XapiResult theResult = theReader.retrieveByID(10334);
			System.out.println(theResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
