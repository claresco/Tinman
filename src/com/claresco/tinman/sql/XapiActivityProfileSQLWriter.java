/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiActivityProfileSQLWriter.java	Jun 4, 2014
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

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.TimeZone;

import org.joda.time.DateTime;

import com.claresco.tinman.lrs.XapiActivity;
import com.claresco.tinman.lrs.XapiState;

/**
 * XapiActivityProfileSQLWriter
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiActivityProfileSQLWriter extends SQLWriter {
	
	private PreparedStatement myInsertStatement;

	private XapiActivityProfileSQLReader myActivityProfileReader;
	private XapiActivitySQLWriter myActivityWriter;
	private XapiDocumentSQLWriter myDocumentWriter;
	
	private String myTableName = "activityprofile";
	private String[] myFieldNames = new String[]{"activityprofileid", "activityid", "profilekey",
			"documentid", "stored"};
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiActivityProfileSQLWriter(Connection conn, XapiActivitySQLWriter theActivityWriter,
			XapiDocumentSQLWriter theDocumentWriter, XapiActivityProfileSQLReader theActivityProfileReader)
			throws SQLException{
		this.myConn = conn;
		this.myActivityProfileReader = theActivityProfileReader;
		this.myActivityWriter = theActivityWriter;
		this.myDocumentWriter = theDocumentWriter;
		this.myInsertStatement = SQLUtility.createInsertStatement(myConn, myTableName, myFieldNames);
	}
	
	
	
	protected int insertActivityProfile(String theActvID, String theProfileKey, String theDocument)
			throws SQLException, XapiDataIntegrityException, XapiSQLOperationProblemException{
		int theDatabaseID = -1;
		
		ResultSet theResultSet = myActivityProfileReader.getResultSet(theActvID, theProfileKey);

		// if the Result Set not empty
		if(!SQLUtility.isResultEmpty(theResultSet)){
			theResultSet.next();
			theDatabaseID = theResultSet.getInt("activityprofileid");
		}
		
		if(theDatabaseID != -1){
			myDocumentWriter.updateDocument(theDocument, theResultSet.getInt("documentid"));
			
			theResultSet.close();
			return theDatabaseID;
		}else{
			theDatabaseID = insertNewActivityProfile(theActvID, theProfileKey, theDocument);
		}
		
		return theDatabaseID;
	}
	
	
	
	protected int insertNewActivityProfile(String theActvID, String theProfileKey, String theDocument) 
			throws SQLException, XapiDataIntegrityException, XapiSQLOperationProblemException{
		int theID = super.fetchId();
		
		myInsertStatement.setInt(1, theID);
		
		myInsertStatement.setInt(2, myActivityWriter.insertActivity(new XapiActivity(theActvID), true));
		
		myInsertStatement.setString(3, theProfileKey);

		myInsertStatement.setInt(4, myDocumentWriter.insertNewDocument(theDocument));
		
		DateTime theStoredTime = DateTime.now();
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		this.myInsertStatement.setTimestamp(5, SQLUtility.getTimestamp(theStoredTime), cal);
		
		myInsertStatement.executeUpdate();
		
		return theID;
	}
	
	
	
	protected void updateActivityProfile(){
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.sql.SQLWriter#close()
	 */
	@Override
	protected void close() throws SQLException {
		super.close();
		SQLUtility.closeStatement(myInsertStatement);
	}
}
