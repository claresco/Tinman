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

import com.claresco.tinman.json.XapiAccountJson;
import com.claresco.tinman.json.XapiActivityDefinitionJson;
import com.claresco.tinman.json.XapiActivityJson;
import com.claresco.tinman.json.XapiActorJson;
import com.claresco.tinman.json.XapiAgentJson;
import com.claresco.tinman.json.XapiGroupJson;
import com.claresco.tinman.json.XapiInverseFuncIdJson;
import com.claresco.tinman.json.XapiObjectJson;
import com.claresco.tinman.json.XapiStatementJson;
import com.claresco.tinman.json.XapiStatementRefJson;
import com.claresco.tinman.json.XapiSubStatementJson;
import com.claresco.tinman.json.XapiVerbJson;
import com.claresco.tinman.lrs.XapiAccount;
import com.claresco.tinman.lrs.XapiActivityDefinition;
import com.claresco.tinman.lrs.XapiAgent;
import com.claresco.tinman.lrs.XapiGroup;
import com.claresco.tinman.lrs.XapiInverseFunctionalIdentifier;
import com.claresco.tinman.lrs.XapiObject;
import com.claresco.tinman.lrs.XapiActor;
import com.claresco.tinman.lrs.XapiActivity;
import com.claresco.tinman.lrs.XapiStatement;
import com.claresco.tinman.lrs.XapiStatementRef;
import com.claresco.tinman.lrs.XapiSubStatement;
import com.claresco.tinman.lrs.XapiVerb;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * XapiObjectSqlWriter.java
 *
 *
 * Status:
 * 	YELLOW
 *
 *
 * @author rheza
 * on Feb 24, 2014
 * 
 */

class XapiObjectSQLWriter extends SQLWriter {

	private PreparedStatement myInsertStatement;
	
	private XapiActorSQLWriter myActorWriter;
	private XapiSubStatementSQLWriter mySubStatementWriter;
	private XapiStatementSQLReader myStatementReader;
	private XapiActivitySQLWriter myActivityWriter;
	
	private String[] myFieldNames = {"objectid", "objecttypecode", "statementid", "activityid", "actorid"};
	
	/**
	 * 
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiObjectSQLWriter(Connection conn, XapiActorSQLWriter theActorWriter, XapiActivitySQLReader theActivityReader,
			XapiSubStatementSQLWriter theSubStatementWriter, XapiStatementSQLReader theStatementReader, 
			XapiActivitySQLWriter theActivityWriter) throws SQLException{
		myConn = conn;
		myInsertStatement = SQLUtility.createInsertStatement(super.myConn, "object", myFieldNames);
		myActorWriter = theActorWriter;
		mySubStatementWriter = theSubStatementWriter;
		myStatementReader = theStatementReader;
		myActivityWriter = theActivityWriter;
	}
	
	
	
	protected int insertNewObject(XapiObject theObject, boolean newActivityAllowed) throws SQLException,
			XapiDataIntegrityException{
		int theId = super.fetchId();
		
		this.myInsertStatement.setInt(1, theId);
		this.myInsertStatement.setNull(3, Types.NUMERIC);
		this.myInsertStatement.setNull(4, Types.NUMERIC);
		this.myInsertStatement.setNull(5, Types.NUMERIC);
		
		// Calling appropriate writers according to the object type of the activity
		if(theObject.getObjectType().equals("Agent")){
			this.myInsertStatement.setString(2, "AGT");
			this.myInsertStatement.setInt(5, myActorWriter.insertNewActor((XapiActor) theObject));
		} else if(theObject.getObjectType().equals("Group")){
			this.myInsertStatement.setString(2, "GRP");
		 	this.myInsertStatement.setInt(5, myActorWriter.insertNewActor((XapiActor) theObject));
		} else if(theObject.getObjectType().equals("Activity")){
			this.myInsertStatement.setString(2, "ACT");
			this.myInsertStatement.setInt(4, myActivityWriter.insertActivity((XapiActivity) theObject,
					newActivityAllowed));
		} else if(theObject.getObjectType().equals("SubStatement")){
			this.myInsertStatement.setString(2, "STMT");
			this.myInsertStatement.setInt(3, this.mySubStatementWriter.insertNewSubStatement
					((XapiSubStatement) theObject, newActivityAllowed));
		} else if(theObject.getObjectType().equals("StatementRef")){
			this.myInsertStatement.setString(2, "SREF");
			// How to check if the statement exists? Call a reader
			int srefID = myStatementReader.findIDByUUID(((XapiStatementRef) theObject).getId().toString());
			
			// Statement it references does not exists
			if(srefID == -1){
				throw new XapiStatementReferenceInvalidException("the statement it refers " +
						"to does not exists in the databse");
			}
			this.myInsertStatement.setInt(3, srefID);
		}	
		
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
	
	
	
	public static void main(String[] args) {
		/**
		 String path1 = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/statement8.json";
//		 String path2 = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/GroupAsObjectDatabaseTesting.json";
		 
		 try{
			 Connection conn = SQLUtility.establishDefaultConnection();
			 
			 XapiAccountSQLWriter myAccountWriter = new XapiAccountSQLWriter(conn);
			 XapiLanguageMapSQLWriter myLanguageMapWriter = new XapiLanguageMapSQLWriter(conn);
			 XapiActivitySQLWriter myActivityWriter = new XapiActivitySQLWriter(conn, myLanguageMapWriter);
			 XapiAgentSQLWriter myAgentWriter = new XapiAgentSQLWriter(conn, myAccountWriter);
			 XapiGroupSQLWriter myGroupWriter = new XapiGroupSQLWriter(conn, myAccountWriter, myAgentWriter);
			 XapiActorSQLWriter myActorWriter = new XapiActorSQLWriter(conn, myAgentWriter, myGroupWriter);
			 XapiVerbSQLWriter myVerbWriter = new XapiVerbSQLWriter(conn, myLanguageMapWriter);
			 XapiObjectSQLWriter mySubstatementObjectWriter = new XapiObjectSQLWriter(conn, myActorWriter, myActivityWriter, null);
			 XapiSubStatementSQLWriter mySubStamentWriter = new XapiSubStatementSQLWriter(conn, myActorWriter, myVerbWriter, mySubstatementObjectWriter);
			 XapiObjectSQLWriter myObjectWriter = new XapiObjectSQLWriter(conn, myActorWriter, myActivityWriter, mySubStamentWriter);
			 XapiStatementSQLWriter myStatementWriter = new XapiStatementSQLWriter(conn, myActorWriter, myVerbWriter, myObjectWriter);
			 
			 //System.out.println(s.getObject());
			 
			 //theWriter.insertNewObject(s.getObject());
			 
			 BufferedReader bf = new BufferedReader(new FileReader(path1));
			 XapiStatement s = gson.fromJson(bf, XapiStatement.class);
			 
			 System.out.println(s.getObject());
			 myStatementWriter.insertNewStatement(s);
			 	
		 } catch(Exception e){
			 e.printStackTrace();
		 } finally{
	
		 }
		**/
	}
	
}
