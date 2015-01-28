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

/**
 * SqlWriter.java
 *
 * Write data into database
 *
 *
 *
 * @author rheza
 * on Feb 20, 2014
 * 
 */

abstract class SQLWriter {

	protected Connection myConn;
	protected PreparedStatement myFetchIdStatement;
	
	// ***IMPORTANT*** may want to delete it	
	
	protected void close() throws SQLException{
		if(myConn != null){
			myConn.close();
		}
				
		if (myFetchIdStatement != null){
			myFetchIdStatement.close();
		}
	}
	
	
	
	protected int fetchId() throws SQLException{
		
		return fetchId("seqmisc");
	}
	
	
	
	protected int fetchId(String sequenceName) throws SQLException{
		String fetchIDString = "select nextval(?)";		
		
		int theId = -1;
		
		// FIXXXXXXXXXX!!!!
		myFetchIdStatement = this.myConn.prepareStatement(fetchIDString);
		myFetchIdStatement.setString(1, sequenceName);
		
		ResultSet newId = myFetchIdStatement.executeQuery();

		while(newId.next()){
			theId = newId.getInt("nextval");
		}
				
		return theId;
	}
	
	
	
	protected void renewConnection(Connection conn) throws SQLException{
		String fetchIDString = "select nextval(?)";	
		this.myConn = conn;
		this.myFetchIdStatement = this.myConn.prepareStatement(fetchIDString);
	}
}
