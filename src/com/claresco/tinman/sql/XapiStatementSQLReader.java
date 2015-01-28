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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.claresco.tinman.json.JsonUtility;
import com.claresco.tinman.lrs.XapiAgent;
import com.claresco.tinman.lrs.XapiContext;
import com.claresco.tinman.lrs.XapiGroup;
import com.claresco.tinman.lrs.XapiInverseFunctionalIdentifier;
import com.claresco.tinman.lrs.XapiObject;
import com.claresco.tinman.lrs.XapiResult;
import com.claresco.tinman.lrs.XapiStatement;
import com.claresco.tinman.lrs.XapiVerb;
import com.claresco.tinman.lrs.XapiActor;
import com.claresco.tinman.servlet.XapiBadParamException;
import com.google.gson.Gson;

/**
 * XapiStatementSqlReader.java
 *
 * Read from database
 *
 *
 *
 * @author rheza
 * on Feb 26, 2014
 * 
 */

class XapiStatementSQLReader extends SQLReader {

	protected PreparedStatement myUUIDRetrievalStatement;
	protected PreparedStatement myIDRetrievalStatement;
	protected PreparedStatement myRetrievalByActorStatement;
	protected PreparedStatement myRetrievalByActorAsObjectStatement;
	protected PreparedStatement myRetrievalByVerbStatement;
	protected PreparedStatement myVoidedRetrievalStatement;
	
	private String myTableName = "statement";
	
	private XapiActorSQLReader myActorReader;
	private XapiVerbSQLReader myVerbReader;
	private XapiObjectSQLReader myObjectReader;
	private XapiContextSQLReader myContextReader;
	private XapiResultSQLReader myResultReader;
	
	 /**
	 * Description:
	 *	Constructor
	 *
	 * Params:
	 *	
	 */
	public XapiStatementSQLReader(Connection conn, XapiActorSQLReader theActorReader, 
			XapiVerbSQLReader theVerbReader, XapiObjectSQLReader theObjectReader, XapiContextSQLReader 
			theContextReader, XapiResultSQLReader theResultReader) throws SQLException{
		this.myConn = conn;
		this.myUUIDRetrievalStatement = SQLUtility.createRetrievalStatement(super.myConn, 
				myTableName, new String[]{"statementuuid", "isvoided"});
		this.myIDRetrievalStatement = SQLUtility.createRetrievalStatement(super.myConn, 
				myTableName, "statementid");
		this.myRetrievalByActorStatement = SQLUtility.createRetrievalStatement(myConn, 
				myTableName, "actorid");
		this.myRetrievalByVerbStatement = SQLUtility.createRetrievalStatement(myConn, 
				myTableName, "verbid");
		this.myRetrievalByActorAsObjectStatement = createActorAsObjectStatement();
		this.myVoidedRetrievalStatement = SQLUtility.createRetrievalStatement(myConn, myTableName, 
				new String[]{"statementid", "isvoided"});
		
		this.myActorReader = theActorReader;
		this.myVerbReader = theVerbReader;
		this.myObjectReader = theObjectReader;
		this.myContextReader = theContextReader;
		this.myResultReader = theResultReader;
	}

	
	
	/**
	 * 
	 * Description:
	 *	Return a statement object from the database based on its uuid
	 *
	 * Params:
	 *	theUUID
	 */
	protected HashMap<Integer, XapiStatement> retrieveByUUID(String theUUID) throws SQLException{
		this.myUUIDRetrievalStatement.setString(1, theUUID);
		this.myUUIDRetrievalStatement.setInt(2, 0);
		myResult = this.myUUIDRetrievalStatement.executeQuery();
		
		HashMap<Integer, XapiStatement> statementArray = getStatementFromResult();
		
		assert statementArray.size() <= 1;
		
		return statementArray;
	}

	
	
	protected HashMap<Integer, XapiStatement> retrieveByUUID(UUID theUUID) throws SQLException{
		return this.retrieveByUUID(theUUID.toString());
	}

	
	
	/**
	 * 
	 * Description:
	 *	Retrieve a statement object from the database based on its uuid
	 *
	 * Params:
	 *
	 */
	protected HashMap<Integer, XapiStatement> retrieveByID(int theID) throws SQLException
			, XapiDataIntegrityException{
		this.myIDRetrievalStatement.setInt(1, theID);
		
		myResult = this.myIDRetrievalStatement.executeQuery();
		
		HashMap<Integer, XapiStatement> statementArray = getStatementFromResult();
		
		if(statementArray.size() > 1){
			throw new XapiDuplicateStatementIDException("something went wrong");
		}
		
		return statementArray;
	}
	
	
	
	protected boolean doesStatementExists(String theUUID) throws SQLException{
		this.myUUIDRetrievalStatement.setString(1, theUUID);
		this.myUUIDRetrievalStatement.setInt(2, 0);
		myResult = this.myUUIDRetrievalStatement.executeQuery();
		
		if (!myResult.isBeforeFirst() ) {    
			 return false; 
		} 
		return true;
	}
	
	
	
	/**
	 * 
	 * Definition:
	 *	Retrieve Statement based on its UUID
	 *
	 * Params:
	 *
	 *
	 */
	protected int findIDByUUID(String theUUID) throws SQLException{
		this.myUUIDRetrievalStatement.setString(1, theUUID);
		this.myUUIDRetrievalStatement.setInt(2, 0);
		myResult = this.myUUIDRetrievalStatement.executeQuery();
		
		if(!myResult.isBeforeFirst()){
			return -1;
		}
		
		myResult.next();
		return myResult.getInt(1);
	}
	
	
	
	/**
	 * 
	 * Definition:
	 *	Retrieve statements based on its actor id
	 *
	 * Params:
	 *
	 *
	 */
	protected HashMap<Integer, XapiStatement> retrieveStatementByActor(int actorID) throws SQLException{
		// Find statements where actor is a subject
		myRetrievalByActorStatement.setInt(1, actorID);
		myResult = myRetrievalByActorStatement.executeQuery();
		HashMap<Integer, XapiStatement> statementMap = getStatementFromResult();
		
		// Find statements where actor is an object
		myRetrievalByActorAsObjectStatement.setInt(1, actorID);
		myResult = myRetrievalByActorAsObjectStatement.executeQuery();
		statementMap.putAll(getStatementFromResult());
		
		// Find groups which the actor belongs to and then find statement which those groups are in
		ArrayList<Integer> groupIDArray = myActorReader.retrieveGroupsOfAgent(actorID);
		for(Integer i : groupIDArray){
			statementMap.putAll(retrieveStatementByActor(i));
		}
		
		return statementMap;
	}
	
	
	
	/**
	 * 
	 * Definition:
	 *	Return statements whose verbs match the IRI
	 *
	 * Params:
	 *
	 *
	 */
	protected HashMap<Integer, XapiStatement> retrieveStatementByVerb(String theVerbIRI) throws SQLException{
		int theVerbID = myVerbReader.retrieveIDByValue(theVerbIRI);
		
		myRetrievalByVerbStatement.setInt(1, theVerbID);
		myResult = myRetrievalByVerbStatement.executeQuery();
		HashMap<Integer, XapiStatement> statementMap = getStatementFromResult();
		
		return statementMap;
	}
	
	
	
	private HashMap<Integer, XapiStatement> retrieveVoidedStatementByID(String theUUID) throws SQLException{
		myVoidedRetrievalStatement.setString(1, theUUID);
		myVoidedRetrievalStatement.setInt(2, 1);
		
		myResult = myVoidedRetrievalStatement.executeQuery();
		
		HashMap<Integer, XapiStatement> myVoidedStatementMap = getStatementFromResult();
		
		return myVoidedStatementMap;
	}
	
	
	
	/**
	 * 
	 * Definition:
	 *	Helper method that will create a hashmap from result
	 *
	 * Params:
	 *
	 *
	 */
	private HashMap<Integer, XapiStatement> getStatementFromResult() throws SQLException{
		HashMap<Integer, XapiStatement> statementArray = new HashMap<Integer, XapiStatement>();
		
		while(myResult.next()){
			UUID theId = UUID.fromString(myResult.getString("statementuuid"));
			XapiActor theActor = myActorReader.retrieveByID(myResult.getInt("actorid"));
			XapiVerb theVerb = myVerbReader.retrieveByID(myResult.getInt("verbid"));
			XapiObject theObject = myObjectReader.retrieveByID(myResult.getInt("objectid"));
			XapiResult theResult = null;
			int theResultID = myResult.getInt("resultid");
			if(!myResult.wasNull()){
				theResult = myResultReader.retrieveByID(theResultID);
			}
			
			XapiContext theContext = null;
			int theContextID = myResult.getInt("contextid");
			if(!myResult.wasNull()){
				theContext = myContextReader.retrieveByID(theContextID);
			}
			
			String theTSString = null;
			
			Timestamp theTS = myResult.getTimestamp("sttime");
			if(!myResult.wasNull()){
				DateTime theTimestamp = SQLUtility.getDatetime(theTS);
				theTSString = theTimestamp.withZoneRetainFields(DateTimeZone.UTC).toString();
			}
			
			// Voided statement should not be returned
			int isVoided = myResult.getInt("isVoided");
			if(isVoided == 0){
				statementArray.put(myResult.getInt("statementid") ,new XapiStatement(theId, theActor,
						theVerb, theObject, theResult, theContext, theTSString));
			}
		}
		
		return statementArray;
	}
	
	
	
	protected HashMap<Integer, XapiStatement> handleConjuctionQuery(HashMap<String, String> myParamMap) throws SQLException,
			XapiDataIntegrityException, XapiSQLOperationProblemException{
		if(myParamMap.size() == 0){
			throw new XapiConflictingParamException("There is no parameter");
		}
		
		if(myParamMap.containsKey("statementId") && myParamMap.size() > 1){
			throw new XapiConflictingParamException("Other parameters are not suitable with statementId");
		}else if(myParamMap.containsKey("voidedStatementId") && myParamMap.size() > 1){
			throw new XapiConflictingParamException("Other parameters are not suitable with voidedStatementId");
		}else if(myParamMap.containsKey("statementId") && myParamMap.containsKey("voidedStatementId")){
			throw new XapiConflictingParamException("Can't have both statementId and voidedStatementId");
		}else if(myParamMap.containsKey("statementId") && myParamMap.size() == 1){
			return retrieveByUUID(myParamMap.get("statementId"));
		}else if(myParamMap.containsKey("voidedStatementId") && myParamMap.size() == 1){
			return retrieveVoidedStatementByID(myParamMap.get("voidedStatementId"));
		}else{
			ArrayList<Integer> myStatementIDs = retrieveStatementsByParameter(myParamMap);
			
			HashMap<Integer, XapiStatement> myResultMap = new HashMap<Integer, XapiStatement>();
			
			if(myStatementIDs == null){
				return null;
			}
			
			for(Integer i : myStatementIDs){
				myResultMap.put(i, retrieveByID(i.intValue()).get(i));
			}
			
			return myResultMap;
		}
	}
	
	
	
	private ArrayList<Integer> retrieveStatementsByParameter(HashMap<String, String> paramMaps)
			throws SQLException, XapiDataIntegrityException, XapiSQLOperationProblemException{
		String topHalf = "select st.statementid from statement st";
		String bottomHalf = "where";

		// This is the conjuction query
		int i = 1;
		HashMap<String, Integer> positionMap = new HashMap<String, Integer>();
		for(String s : paramMaps.keySet()){
			if(s.equals("statementId")){
				bottomHalf += "st.statementid = ?";
				positionMap.put(s, new Integer(i));
				i++;
			}else if(s.equals("agent")){
				topHalf += createTopHalfString("actor", "a", "actorid");
				bottomHalf += createBottomHalfString("a", "actorid");
				positionMap.put(s, new Integer(i));
				i++;
			}else if(s.equals("verb")){
				topHalf += createTopHalfString("verb", "v", "verbid");
				bottomHalf += createBottomHalfString("v", "verbiri");
				positionMap.put(s, new Integer(i));
				i++;
			}else if(s.equals("activity")){
				topHalf += createTopHalfString("object", "obj", "objectid");
				topHalf += createTopHalfString("activity", "actv", "activityid");
				bottomHalf += createBottomHalfString("actv", "actviri");
				positionMap.put(s, new Integer(i));
				i++;
			}else if(s.equals("since")){
				bottomHalf += createBottomHalfString("st", "ststored", ">");
				positionMap.put(s, new Integer(i));
				i++;
			}else if(s.equals("until")){
				bottomHalf += createBottomHalfString("st", "ststored", "<");
				positionMap.put(s, new Integer(i));
				i++;
			}
		}
		
		bottomHalf += createBottomHalfString("st", "isvoided");
		positionMap.put("isvoided", i);
		
		bottomHalf = bottomHalf.replace("where and", "where");
		
		// Always order by stored time in desceding onder
		String ending = " order by ststored desc;";

		String fullString = topHalf + " " + bottomHalf + ending;

		return executeConjQueryStatement(paramMaps, positionMap, fullString);
	}
	
	
	
	private String createTopHalfString(String tableName, String tableNickname, String fieldName){
		String theString = " left join %s %s using (%s)";
		
		return String.format(theString, tableName, tableNickname, fieldName);
	}
	
	
	
	private String createBottomHalfString(String tableNickname, String fieldName){
		String theS = " and %s.%s = ?";
		
		return String.format(theS, tableNickname, fieldName);
	}
	
	
	
	private String createBottomHalfString(String tableNickname, String fieldName, String operator){
		String theS = " and %s.%s %s ?";
		
		return String.format(theS, tableNickname, fieldName, operator);
	}
	
	
	
	private ArrayList<Integer> executeConjQueryStatement(HashMap<String, String> paramMaps, 
			HashMap<String, Integer> locationMaps, String conjQueryStatement) throws SQLException,
			XapiSQLOperationProblemException{
		PreparedStatement myConjQueryStatement = myConn.prepareStatement(conjQueryStatement);
		DateTimeFormatter theFormatter = ISODateTimeFormat.dateTimeParser();
		for(String paramName : paramMaps.keySet()){
			String paramValue = paramMaps.get(paramName);
			int locationIndex = locationMaps.get(paramName).intValue();
			if(paramName.equals("statementId")){
				myConjQueryStatement.setString(locationIndex, paramValue);
			}else if(paramName.equals("agent")){
				
				myConjQueryStatement.setInt(locationIndex, Integer.parseInt(paramValue));
			}else if(paramName.equals("verb")){
				myConjQueryStatement.setString(locationIndex, paramValue);
			}else if(paramName.equals("activity")){
				myConjQueryStatement.setString(locationIndex, paramValue);
			}else if(paramName.equals("since")){
				try{
					DateTime myTimeStamp = theFormatter.parseDateTime(paramValue);
					Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
					myConjQueryStatement.setTimestamp(locationIndex, SQLUtility.getTimestamp(myTimeStamp), cal);
				}catch(IllegalArgumentException exc){
					throw new XapiSQLOperationProblemException("Having trouble reading the timestamp");
				}
			}else if(paramName.equals("until")){
				try{
					DateTime myTimeStamp = theFormatter.parseDateTime(paramValue);
					Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
					myConjQueryStatement.setTimestamp(locationIndex, SQLUtility.getTimestamp(myTimeStamp), cal);
				}catch(IllegalArgumentException exc){
					throw new XapiSQLOperationProblemException("Having trouble parsing the timestamp");
				}
			}
		}
		
		myConjQueryStatement.setInt(locationMaps.get("isvoided").intValue(), 0);
		
		myResult = myConjQueryStatement.executeQuery();
		
		ArrayList<Integer> arrayIDs = new ArrayList<Integer>();
		
		while(myResult.next()){
			arrayIDs.add(new Integer(myResult.getInt("statementid")));
		}
		
		if(arrayIDs.isEmpty()){
			return null;
		}
		
		return arrayIDs;
	}
	

	
	/**
	 * 
	 * Definition:
	 *	Helper method to create statement to retrieve actor as object
	 *
	 * Params:
	 *
	 *
	 */
	private PreparedStatement createActorAsObjectStatement() throws SQLException{
		String theString = "select * from statement where objectid in (select objectid from object where " +
				"actorid = ?)";
		
		return super.myConn.prepareStatement(theString);
	}
		
		
	
	/**
	 * Description:
	 * 	Close everything makes everything safe
	 */
	protected void close() throws SQLException{
		super.close();
		SQLUtility.closeStatement(this.myIDRetrievalStatement);
		SQLUtility.closeStatement(this.myUUIDRetrievalStatement);
		SQLUtility.closeStatement(myRetrievalByActorAsObjectStatement);
		SQLUtility.closeStatement(myRetrievalByActorStatement);
		SQLUtility.closeStatement(myRetrievalByVerbStatement);
	}
	
	
	
	public static void main(String[] args) {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
