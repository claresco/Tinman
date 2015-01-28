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
import java.util.ArrayList;

import com.claresco.tinman.lrs.XapiActivity;
import com.claresco.tinman.lrs.XapiActivityDefinition;
import com.claresco.tinman.lrs.XapiExtension;
import com.claresco.tinman.lrs.XapiInteraction;
import com.claresco.tinman.lrs.XapiInteractionComponent;
import com.claresco.tinman.lrs.XapiLanguageMap;

/**
 * XapiActivitySQLReader.java
 *
 * Read activity related data from the database
 *
 *
 *
 * @author rheza
 * on Feb 27, 2014
 * 
 */

class XapiActivitySQLReader extends SQLReader {
	
	private String myTableName = "activity";
	
	private PreparedStatement myRetrievalByIDStatement;
	private PreparedStatement myRetrievalByValueStatement;
	private PreparedStatement myCorrectResponseRetrievalStatement;
	private PreparedStatement myInteractionComponentRetrievalStatement;
	private PreparedStatement myInteractionTypeLookupStatement;
	private PreparedStatement myActivityTypeLookupStatement;
	private PreparedStatement myActivityIRILookupStatement;
	
	private XapiLanguageMapSQLReader myLanguageMapReader;
	private XapiExtensionSQLReader myExtensionReader;
	
	/**
	 * Description:
	 *
	 * Params:
	 * @throws SQLException 
	 *
	 */
	protected XapiActivitySQLReader(Connection conn, XapiLanguageMapSQLReader
			theLanguageMapReader, XapiExtensionSQLReader theExtReader) throws SQLException {
		// TODO Auto-generated constructor stub
		this.myConn = conn;
		
		this.myRetrievalByIDStatement = SQLUtility.createRetrievalStatement
				(myConn, myTableName, "activityid");
		this.myRetrievalByValueStatement = SQLUtility.createRetrievalStatement
				(myConn, myTableName, new String[]{"actviri"});
		this.myCorrectResponseRetrievalStatement = SQLUtility.createRetrievalStatement
				(myConn, "correctresponse", new String[]{"activityid"});
		this.myInteractionComponentRetrievalStatement = SQLUtility.createRetrievalStatement
				(myConn, "interactioncomponent", new String[]{"activityid"});
		this.myInteractionTypeLookupStatement = SQLUtility.createRetrievalStatement(myConn,
				"interactiontype", new String[]{"interactiontypecode"});
		this.myActivityTypeLookupStatement = SQLUtility.createRetrievalStatement(myConn, 
				"activitytype", new String[]{"atypiri"});
		this.myActivityIRILookupStatement = SQLUtility.createRetrievalStatement(myConn, 
				"activitytype", new String[]{"activitytypecode"});
		
		this.myLanguageMapReader = theLanguageMapReader;
		this.myExtensionReader = theExtReader;
	}
	
	
	
	/**
	 * 
	 * Description:
	 *	Retrieve the activity based on its ID
	 * Params:
	 *
	 */
	protected XapiActivity retrieveByID(int theID) throws SQLException{
		myRetrievalByIDStatement.setInt(1, theID);
		
		myResult = myRetrievalByIDStatement.executeQuery();
		
		if(SQLUtility.isResultEmpty(myResult)){
			return null;
		}
		
		myResult.next();
		
		/**
		String theIRI = myResult.getString("actviri");
		XapiLanguageMap theName = myLanguageMapReader.retrieveByID(myResult.getInt
				("namelanguagemapid"));
		XapiLanguageMap theDescription = myLanguageMapReader.retrieveByID(myResult.
				getInt("desclanguagemapid"));
		String theType = myResult.getString("activitytypecode");
		String theInteractionType = myResult.getString("interactiontypecode");
		XapiExtension theExt = myExtensionReader.retrieveByID(myResult.getInt
				("xextensionid"));
		ArrayList<String> theCorrectResp = retrieveCorrectResponse(theID);
		
		XapiInteraction theInteraction = retrieveInteraction(theID, theInteractionType, theCorrectResp);
		
		XapiActivityDefinition theDefinition = new XapiActivityDefinition
				(theName, theDescription, theType, null, theInteraction, theExt);
		return new XapiActivity(theIRI, theDefinition);
		**/
		return getOneXapiActivityFromResult();
	}
	
	
	
	/**
	 * 
	 * Description:
	 *	Find the ID based on the IRI
	 *	Returns all the instances' id that uses that IRI
	 *
	 * Params:
	 *
	 */
	protected int retrieveIDByValue(String theIRI) throws SQLException{
		myRetrievalByValueStatement.setString(1, theIRI);
		
		myResult = myRetrievalByValueStatement.executeQuery();
		
		if(SQLUtility.isResultEmpty(myResult)){
			return -1;
		}
		
		myResult.next();
		
		return myResult.getInt("activityid");
	}
	
	
	protected String retrieveActivityTypeCode(String type) throws SQLException{
		myActivityTypeLookupStatement.setString(1, type);
		myResult = myActivityTypeLookupStatement.executeQuery();
		
		if(isResulEmpty()){
			return null;
		}
		
		myResult.next();
		return myResult.getString("activitytypecode");
	}
	
	
	
	protected String retrieveActivityIRI(String theTypeCode) throws SQLException{
		myActivityIRILookupStatement.setString(1, theTypeCode);
		myResult = myActivityIRILookupStatement.executeQuery();
		
		if(isResulEmpty()){
			return null;
		}
		
		myResult.next();
		return myResult.getString("atypiri");
	}
	
	
	
	protected ArrayList<XapiActivity> retrieveActivityByValue(String theIRI)
			throws SQLException{
		myRetrievalByValueStatement.setString(1, theIRI);
		
		myResult = myRetrievalByValueStatement.executeQuery();
		
		if(SQLUtility.isResultEmpty(myResult)){
			return null;
		}
		
		ArrayList<XapiActivity> activityArray = new ArrayList<XapiActivity>();
		while(myResult.next()){
			activityArray.add(getOneXapiActivityFromResult());
		}
		
		return activityArray;
	}
	
	
	
	private XapiActivity getOneXapiActivityFromResult() throws SQLException{
		int theID = myResult.getInt("activityid");
		String theIRI = myResult.getString("actviri");
		XapiLanguageMap theName = myLanguageMapReader.retrieveByID(myResult.getInt
				("namelanguagemapid"));
		XapiLanguageMap theDescription = myLanguageMapReader.retrieveByID(myResult.
				getInt("desclanguagemapid"));
		String theTypeCode = myResult.getString("activitytypecode");
		
		String theInteractionTypeCode = myResult.getString("interactiontypecode");
		
		String theInteractionType = null;
		
		if(theInteractionTypeCode != null){
			theInteractionType = retrieveInteractionType(theInteractionTypeCode);
		}
		
		XapiExtension theExt = myExtensionReader.retrieveByID(myResult.getInt
				("xextensionid"));
		ArrayList<String> theCorrectResp = retrieveCorrectResponse(theID);
		
		XapiInteraction theInteraction = retrieveInteraction(theID, theInteractionType, theCorrectResp);
		
		String theActivityIRI = null;
		
		if(theTypeCode != null){
			theActivityIRI = retrieveActivityIRI(theTypeCode);
		}
		
		XapiActivityDefinition theDefinition = new XapiActivityDefinition
				(theName, theDescription, theActivityIRI, null, theInteraction, theExt);
		return new XapiActivity(theIRI, theDefinition);
	}
	
	
	
	private String retrieveInteractionType(String theInteractionTypeCode) throws SQLException{
		String theInteractionType = null;
		
		this.myInteractionTypeLookupStatement.setString(1, theInteractionTypeCode);
		ResultSet theResult = myInteractionTypeLookupStatement.executeQuery();
		
		if(!SQLUtility.isResultEmpty(theResult)){
			theResult.next();
			theInteractionType = theResult.getString("itypname");
		}
		
		SQLUtility.closeResultSet(theResult);
				
		return theInteractionType.toLowerCase();
	}
	
	
	
	private ArrayList<String> retrieveCorrectResponse(int theActivityID) 
			throws SQLException{
		myCorrectResponseRetrievalStatement.setInt(1, theActivityID);
		myResult = myCorrectResponseRetrievalStatement.executeQuery();
		
		if(isResulEmpty()){
			return null;
		}
		
		ArrayList<String> correctResponsePattern = new ArrayList<String>();
		
		while(myResult.next()){
			correctResponsePattern.add(myResult.getString("crpattern"));
		}
		
		return correctResponsePattern;
	}
	
	
	
	protected ResultSet getCorrectResponseResultSet(int theActivityID) throws SQLException{
		myCorrectResponseRetrievalStatement.setInt(1, theActivityID);
		return myCorrectResponseRetrievalStatement.executeQuery();
	}
	
	
	
	private XapiInteraction retrieveInteraction(int theActivityID, String 
			theInteractionType, ArrayList<String> theCorrectResponses) throws SQLException{
		this.myInteractionComponentRetrievalStatement.setInt(1, theActivityID);
		
		ArrayList<XapiInteractionComponent> theChoices = new ArrayList<XapiInteractionComponent>();
		ArrayList<XapiInteractionComponent> theScale = new ArrayList<XapiInteractionComponent>();
		ArrayList<XapiInteractionComponent> theSource = new ArrayList<XapiInteractionComponent>();
		ArrayList<XapiInteractionComponent> theTarget = new ArrayList<XapiInteractionComponent>();
		ArrayList<XapiInteractionComponent> theSteps = new ArrayList<XapiInteractionComponent>();
		
		myResult = myInteractionComponentRetrievalStatement.executeQuery();
		
		String theID;
		XapiLanguageMap theDescription;
		while (myResult.next()) {
			theID = myResult.getString("icompkey");
			theDescription = myLanguageMapReader.retrieveByID(myResult.getInt("languagemapid"));
			XapiInteractionComponent theIComp = new XapiInteractionComponent(theID, theDescription);
			String theType = myResult.getString("interactioncomponenttypecode");
			if(theType.equals("CHOICES")){
				theChoices.add(theIComp);
			}else if (theType.equals("SCALE")) {
				theScale.add(theIComp);
			}else if (theType.equals("SOURCE")) {
				theSource.add(theIComp);
			}else if (theType.equals("TARGET")) {
				theTarget.add(theIComp);
			}else if (theType.equals("STEPS")) {
				theSteps.add(theIComp);
			}
		}
		
		if(theChoices.isEmpty()){
			theChoices = null;
		}
		if(theScale.isEmpty()){
			theScale = null;
		}
		if(theTarget.isEmpty()){
			theTarget = null;
		}
		if(theSource.isEmpty()){
			theSource = null;
		}
		if(theSteps.isEmpty()){
			theSteps = null;
		}
		
		
		
		
		return new XapiInteraction(theInteractionType, theCorrectResponses, theChoices,
				theScale, theTarget, theSource, theSteps);
	}
	
	
	
	
	protected ResultSet getInteractionResultSet(int theActivityID) throws SQLException{
		this.myInteractionComponentRetrievalStatement.setInt(1, theActivityID);
		
		return myInteractionComponentRetrievalStatement.executeQuery();
	}
	
	
	
	protected ResultSet getActivityResultSet(int theActivityID) throws SQLException{
		this.myRetrievalByIDStatement.setInt(1, theActivityID);
		
		return myRetrievalByIDStatement.executeQuery();
	}
	
	
	
	/**
	 * Description:
	 * 	Close everything
	 * 
	 */
	protected void close() throws SQLException{
		super.close();
		SQLUtility.closeStatement(myRetrievalByIDStatement);
		SQLUtility.closeStatement(myRetrievalByValueStatement);
		SQLUtility.closeStatement(myActivityTypeLookupStatement);
		SQLUtility.closeStatement(myCorrectResponseRetrievalStatement);
		SQLUtility.closeStatement(myInteractionComponentRetrievalStatement);
		SQLUtility.closeStatement(myInteractionTypeLookupStatement);
		SQLUtility.closeStatement(myActivityIRILookupStatement);
	}
	
	
	
	public static void main(String[] args) {
		try {
			Connection conn = SQLUtility.establishDefaultConnection();
			XapiLanguageMapSQLReader theLMapReader = new XapiLanguageMapSQLReader(conn);
			XapiExtensionSQLReader theExtReader = new XapiExtensionSQLReader(conn);
			XapiActivitySQLReader theReader = new XapiActivitySQLReader(conn, theLMapReader, theExtReader);
			
			ArrayList<String> corrR = theReader.retrieveCorrectResponse(10065);
			for(String c : corrR){
				System.out.println(c);
			}
			
			XapiInteraction theInter = theReader.retrieveInteraction(10629, "LIKERT", corrR);
			for(XapiInteractionComponent c : theInter.getChoices()){
				System.out.println(c.getID());
				System.out.println(c.getDescription());
			}
			
			XapiActivity theAct = theReader.retrieveByID(10629);
			System.out.println(theAct);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

}
