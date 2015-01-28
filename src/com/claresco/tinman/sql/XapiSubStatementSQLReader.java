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

import com.claresco.tinman.lrs.XapiSubStatement;
import com.claresco.tinman.lrs.XapiVerb;
import com.claresco.tinman.lrs.XapiActor;
import com.claresco.tinman.lrs.XapiObject;

/**
 * XapiSubStatementSQLReader.java
 *
 * Read substatement from the database
 *
 *
 *
 * @author rheza
 * on Feb 27, 2014
 * 
 */

public class XapiSubStatementSQLReader extends SQLReader {

	private String myTableName = "statement";
	
	private PreparedStatement myRetrievalByIDStatement;

	private XapiActorSQLReader myActorReader;
	private XapiVerbSQLReader myVerbReader;
	private XapiObjectSQLReader myObjectReader;

	/**
	 * 
	 * Description:
	 *	Constructor
	 *
	 * Params:
	 *
	 */
	public XapiSubStatementSQLReader(Connection conn, XapiActorSQLReader theActorReader, XapiVerbSQLReader theVerbReader, XapiObjectSQLReader theObjectReader) throws SQLException{
		this.myConn = conn;
		this.myRetrievalByIDStatement = SQLUtility.createRetrievalStatement(myConn, myTableName, "statementid");
		this.myActorReader = theActorReader;
		this.myVerbReader = theVerbReader;
		this.myObjectReader = theObjectReader;
	}
	
	
	
	protected XapiSubStatement retrieveByID(int theID) throws SQLException{
		this.myRetrievalByIDStatement.setInt(1, theID);
		
		ResultSet myResult = this.myRetrievalByIDStatement.executeQuery();
		
		XapiActor theActor;
		XapiVerb theVerb;
		XapiObject theObject;
		
		myResult.next();
		theActor = this.myActorReader.retrieveByID(myResult.getInt(2));
		theVerb = this.myVerbReader.retrieveByID(myResult.getInt(3));
		theObject = this.myObjectReader.retrieveByID(myResult.getInt(4));
		
		System.out.println(theObject);

		return new XapiSubStatement(theActor, theVerb, theObject);
	}
	
	
	
	/**
	 * 
	 * Description:
	 *	Retrieve sub statement based on its UUID	
	 *
	 * Params:
	 *
	 */
	protected String retrieveUUID(int theID) throws SQLException{
		this.myRetrievalByIDStatement.setInt(1, theID);
		
		ResultSet myResult = this.myRetrievalByIDStatement.executeQuery();
		
		myResult.next();

		String theUUID = myResult.getString("statementuuid");
		
		return theUUID;
	}
	
	
	
	protected void close() throws SQLException{
		super.close();
		SQLUtility.closeStatement(myRetrievalByIDStatement);
	}
	
	
	public static void main(String[] args) {

	}
	
}
