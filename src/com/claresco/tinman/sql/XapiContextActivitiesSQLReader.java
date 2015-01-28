/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiContextActivitiesSQLReader.java	May 1, 2014
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.claresco.tinman.json.JsonUtility;
import com.claresco.tinman.lrs.XapiActivity;
import com.claresco.tinman.lrs.XapiContextActivities;
import com.google.gson.Gson;

/**
 * XapiContextActivitiesSQLReader
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiContextActivitiesSQLReader extends SQLReader {
	
	private String myTableName = "contextactivity";
	private PreparedStatement myRetrievalStatement;
	private XapiActivitySQLReader myActivityReader;
	
	
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiContextActivitiesSQLReader(Connection conn, XapiActivitySQLReader 
			theActivityReader) throws SQLException {
		super.myConn = conn;
		myRetrievalStatement = SQLUtility.createRetrievalStatement(myConn, myTableName,
				new String[]{"contextid"});
		myActivityReader = theActivityReader;
	}
	
	
	
	protected XapiContextActivities retrieveContextActivities(int theContextID) 
			throws SQLException{
		myRetrievalStatement.setInt(1, theContextID);
		
		myResult = myRetrievalStatement.executeQuery();
		
		if(isResulEmpty()){
			return null;
		}
		
		ArrayList<XapiActivity> theParent = new ArrayList<XapiActivity>();
		ArrayList<XapiActivity> theGrouping = new ArrayList<XapiActivity>();
		ArrayList<XapiActivity> theCategory = new ArrayList<XapiActivity>();
		ArrayList<XapiActivity> theOther = new ArrayList<XapiActivity>();
		
		while(myResult.next()){
			XapiActivity theActivity = myActivityReader.retrieveByID(myResult.
					getInt("activityid"));
			String theType = myResult.getString("contextactivitytypecode");
			if(theType.equals("PARENT")){
				theParent.add(theActivity);
			}
			else if(theType.equals("GROUPING")){
				theGrouping.add(theActivity);
			}
			else if(theType.equals("CATEGORY")){
				theCategory.add(theActivity);
			}
			else if(theType.equals("OTHER")){
				theOther.add(theActivity);
			}
		}
		
		if(theParent.isEmpty()){
			theParent = null;
		}
		if(theGrouping.isEmpty()){
			theGrouping = null;
		}
		if(theCategory.isEmpty()){
			theCategory = null;
		}
		if(theOther.isEmpty()){
			theOther = null;
		}
		
		return new XapiContextActivities(theParent, theGrouping, theCategory, theOther);
	}
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.sql.SQLReader#close()
	 */
	@Override
	protected void close() throws SQLException {
		// TODO Auto-generated method stub
		super.close();
		SQLUtility.closeStatement(myRetrievalStatement);
	}
	
	
	
	public static void main(String[] args) {
		try{
			Connection conn = SQLUtility.establishDefaultConnection();
			XapiLanguageMapSQLReader theLMapReader = new XapiLanguageMapSQLReader(conn);
			XapiExtensionSQLReader theExtReader = new XapiExtensionSQLReader(conn);
			XapiActivitySQLReader theActvReader = new XapiActivitySQLReader(conn, theLMapReader, theExtReader);
			XapiContextActivitiesSQLReader theReader = new XapiContextActivitiesSQLReader(conn, theActvReader);
			
			XapiContextActivities theCtxtActv = theReader.retrieveContextActivities(10498);
			System.out.println(theCtxtActv);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
