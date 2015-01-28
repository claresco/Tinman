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
import java.sql.SQLException;
import java.sql.Types;

import com.claresco.tinman.lrs.XapiSubStatement;

/**
 * XapiSubStatementSqlWriter.java
 *
 * To Write a substatement to the database
 *
 *
 *
 * @author rheza
 * on Feb 25, 2014
 * 
 */

class XapiSubStatementSQLWriter extends SQLWriter{
	
	private PreparedStatement myInsertStatement;
	private XapiActorSQLWriter myActorWriter;
	private XapiVerbSQLWriter myVerbWriter;
	private XapiObjectSQLWriter myObjectWriter;
	
	private String[] myFieldNames = {"statementid", "actorid", "verbid", "objectid",
			"resultid", "contextid", "sttime", "ststored" , "authorityid", "stversion",
			"statementuuid"};
	private String myTableName = "statement";
	
	
	
	/**
	 * Description:
	 *	Constructor
	 * Params:
	 *
	 */
	
	public XapiSubStatementSQLWriter(Connection conn, XapiActorSQLWriter theActorWriter, XapiVerbSQLWriter theVerbWriter, XapiObjectSQLWriter theObjectWriter) throws SQLException{
		// TODO Auto-generated constructor stub
		this.myConn = conn;
		this.myInsertStatement = SQLUtility.createInsertStatement(myConn, myTableName, myFieldNames);
		this.myActorWriter = theActorWriter;
		this.myVerbWriter = theVerbWriter;
		this.myObjectWriter = theObjectWriter;
	}

	
	
	/**
	 * 
	 * Description:
	 *	Insert a new substatement to the database
	 * Params:
	 *	theSubstatement : the SubStatement object
	 *
	 */
	protected int insertNewSubStatement(XapiSubStatement theSubStatement, boolean newActivityAllowed)
			throws SQLException, XapiDataIntegrityException{
		int theId = super.fetchId();
		
		this.myInsertStatement.setInt(1, theId);
		this.myInsertStatement.setInt(2, this.myActorWriter.insertNewActor(theSubStatement.getActor()));
		this.myInsertStatement.setInt(3, this.myVerbWriter.insertNewVerb(theSubStatement.getVerb()));
		this.myInsertStatement.setInt(4, this.myObjectWriter.insertNewObject(theSubStatement.getObject(),
				newActivityAllowed));
		this.myInsertStatement.setNull(5, Types.NUMERIC);
		this.myInsertStatement.setNull(6, Types.NUMERIC);
		this.myInsertStatement.setNull(7, Types.TIMESTAMP);
		this.myInsertStatement.setNull(8, Types.TIMESTAMP);
		this.myInsertStatement.setNull(9, Types.NUMERIC);
		this.myInsertStatement.setNull(10, Types.CHAR);
		this.myInsertStatement.setNull(11, Types.CHAR);
		
		this.myInsertStatement.executeUpdate();
		
		return theId;
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
}
