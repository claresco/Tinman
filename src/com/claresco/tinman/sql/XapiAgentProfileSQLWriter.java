/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiAgentProfileSQLWriter.java	Jun 25, 2014
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

import org.joda.time.DateTime;

import com.claresco.tinman.lrs.XapiActor;
import com.hp.hpl.jena.sparql.modify.UpdateSink;

/**
 * XapiAgentProfileSQLWriter
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiAgentProfileSQLWriter extends SQLWriter {

	private PreparedStatement myInsertStatement;
	private PreparedStatement myUpdateStatement;

	private XapiActorSQLWriter myActorWriter;
	private XapiDocumentSQLWriter myDocumentWriter;
	private XapiAgentProfileSQLReader myAgentProfileReader;
	
	private String myTableName = "agentprofile";
	private String[] myFieldNames = new String[]{"agentprofileid", "actorid", "profilekey",
			"documentid", "stored"};
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiAgentProfileSQLWriter(Connection conn, XapiActorSQLWriter theActorWriter, 
			XapiDocumentSQLWriter theDocumentWriter, XapiAgentProfileSQLReader theAgentProfileReader)
			throws SQLException{
		this.myConn = conn;
		this.myActorWriter = theActorWriter;
		this.myDocumentWriter = theDocumentWriter;
		this.myAgentProfileReader = theAgentProfileReader;
		
		this.myInsertStatement = SQLUtility.createInsertStatement(myConn, myTableName, myFieldNames);
		this.myUpdateStatement = SQLUtility.createUpdateStatement(myConn, myTableName, new String[]{"stored"}
			, new String[]{"agentprofileid"});
	}
	
	
	
	protected int insertAgentProfile(XapiActor theActor, String theProfileKey, String theDocument) 
			throws SQLException, XapiDataIntegrityException, XapiSQLOperationProblemException{
		int theDatabaseID = -1;
		ResultSet theRS = myAgentProfileReader.getResultSet(theActor, theProfileKey);
		
		if( theRS != null && !SQLUtility.isResultEmpty(theRS)){
			theRS.next();
			theDatabaseID = theRS.getInt("agentprofileid");
		}
		
		if(theDatabaseID != -1){
			int theDocumentID = theRS.getInt("documentid");
			updateAgentProfile(theDocument, theDocumentID, theDatabaseID);
			
		}else{
			theDatabaseID = insertNewAgentProfile(theActor, theProfileKey, theDocument);
		}
		
		SQLUtility.closeResultSet(theRS);
		
		return theDatabaseID;
	}
	
	
	
	private int insertNewAgentProfile(XapiActor theActor, String theProfileKey, String theDocument)
			throws SQLException, XapiDataIntegrityException, XapiSQLOperationProblemException{
		int theID = super.fetchId();
		
		myInsertStatement.setInt(1, theID);
		myInsertStatement.setInt(2, myActorWriter.insertNewActor(theActor));
		myInsertStatement.setString(3, theProfileKey);
		myInsertStatement.setInt(4, myDocumentWriter.insertNewDocument(theDocument));
		
		DateTime theStoredTime = DateTime.now();
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		myInsertStatement.setTimestamp(5, SQLUtility.getTimestamp(theStoredTime), cal);
		
		myInsertStatement.executeUpdate();
		
		return theID;
	}
	
	
	
	private void updateAgentProfile(String theDocument, int theDocumentID, int theAgentProfileID)
			throws SQLException, XapiSQLOperationProblemException{
		myDocumentWriter.updateDocument(theDocument, theDocumentID);
		
		DateTime theStoredTime = DateTime.now();
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		myUpdateStatement.setTimestamp(1, SQLUtility.getTimestamp(theStoredTime), cal);
		
		myUpdateStatement.setInt(2, theAgentProfileID);
		
		myUpdateStatement.executeUpdate();
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.sql.SQLWriter#close()
	 */
	@Override
	protected void close() throws SQLException {
		super.close();
		SQLUtility.closeStatement(myInsertStatement);
		SQLUtility.closeStatement(myUpdateStatement);
	}
}
