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
import java.sql.SQLException;

import com.claresco.tinman.lrs.XapiAccount;
import com.claresco.tinman.lrs.XapiActor;
import com.claresco.tinman.lrs.XapiAgent;
import com.claresco.tinman.lrs.XapiGroup;
import com.claresco.tinman.lrs.XapiInverseFunctionalIdentifier;

/**
 * XapiActorSqlWriter.java
 *
 *
 * Status:
 * 	Yellow
 *
 * @author rheza
 * on Feb 20, 2014
 * 
 */

class XapiActorSQLWriter extends SQLWriter{

	private XapiAgentSQLWriter myAgentWriter;
	private XapiGroupSQLWriter myGroupWriter;
	private XapiActorSQLReader myActorReader;
	
	/**
	 * Description:
	 *
	 * Params:
	 *
	 */
	public XapiActorSQLWriter(Connection conn, XapiAgentSQLWriter theAgentWriter, XapiGroupSQLWriter theGroupWriter,
			XapiActorSQLReader theActorReader) throws SQLException{
		this.myConn = conn;
		this.myAgentWriter = theAgentWriter;
		this.myGroupWriter = theGroupWriter;
		this.myActorReader = theActorReader;
	}
	
	
	
	/**
	 * 
	 * Definition:
	 *	Function which insert actor into database.
	 *	If the actor already exists in the database, then a new actor data will not be 
	 * 	created
	 *
	 * Params:
	 *
	 *
	 */
	protected int insertNewActor(XapiActor theActor) throws SQLException, XapiDataIntegrityException{
		XapiInverseFunctionalIdentifier theActorID = theActor.getInverseFuncId();
		
		int theDatabaseID = -1;
		
		synchronized (this) {
			// Check if the actor already exists in the database
			switch(theActorID.getIdentifierIndex()){
				case 1: theDatabaseID = myActorReader.retrieveByMbox(theActorID.getMbox().toString());
						break;
				case 2: theDatabaseID = myActorReader.retrieveBySha1Sum(theActorID.getMboxSha1Sum());
						break;
				case 3: theDatabaseID = myActorReader.retrieveByOpenID(theActorID.getOpenId().toString());
						break;
				case 4: theDatabaseID = myActorReader.retrieveIDByAccount(theActorID.getAccount());
						break;
			}
			
			// Enforce: any agent can not share identifier with any group and vice cersa
			if(theDatabaseID != -1){
				if(theActor.isAgent()){
					if(myActorReader.isActorGroup(theDatabaseID)){
						throw new XapiDuplicateActorException("There is already a group with that identifier");
					}
				}else if(theActor.isGroup()){
					if(myActorReader.isActorAgent(theDatabaseID)){
						throw new XapiDuplicateActorException("There is already an agent with that identifier");
					}
				}
				return theDatabaseID;
			}
			
			if(theActor.isAgent()){
				return myAgentWriter.insertNewAgent((XapiAgent) theActor); 
			}else if(theActor.isGroup()){
				return myGroupWriter.insertNewGroup((XapiGroup)theActor);
			}
			
			
			return -1;
		}
	}

	
	
	protected void close() throws SQLException{
		super.close();
		
	}
	
	
	
	
	public static void main(String[] args){
		try{
			Connection conn = SQLUtility.establishDefaultConnection();
			XapiAccount theAccount = new XapiAccount("something", "seomthing");
			XapiAgent theAgent = new XapiAgent("abc", new XapiInverseFunctionalIdentifier
					(null, null, null, theAccount));
			
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		
	}
}
