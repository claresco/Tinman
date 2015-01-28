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

import com.claresco.tinman.lrs.XapiObject;
import com.claresco.tinman.lrs.XapiStatementRef;

/**
 * XapiObjectSQLReader.java
 *
 * Read XapiObject related data from the database
 *
 *
 *
 * @author rheza
 * on Feb 27, 2014
 * 
 */

public class XapiObjectSQLReader extends SQLReader {

	private String myTableName = "object";
	
	private PreparedStatement myRetrievalByIDStatement;
	
	private XapiActivitySQLReader myActivityReader;
	private XapiActorSQLReader myActorReader;
	private XapiSubStatementSQLReader mySubStatementReader;
	
	/**
	 * Description:
	 *
	 * Params:
	 * @throws SQLException 
	 *
	 */
	public XapiObjectSQLReader(Connection conn, XapiActivitySQLReader theActivityReader, XapiActorSQLReader
			theActorReader, XapiSubStatementSQLReader theSubStatementReader) throws SQLException {
		this.myConn = conn;
		this.myRetrievalByIDStatement = SQLUtility.createRetrievalStatement(myConn, myTableName, "objectid");
		this.myActivityReader = theActivityReader;
		this.myActorReader = theActorReader;
		this.mySubStatementReader = theSubStatementReader;
	}
	
	/**
	 * 
	 * Description:
	 *	Retrieve XapiObject based on its ID	
	 *
	 * Params:
	 *
	 */
	protected XapiObject retrieveByID(int theID) throws SQLException{
		this.myRetrievalByIDStatement.setInt(1, theID);
		
		ResultSet myResult = this.myRetrievalByIDStatement.executeQuery();
		XapiObject theObject = null;
		
		myResult.next();
		String objectType = myResult.getString(2);

		if(objectType.equals("ACT")){
			theObject = this.myActivityReader.retrieveByID(myResult.getInt(4));
		}else if(objectType.equals("AGT")){
			theObject = (XapiObject) this.myActorReader.retrieveByID(myResult.getInt(5));
		}else if(objectType.equals("GRP")){
			theObject = (XapiObject) this.myActorReader.retrieveByID(myResult.getInt(5));
		}else if(objectType.equals("STMT")){
			theObject = this.mySubStatementReader.retrieveByID(myResult.getInt(3));
		}else if(objectType.equals("SREF")){
			theObject = new XapiStatementRef(this.mySubStatementReader.retrieveUUID(myResult.getInt(3)));
		}
		
		return theObject;
	}
	
	/**
	 * 
	 * Description:
	 * 	Close everything
	 */
	protected void close() throws SQLException{
		super.close();
		if(this.myRetrievalByIDStatement != null){
			this.myRetrievalByIDStatement.close();
		}
	}
	
}
