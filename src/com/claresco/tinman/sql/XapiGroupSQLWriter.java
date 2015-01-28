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

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.claresco.tinman.json.*;
import com.claresco.tinman.lrs.*;
import com.google.gson.Gson;

/**
 * XapiGroupSqlWriter.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Feb 24, 2014
 * 
 */

class XapiGroupSQLWriter extends SQLWriter {
	
	private PreparedStatement myInsertStatement;
	
	private PreparedStatement myMemberInsertStatement;
	private XapiAccountSQLWriter myAccountWriter;
	private XapiAgentSQLWriter myAgentWriter;
	
	private String[] myFieldNames = {"actorid", "actortypecode", "actname", "actmbox", "atcmbox_sha1",
			"openid", "accountid"};
	private String[] myMemberFieldNames = {"groupid", "agentid"};
	
	/**
	 * Description:
	 *
	 * Params:
	 *
	 */
	public XapiGroupSQLWriter(Connection conn, XapiAccountSQLWriter theAccountWriter, XapiAgentSQLWriter
			theAgentWriter)  throws SQLException{
		this.myConn = conn;
		this.myInsertStatement = SQLUtility.createInsertStatement(super.myConn, "actor", myFieldNames);
		this.myAccountWriter = theAccountWriter;
		this.myMemberInsertStatement = SQLUtility.createInsertStatement(super.myConn, "groupactors", 
				myMemberFieldNames);
		this.myAgentWriter = theAgentWriter;
	}
	
	
	
	protected int insertNewGroup(XapiGroup theGroup) throws SQLException{
		int theId = super.fetchId();
		XapiInverseFunctionalIdentifier invFuncId = theGroup.getInverseFuncId();
		
		myInsertStatement.setInt(1, theId);
		
		myInsertStatement.setString(2, "GRP");
		
		myInsertStatement.setString(3, theGroup.getName());
		
		myInsertStatement.setNull(4, Types.CHAR);
		myInsertStatement.setNull(5, Types.CHAR);
		myInsertStatement.setNull(6, Types.CHAR);
		myInsertStatement.setNull(7, Types.NUMERIC);
		
		if(invFuncId != null){
			if(invFuncId.hasMbox()){
				myInsertStatement.setString(4, theGroup.getInverseFuncId().getMbox().toString());
			}
			
			if(invFuncId.hasMboxSha1Sum()){
				myInsertStatement.setString(5, theGroup.getInverseFuncId().getMboxSha1Sum());
			}
			
			
			if(invFuncId.hasOpenId()){
				myInsertStatement.setString(6, theGroup.getInverseFuncId().getOpenId().toString());
			}
			
			
			if (invFuncId.hasAccount()) {
				myInsertStatement.setInt(7, this.myAccountWriter.insertNewAccount(invFuncId.getAccount()));
			}
		}

		int result = myInsertStatement.executeUpdate();
		
		this.insertMember(theGroup.getMember(), theId);
		
		if(result == 1){
			return theId;
		}
		return -1;

	}
	
	
	
	private void insertMember(XapiAgent[] memberArray, int theGroupId) throws SQLException{
		this.myMemberInsertStatement.setInt(1, theGroupId);
		
		for (XapiAgent agent : memberArray) {
			this.myMemberInsertStatement.setInt(2, this.myAgentWriter.insertNewAgent(agent));
			this.myMemberInsertStatement.executeUpdate();
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.sql.SQLWriter#close()
	 */
	@Override
	protected void close() throws SQLException {
		super.close();
		SQLUtility.closeStatement(myInsertStatement);
		SQLUtility.closeStatement(myMemberInsertStatement);
	}
	
	
	
	public static void main(String[] args) {
		 final Gson gson = JsonUtility.createGson();

		 String path = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/SerializeGroup.json";
		 
		 try{
			 BufferedReader bf = new BufferedReader(new FileReader(path));
			 XapiGroup g = gson.fromJson(bf, XapiGroup.class);
			 
			 Connection conn = SQLUtility.establishDefaultConnection();
			 System.out.println(g);
		 } catch(Exception e){
			 e.printStackTrace();
		 } finally{
		 }
	}
}
