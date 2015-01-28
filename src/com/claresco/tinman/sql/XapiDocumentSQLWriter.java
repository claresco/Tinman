/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiDocumentSQLWriter.java	May 8, 2014
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * XapiDocumentSQLWriter
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiDocumentSQLWriter extends SQLWriter {
	
	private PreparedStatement myInsertStatement;
	private PreparedStatement myUpdateStatement;
	
	private String myTableName = "document";
	private String[] myFieldNames = new String[]{"documentid", "docdata"};
	
	
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiDocumentSQLWriter(Connection conn) throws SQLException {
		myConn = conn;
		myInsertStatement = SQLUtility.createInsertStatement(myConn, myTableName, 
				myFieldNames);
		myUpdateStatement = SQLUtility.createUpdateStatement(myConn, myTableName, new String[]{"docdata"},
				new String[]{"documentid"});
	}
	
	
	
	protected int insertNewDocument(String theDocument) throws SQLException, XapiSQLOperationProblemException{
		int theID = super.fetchId();
		
		myInsertStatement.setInt(1, theID);
		
		try{
			byte[] theBytesDocument = theDocument.getBytes("UTF-8");

			InputStream is = new ByteArrayInputStream(theBytesDocument);
			myInsertStatement.setBinaryStream(2, is, theBytesDocument.length);

			myInsertStatement.executeUpdate();

			return theID;
		}catch(UnsupportedEncodingException e){
			throw new XapiSQLOperationProblemException("Can't process the document");
		}

	}
	
	
	
	protected void updateDocument(String theDocument, int theID) throws SQLException, 
			XapiSQLOperationProblemException{
		try{
			byte[] theBytesDocument = theDocument.getBytes("UTF-8");
			
			InputStream is = new ByteArrayInputStream(theBytesDocument);
			myUpdateStatement.setBinaryStream(1, is, theBytesDocument.length);
			
			myUpdateStatement.setInt(2, theID);
			
			myUpdateStatement.executeUpdate();
		}catch(UnsupportedEncodingException e){
			throw new XapiSQLOperationProblemException("Having problem to convert your" +
					" document to machine readable format");
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
		SQLUtility.closeStatement(myUpdateStatement);
	}
	
	
	
	public static void main(String[] args) {
		try{
			Connection conn = SQLUtility.establishDefaultConnection();
			XapiDocumentSQLWriter theW = new XapiDocumentSQLWriter(conn);
			String s = "1";
			theW.updateDocument(s, 10961);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
