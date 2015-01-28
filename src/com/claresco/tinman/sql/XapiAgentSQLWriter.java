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

import com.claresco.tinman.lrs.XapiAccount;
import com.claresco.tinman.lrs.XapiInverseFunctionalIdentifier;
import com.claresco.tinman.lrs.XapiAgent;

/**
 * XapiAgentSqlWriter.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Feb 24, 2014
 * 
 */

class XapiAgentSQLWriter extends SQLWriter {

	private PreparedStatement myInsertStatement;
	
	private XapiAccountSQLWriter myAccountWriter;
	
	private String[] myFieldNames = {"actorid", "actortypecode", "actname", "actmbox", "atcmbox_sha1",
			"openid", "accountid"};
	
	/**
	 * Description:
	 *
	 * Params:
	 *
	 */	
	public XapiAgentSQLWriter(Connection conn, XapiAccountSQLWriter theAccountWriter) throws SQLException{
		myConn = conn;
		myInsertStatement = SQLUtility.createInsertStatement(super.myConn, "actor", myFieldNames);
		myAccountWriter = theAccountWriter;
	}

	
	
	protected int insertNewAgent(XapiAgent theAgent) throws SQLException{
		
		int theId = super.fetchId();
		XapiInverseFunctionalIdentifier invFuncId = theAgent.getInverseFuncId();
		
		myInsertStatement.setInt(1, theId);
		
		myInsertStatement.setString(2, "AGT");
		
		myInsertStatement.setString(3, theAgent.getName());
		
		myInsertStatement.setNull(4, Types.CHAR);
		myInsertStatement.setNull(5, Types.CHAR);
		myInsertStatement.setNull(6, Types.CHAR);
		myInsertStatement.setNull(7, Types.NUMERIC);

		
		if(invFuncId.hasMbox()){
			myInsertStatement.setString(4, theAgent.getInverseFuncId().getMbox().toString());
		}
		
		if(invFuncId.hasMboxSha1Sum()){
			myInsertStatement.setString(5, theAgent.getInverseFuncId().getMboxSha1Sum());
		}
		
		if(invFuncId.hasOpenId()){
			myInsertStatement.setString(6, theAgent.getInverseFuncId().getOpenId().toString());
		}
				
		if (invFuncId.hasAccount()) {
			myInsertStatement.setInt(7, this.myAccountWriter.insertNewAccount(invFuncId.getAccount()));
		}

		int result = myInsertStatement.executeUpdate();
		
		if(result == 1){
			return theId;
		}
		return -1;
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

	
	public static void main(String[] args){
		try{
			Connection conn = SQLUtility.establishDefaultConnection();
			XapiAccount theAccount = new XapiAccount("something", "seomthing");
			XapiAgent theAgent = new XapiAgent("abc", new XapiInverseFunctionalIdentifier(null, null, null, theAccount));
		} catch(SQLException e){
			e.printStackTrace();
		}
		
	}
}
