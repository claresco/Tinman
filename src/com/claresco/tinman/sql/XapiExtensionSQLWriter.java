/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiExtensionSQLWriter.java	Apr 30, 2014
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
import java.util.HashMap;

import com.claresco.tinman.lrs.XapiExtension;

/**
 * XapiExtensionSQLWriter
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *	GREEN
 *
 */
public class XapiExtensionSQLWriter extends SQLWriter {
	
	private PreparedStatement myExtMapInsertStatement;
	private PreparedStatement myExtInsertStatement;
	
	private PreparedStatement myUpdateStatement;
	
	private XapiExtensionSQLReader myExtensionReader;
	
	private String[] myExtFieldNames = {"xextensionid"};
	private String[] myExtMapFieldNames = {"xextensionmapid", "xextensionid", "xekeyiri",
			"xevalue"};
	
	/**
	 * Description:
	 *
	 * Params:
	 *
	 */
	protected XapiExtensionSQLWriter(Connection conn, XapiExtensionSQLReader theExtensionReader)
			throws SQLException{
		this.myConn = conn;
		
		myExtInsertStatement = SQLUtility.createInsertStatement(myConn, "xextension",
				myExtFieldNames);
		myExtMapInsertStatement = SQLUtility.createInsertStatement(myConn, "xextensionmap",
				myExtMapFieldNames);
		
		myUpdateStatement = SQLUtility.createUpdateStatement(myConn, "xextensionmap", new 
				String[]{"xekeyiri", "xevalue"}, new String[]{"xextensionmapid"});
		
		myExtensionReader = theExtensionReader;
	}		
	
	
	
	protected int insertNewExtension(XapiExtension theExtension) throws SQLException{
		int theExtID = super.fetchId();
		myExtInsertStatement.setInt(1, theExtID);
		myExtInsertStatement.executeUpdate();
		
		for(String s : theExtension.getKeys()){
			insertOneExtensionEntry(theExtID, s, theExtension.getValueOf(s));
		}
		
		return theExtID;
	}
	
	
	
	private void insertOneExtensionEntry(int theExtID, String theKey, String theValue)
			throws SQLException{
		myExtMapInsertStatement.setInt(1, super.fetchId());
		myExtMapInsertStatement.setInt(2, theExtID);
		myExtMapInsertStatement.setString(3, theKey);
		myExtMapInsertStatement.setString(4, theValue);
		myExtMapInsertStatement.executeUpdate();
	}
	
	
	
	protected void updateExtension(XapiExtension theExtension, int theExtID)
			throws SQLException{
		ResultSet myRS = myExtensionReader.getResultSet(theExtID);
		
		for(String s: theExtension.getKeys()){
			if(myRS.next()){
				updateOneExtensionEntry(myRS.getInt("xextensionmapid"), s, theExtension.getValueOf(s));
			}else{
				insertOneExtensionEntry(theExtID, s, theExtension.getValueOf(s));
			}
		}
	}
	
	
	
	private void updateOneExtensionEntry(int theID, String theKey, String theValue) 
			throws SQLException{
		myUpdateStatement.setString(1, theKey);
		myUpdateStatement.setString(2, theValue);
		myUpdateStatement.setInt(3, theID);
		myUpdateStatement.executeUpdate();
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.sql.SQLWriter#close()
	 */
	@Override
	protected void close() throws SQLException {
		// TODO Auto-generated method stub
		super.close();
		SQLUtility.closeStatement(myExtInsertStatement);
		SQLUtility.closeStatement(myExtMapInsertStatement);
		SQLUtility.closeStatement(myUpdateStatement);
	}
	
	
	
	public static void main(String[] args) {
		try {
			Connection conn = SQLUtility.establishDefaultConnection();
			XapiExtensionSQLReader theR = new XapiExtensionSQLReader(conn);
			XapiExtensionSQLWriter theW = new XapiExtensionSQLWriter(conn, theR);
			XapiExtension theExt = new XapiExtension();
			theExt.add("http://www.goo.com", "asd234h");
			theExt.add("http://www.yah.com", "2349kljajjr");
			theW.updateExtension(theExt, 10646);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
