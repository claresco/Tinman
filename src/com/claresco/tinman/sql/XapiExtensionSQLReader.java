/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiExtensionSQLReader.java	Apr 30, 2014
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

import com.claresco.tinman.lrs.XapiExtension;

/**
 * XapiExtensionSQLReader
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *	YELLO
 *
 */
public class XapiExtensionSQLReader extends SQLReader {
	
	private String myTableName = "xextensionmap";
	private PreparedStatement myExtMapRetrievalStatement;
	private String[] myExtFieldNames = {"xextensionid"};

	
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiExtensionSQLReader(Connection conn) throws SQLException{
		this.myConn = conn;
		this.myExtMapRetrievalStatement = SQLUtility.createRetrievalStatement
				(myConn, myTableName, myExtFieldNames);
	}
	
	
	
	protected XapiExtension retrieveByID(int theID) throws SQLException{
		XapiExtension theExtension = new XapiExtension();
		
		myExtMapRetrievalStatement.setInt(1, theID);
		
		myResult = myExtMapRetrievalStatement.executeQuery();

		if(isResulEmpty()){
			return null;
		}
		
		while(myResult.next()){
			String key = myResult.getString("xekeyiri");
			String value = myResult.getString("xevalue");
			theExtension.add(key, value);
		}
		
		return theExtension;
	}
	
	
	
	protected ResultSet getResultSet(int theID) throws SQLException{
		myExtMapRetrievalStatement.setInt(1, theID);
		
		return myExtMapRetrievalStatement.executeQuery();
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.sql.SQLReader#close()
	 */
	@Override
	protected void close() throws SQLException {
		super.close();
		SQLUtility.closeStatement(myExtMapRetrievalStatement);
	}
	
}
