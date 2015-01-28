/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiContextActivitiesSQLWriter.java	Apr 25, 2014
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
import java.sql.Types;
import java.util.ArrayList;

import com.claresco.tinman.json.JsonUtility;
import com.claresco.tinman.lrs.XapiActivity;
import com.claresco.tinman.lrs.XapiContextActivities;
import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.axes.HasPositionalPredChecker;

/**
 * XapiContextActivitiesSQLWriter
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *	YELLOW
 *
 */
public class XapiContextActivitiesSQLWriter extends SQLWriter {

	private XapiActivitySQLWriter myActivityWriter;
	
	private PreparedStatement myInsertStatement;
	
	private String myTableName = "contextactivity";
	private String[] myFieldNames = {"contextactivityid", "contextactivitytypecode", 
			"contextid", "activityid"};
	
	/**
	 * Description:
	 *	Constructor
	 *
	 * Params:
	 *
	 */
	protected XapiContextActivitiesSQLWriter(Connection conn, XapiActivitySQLWriter theActWriter)
			throws SQLException{
		myConn = conn;
		myActivityWriter = theActWriter;
		myInsertStatement = SQLUtility.createInsertStatement(myConn, myTableName, myFieldNames);
	}
	
	
	
	protected void insertNewContextActivities(XapiContextActivities theContextActivities,
			int theContextID, boolean newActivityAllowed) throws SQLException, XapiDataIntegrityException{
		myInsertStatement.setInt(3, theContextID);
		
		if(theContextActivities.hasParent()){
			insertEachActivity("PARENT", theContextActivities.getParent(), newActivityAllowed);
		}
		
		if(theContextActivities.hasGrouping()){
			insertEachActivity("GROUPING", theContextActivities.getGrouping(), newActivityAllowed);
		}
		
		
		
		if(theContextActivities.hasOther()){
			insertEachActivity("OTHER", theContextActivities.getOther(), newActivityAllowed);
		}
		
		
		
		if(theContextActivities.hasCategory()){
			insertEachActivity("CATEGORY", theContextActivities.getCategory(), newActivityAllowed);
		}
		
	}
	
	
	
	private void insertEachActivity(String theType, ArrayList<XapiActivity> theActivityList,
			boolean newActivityAllowed) throws SQLException, XapiDataIntegrityException{
		myInsertStatement.setString(2, theType);
		for(XapiActivity act : theActivityList){
			myInsertStatement.setInt(1, super.fetchId());
			myInsertStatement.setInt(4, myActivityWriter.insertActivity(act, newActivityAllowed));
			myInsertStatement.executeUpdate();
			
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.sql.SQLWriter#close()
	 */
	@Override
	protected void close() throws SQLException {
		// TODO Auto-generated method stub
		super.close();
		SQLUtility.closeStatement(myInsertStatement);
	}
	
	
	
	/**
	public static void main(String[] args) {
		try{
			Connection conn = SQLUtility.establishDefaultConnection();
			XapiLanguageMapSQLReader theLMapReader = new XapiLanguageMapSQLReader(conn);
			XapiExtensionSQLReader theExtReader = new XapiExtensionSQLReader(conn);
			XapiActivitySQLReader theActvReader = new XapiActivitySQLReader(conn, theLMapReader, theExtReader);
			
			XapiContextActivitiesSQLWriter theWriter = new XapiContextActivitiesSQLWriter(conn, theActvReader, null);
			
			Gson gson = JsonUtility.createGson();
			String path = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco" +
					"/tinman/json/ContextActivities.json";
			 
			BufferedReader bf = new BufferedReader(new FileReader(path));
			XapiContextActivities s = gson.fromJson(bf, XapiContextActivities.class);
			
			theWriter.insertNewContextActivities(s, 10498, false);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	**/
}
