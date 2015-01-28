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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Properties;

import org.joda.time.DateTime;

import com.claresco.tinman.lrs.XapiStatement;

/**
 * SqlUtility.java
 *
 * Description:
 * 	Utility file to help write and read from and to the databse
 *
 *
 *
 * @author rheza
 * on Feb 20, 2014
 * 
 */

public abstract class SQLUtility {

	protected static PreparedStatement createInsertStatement(Connection conn, String databaseName, 
			String[] fieldNames) throws SQLException{
		String insertString = createInsertString(databaseName, fieldNames);
		
		return conn.prepareStatement(insertString);
	}
	
	
	
	private static String createInsertString(String databaseName, String[] fieldNames){
		String insertString = String.format("insert into %s (", databaseName);
		
		for (int i = 0; i < fieldNames.length; i++) {
			if(i == fieldNames.length - 1){
				insertString = insertString.concat(fieldNames[i]).concat(") ");
			}else{
				insertString = insertString.concat(fieldNames[i]).concat(",");
			}
		}
		
		insertString = insertString.concat("values (");
		
		for (int j = 0; j < fieldNames.length; j++) {
			if(j == fieldNames.length - 1){
				insertString = insertString.concat("?);");
			}else{
				insertString = insertString.concat("?,");
			}
		}
				
		return insertString;
	}
	
	
	
	protected static PreparedStatement createRetrievalStatement(Connection conn, String databaseName,
			String[] fieldNames) throws SQLException{
		String retrieveString = createRetrieveString(databaseName, fieldNames);
		
		return conn.prepareStatement(retrieveString);
	}
	
	
	
	protected static PreparedStatement createRetrievalStatement(Connection conn, String databaseName,
			String fieldName) throws SQLException{
		String retrieveString = createRetrieveString(databaseName, fieldName);
		
		return conn.prepareStatement(retrieveString);
	}
	
	
	
	protected static PreparedStatement createRetrievalStatement(Connection conn, String tableName,
			String[] fieldNames, String[] fieldsToRetrieve) throws SQLException{
		String s = createRetrievalString(tableName, fieldNames, fieldsToRetrieve);
		
		return conn.prepareStatement(s);
	}
	
	
	
	protected static PreparedStatement createRetrievalStatement(Connection conn, String tableName, 
			String[] fieldNames, String[] fieldsToRetrieve, String[] operations) throws SQLException{
		String s = createRetrievalString(tableName, fieldNames, fieldsToRetrieve, operations);
		
		return conn.prepareStatement(s);
	}
	
	
	
	private static String createRetrieveString(String databaseName, String fieldName){
		String retrieveString = String.format("select * from %s where %s = ?", databaseName, fieldName);
		//String retrieveString = "select * from ? where ? = ?";
		return retrieveString;
	}
	
	
	
	private static String createRetrievalString(String tableName, String[] fieldNames, String[] fieldsToRetrieve){
		String[] operation = new String[fieldNames.length];
		
		for(int i = 0; i < fieldNames.length; i++){
			operation[i] = "=";
		}
		
		return createRetrievalString(tableName, fieldNames, fieldsToRetrieve, operation);
	}
	
	
	private static String createRetrievalString(String tableName, String[] fieldNames, String[] fieldsToRetrieve,
			String[] operations){
		String baseString = "select %s from %s where %s";
		
		String selectionString = "";
		for(int i = 0; i < fieldNames.length; i++){
			selectionString += String.format("and %s %s ? ", fieldNames[i], operations[i]);
		}
		
		String fieldString = "";
		for(String fs : fieldsToRetrieve){
			fieldString += String.format(", %s", fs);
		}
		
		String fullString = String.format(baseString, fieldString, tableName, selectionString);
		
		fullString = fullString.replace("select ,", "select");
		fullString = fullString.replace("where and", "where");
		
		fullString = fullString + ";";
		
		return fullString;
	}
	
	
	
	private static String createRetrieveString(String databaseName, String[] fieldNames){
		String retrieveString = createRetrieveString(databaseName, fieldNames[0]);
		for(int i = 1; i < fieldNames.length; i++){
			retrieveString = retrieveString.concat(String.format(" and %s = ?", fieldNames[i]));
		}
		return retrieveString;
	}
	
	
	
	protected static PreparedStatement createUpdateStatement(Connection conn, String databaseName, 
			String[] fieldsToUpdate, String[] selectionFields) throws SQLException{
		String updateString = createUpdateString(databaseName, fieldsToUpdate, selectionFields);
		return conn.prepareStatement(updateString);
	}
	
	
	
	private static String createUpdateString(String databaseName, String[] fieldsToUpdate, String[] selectionFields){
		String baseString = "update %s set %s where %s;";
		
		String setFieldsString = "";
		for (String s : fieldsToUpdate){
			setFieldsString += s + " = ?, ";
		}
		setFieldsString = setFieldsString.substring(0, setFieldsString.length() - 2);
		//System.out.println(setFieldsString);
		
		String selectionString = "";
		for (String s : selectionFields){
			selectionString += s + " = ? and ";
		}
		selectionString = selectionString.substring(0, selectionString.length() - 4);
		//System.out.println(selectionString);
		
		return String.format(baseString, databaseName, setFieldsString, selectionString);
	}
	
	

	protected static void closeStatement(PreparedStatement theStatement) throws SQLException{
		if(theStatement != null){
			theStatement.close();
		}
	}
	
	
	
	protected static void closeResultSet(ResultSet theResultSet) throws SQLException{
		if(theResultSet != null){
			theResultSet.close();
		}
	}
	
	
	
	protected static Connection establishDefaultConnection() throws SQLException{
		String myUser = "username";
		String myPassword = "password";	
		String myUrl = "jdbc:";
		
		Properties props = new Properties();
		props.setProperty("user", myUser);
		props.setProperty("password", myPassword);
		
		return DriverManager.getConnection(myUrl, props);
	}
	

	
	protected static boolean isResultEmpty(ResultSet theResult) throws SQLException{
		if(!theResult.isBeforeFirst()){
			return true;
		}
		return false;
	}
	
	
	
	protected static ResultSet executeRetrievalByIDQuery(PreparedStatement theStatement, int theID)
			throws SQLException{
		theStatement.setInt(1, theID);
		return theStatement.executeQuery();
	}
	
	
	
	protected static ResultSet setStringAndExecute(PreparedStatement theStatement, String theString,
			int theIndex)throws SQLException{
		theStatement.setString(theIndex, theString);
		return theStatement.executeQuery();
	}
	
	
	
	protected static ResultSet setIntAndExecute(PreparedStatement theStatement, int theID, int 
			theIndex)throws SQLException{
		theStatement.setInt(theIndex, theID);
		return theStatement.executeQuery();
	}

	
	
	protected static void printHashMap(HashMap<Integer, XapiStatement> theMap){
		System.out.println("statement id");
		for(Integer i : theMap.keySet()){
			System.out.println(i.toString());
		}
	}
	
	
	
	protected static Timestamp getTimestamp(DateTime theTS){
		return new Timestamp(theTS.getMillis());
	}
	
	
	
	protected static DateTime getDatetime(Timestamp theTS){
		return new DateTime(theTS.getTime());
	}
	
	
	
	public static void main(String[] args) {
		System.out.println(SQLUtility.createRetrieveString("actor", "actorid"));
		
		String[] a = {"accthomepage", "acctname"};
		String[] operation = {">=", "<="};
		
		System.out.println(SQLUtility.createRetrieveString("account", a));
		
		System.out.println(SQLUtility.createInsertString("account", a));
		
		System.out.println(SQLUtility.createRetrievalString("account", a, a, operation));
		
		System.out.println(SQLUtility.createUpdateString("statement", a, a));
	}

}
