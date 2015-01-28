/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiDocumentSQLReader.java	May 8, 2014
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

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.hp.hpl.jena.sdb.core.sqlnode.SqlUnion;

/**
 * XapiDocumentSQLReader
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiDocumentSQLReader extends SQLReader {
	
	private PreparedStatement myRetrievalStatement;
	
	private String myTableName = "document";
	private String[] myFieldNames = new String[]{"documentid"};

	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiDocumentSQLReader(Connection conn) throws SQLException {
		myConn = conn;
		myRetrievalStatement = SQLUtility.createRetrievalStatement(myConn, myTableName
				, myFieldNames);
	}
	
	
	
	protected String retrieveDocument(int theID) throws SQLException, XapiSQLOperationProblemException{
		myRetrievalStatement.setInt(1, theID);
		myResult = myRetrievalStatement.executeQuery();
		
		if(isResulEmpty()){
			return null;
		}
		
		myResult.next();
		InputStream theIS = myResult.getBinaryStream("docdata");
		StringBuffer theStringBuffer = new StringBuffer();
		
		int theLengthRead;
		byte[] fileBytes = new byte[1024];
		
		try{
			while((theLengthRead = theIS.read(fileBytes)) > 0){
				theStringBuffer.append(new String(fileBytes, 0, theLengthRead, "UTF-8"));
			}
			
			return theStringBuffer.toString().trim();
		}catch(IOException e){
			throw new XapiSQLOperationProblemException("Something wrong with converting the " +
					"data to human readable format");
		}
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
		try {
			Connection conn = SQLUtility.establishDefaultConnection();
			XapiDocumentSQLReader theR = new XapiDocumentSQLReader(conn);
			String s = theR.retrieveDocument(14575);
			s = s.trim();
			//System.out.println(s.length());
			System.out.println(s);
			
			/**
			String s2 = theR.retrieveDocument(43541);
			s2 = s2.trim();
			System.out.println(s2);
		
			String d = "Basic ";
			System.out.println(d);
			
			System.out.println(URLDecoder.decode("Basic%20", "UTF-8"));
			
			System.out.println("enf of document");
			**/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
