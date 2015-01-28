/**
 *------------------------* * ClarescoExperienceAPI
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.claresco.tinman.json.JsonUtility;
import com.claresco.tinman.lrs.XapiActivity;
import com.claresco.tinman.lrs.XapiActivityDefinition;
import com.claresco.tinman.lrs.XapiInteraction;
import com.claresco.tinman.lrs.XapiInteractionComponent;
import com.claresco.tinman.lrs.XapiStatement;
import com.google.gson.Gson;

/**
 * XapiActivitySqlWriter.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Feb 24, 2014
 * 
 */

class XapiActivitySQLWriter extends SQLWriter {

	private PreparedStatement myInsertStatement;
	private PreparedStatement myCorrectResponseInsertStatement;
	private PreparedStatement myInteractionCompInsertStatement;
	
	private PreparedStatement myActivityTypeInsertStatement;
	
	private PreparedStatement myUpdateStatement;
	private PreparedStatement myCorrectResponseUpdateStatement;
	private PreparedStatement myInteractionCompUpdateStatement;
	
	private XapiLanguageMapSQLWriter myLanguageMapWriter;
	private XapiExtensionSQLWriter myExtensionWriter;
	private XapiActivitySQLReader myActivityReader;
	
	private String[] myFieldNames = {"activityid", "actviri", "namelanguagemapid", "desclanguagemapid", 
			"activitytypecode", "interactiontypecode", "xextensionid"};
	private String[] correctResponseFieldNames = {"correctresponseid", "activityid", "crpattern"};
	private String[] interactionComponentFieldNames = {"interactioncomponentid", "activityid",
			"icompkey", "languagemapid", "interactioncomponenttypecode"};
	

	
	
	/**
	 * Description:
	 *
	 * Params:
	 *
	 */
	public XapiActivitySQLWriter(Connection conn, XapiLanguageMapSQLWriter theLanguageMapWriter,
			XapiExtensionSQLWriter theExtWriter, XapiActivitySQLReader theActivityReader) throws SQLException{
		this.myConn = conn;
		
		this.myInsertStatement = SQLUtility.createInsertStatement(super.myConn, "activity", myFieldNames);
		this.myCorrectResponseInsertStatement = SQLUtility.createInsertStatement(myConn, "correctresponse",
				correctResponseFieldNames);
		this.myInteractionCompInsertStatement = SQLUtility.createInsertStatement(myConn, "interaction" +
				"component", interactionComponentFieldNames);
		this.myInteractionCompUpdateStatement = SQLUtility.createUpdateStatement(myConn, "interactioncomponent",
				new String[]{"icompkey", "interactioncomponenttypecode"}, new String[]{"interactioncomponentid"});
		
		this.myActivityTypeInsertStatement = SQLUtility.createInsertStatement(myConn, "activitytype",
				new String[]{"activitytypecode", "atypiri"});
		
		this.myUpdateStatement = SQLUtility.createUpdateStatement(myConn, "activity", new String[]{
				"activitytypecode", "interactiontypecode", "namelanguagemapid", "desclanguagemapid", 
				"xextensionid"}, new String[]{"activityid"});
		this.myCorrectResponseUpdateStatement = SQLUtility.createUpdateStatement(myConn, "correctresponse", 
				new String[]{"crpattern"}, new String[]{"correctresponseid"});
		
		this.myLanguageMapWriter = theLanguageMapWriter;
		this.myExtensionWriter = theExtWriter;
		
		this.myActivityReader = theActivityReader;
	}	
	
	
	
	protected int insertActivity(XapiActivity theActivity, boolean isNewActivityAllowed)
			throws SQLException, XapiDataIntegrityException{
		synchronized (this) {
			int activityID = myActivityReader.retrieveIDByValue(theActivity.getId().toString());
			
			// Checking if the activity is already in the database
			if (activityID != -1) {
				// Update the activity if it is necessary
				updateActivityIfNecessary(activityID, theActivity, isNewActivityAllowed);
			}
			else{
				if(!isNewActivityAllowed){
					throw new XapiActivityInvalidException("Not allowed to define new activity");
				}else{
					activityID = insertNewActivity(theActivity);
				}
			}
			
			return activityID;
		}
	}
	
	
	
	protected int insertNewActivity(XapiActivity theActivity) throws SQLException,
			XapiDataIntegrityException{
		int theId = super.fetchId();

		XapiInteraction theInteraction = null;
		
		this.myInsertStatement.setInt(1, theId);
		this.myInsertStatement.setString(2, theActivity.getId().toString());
		
		this.myInsertStatement.setNull(3, Types.NUMERIC);
		this.myInsertStatement.setNull(4, Types.NUMERIC);
		this.myInsertStatement.setNull(5, Types.CHAR);
		this.myInsertStatement.setNull(6, Types.CHAR);
		this.myInsertStatement.setNull(7, Types.NUMERIC);
		
		if(theActivity.hasDefinition()){
			XapiActivityDefinition theDefinition = theActivity.getDefinition();
			if(theDefinition.hasName()){
				this.myInsertStatement.setInt(3, myLanguageMapWriter.insertNewLanguageMap
						(theDefinition.getName()));
			}
			if(theDefinition.hasDescription()){
				this.myInsertStatement.setInt(4, myLanguageMapWriter.insertNewLanguageMap
						(theDefinition.getDescription()));
			}
			
			if(theDefinition.hasType()){
				String theTypeCode = insertActivityType(theDefinition.getType().toString());
				myInsertStatement.setString(5, theTypeCode);
			}
			
			if(theDefinition.hasInteractionProperties()){
				theInteraction = theDefinition.getInteractionProperties();
				
				myInsertStatement.setString(6, getInteractionTypeCode(theInteraction.getType()));
			}
			
			if(theDefinition.hasExtension()){
				myInsertStatement.setInt(7, myExtensionWriter.insertNewExtension
						(theDefinition.getExtension()));
			}
		}
		
		this.myInsertStatement.executeUpdate();
		
		if(theInteraction != null){
			if(theInteraction.hasCorrectReponse()){
				insertCorrectResponse(theInteraction.getCorrectResponse(), theId);
			}
			if(theInteraction.hasChoices()){
				insertInteractionComponent("CHOICES", theInteraction.getChoices(), theId);
			}
			if(theInteraction.hasScale()){
				insertInteractionComponent("SCALE", theInteraction.getScale(), theId);
			}
			if(theInteraction.hasSource()){
				insertInteractionComponent("SOURCE", theInteraction.getSource(), theId);
			}
			if(theInteraction.hasTarget()){
				insertInteractionComponent("TARGET", theInteraction.getTarget(), theId);
			}
			if(theInteraction.hasSteps()){
				insertInteractionComponent("STEPS", theInteraction.getSteps(), theId);
			}
		}
		
		
		return theId;
	}
	
	
	
	private String getInteractionTypeCode(String interactionType) throws XapiDataIntegrityException{
		if(interactionType.equalsIgnoreCase("choice")){
			return "CHO";
		}else if(interactionType.equalsIgnoreCase("sequencing")){
			return "SEQ";
		}else if(interactionType.equalsIgnoreCase("likert")){
			return "LIKE";
		}else if(interactionType.equalsIgnoreCase("matching")){
			return "MAT";
		}else if(interactionType.equalsIgnoreCase("performance")){
			return "PERF";
		}else if(interactionType.equalsIgnoreCase("true-false")){
			return "TF";
		}else if(interactionType.equalsIgnoreCase("fill-in")){
			return "FILL";
		}else if(interactionType.equalsIgnoreCase("numeric")){
			return "NUM";
		}else if(interactionType.equalsIgnoreCase("other")){
			return "OTH";
		}else{
			throw new XapiActivityInvalidException("Bad interaction");
		}
	}
	
	
	
	private String insertActivityType(String theType) throws SQLException{
		String theDatabaseID = myActivityReader.retrieveActivityTypeCode(theType);
		
		if(theDatabaseID != null){
			return theDatabaseID;
		}
		
		int theID = super.fetchId();
		
		String theActivityTypeCode = String.valueOf(theID);
		
		myActivityTypeInsertStatement.setString(1, theActivityTypeCode);
		myActivityTypeInsertStatement.setString(2, theType);
		myActivityTypeInsertStatement.executeUpdate();
		
		return theActivityTypeCode;
	}
	
	
	
	private void insertCorrectResponse(ArrayList<String> theCorrectResponses, int theActID) throws SQLException{
		for (String s : theCorrectResponses){
			insertOneCorrectResponse(s, theActID);
		}
	}
	
	
	
	private void insertOneCorrectResponse(String theResponse, int theActID) throws SQLException{
		int theID = super.fetchId();
		myCorrectResponseInsertStatement.setInt(1, theID);
		myCorrectResponseInsertStatement.setInt(2, theActID);
		myCorrectResponseInsertStatement.setString(3, theResponse);
		myCorrectResponseInsertStatement.executeUpdate();
	}
	
	
	
	private void insertInteractionComponent(String iCompType, ArrayList<XapiInteractionComponent> theComponents,
			int theActivityID) throws SQLException{
		for(XapiInteractionComponent iComp : theComponents){
			insertOneInteractionComponent(iCompType, iComp, theActivityID);
		}
	}
	
	
	
	private void insertOneInteractionComponent(String iCompType, XapiInteractionComponent iComp, int theActivityID)
			throws SQLException{
		myInteractionCompInsertStatement.setInt(1, super.fetchId());
		myInteractionCompInsertStatement.setInt(2, theActivityID);
		myInteractionCompInsertStatement.setString(3, iComp.getID());
		
		if(iComp.getDescription() != null){
			myInteractionCompInsertStatement.setInt(4, myLanguageMapWriter.insertNewLanguageMap
					(iComp.getDescription()));
		}else{
			myInteractionCompInsertStatement.setNull(4, Types.NUMERIC);
		}
		
		myInteractionCompInsertStatement.setString(5, iCompType);
		myInteractionCompInsertStatement.executeUpdate();
	}
	
	
	
	protected void updateActivityIfNecessary(int theActvID, XapiActivity theActivity, boolean isDefiningActivityAllowed)
			throws SQLException, XapiDataIntegrityException{
		XapiActivity theExistingActivity = myActivityReader.retrieveByID(theActvID);
		
		// Somehow the activity is not found in the database
		if(theExistingActivity == null){
			throw new XapiActivityInvalidException("Trying to update not existing activity");
		}
		
		// Determine if activity needs to be updated
		if(!theActivity.equals(theExistingActivity)){
			if(isDefiningActivityAllowed){
				updateActivity(theActvID, theActivity);
			}else{
				throw new XapiActivityInvalidException("Not allowed to redefine the activity");
			}
		}
	}
	
	
	
	private void updateActivity(int theActvID, XapiActivity theActivity) throws SQLException,
			XapiDataIntegrityException{
		myUpdateStatement.setNull(1, Types.CHAR);
		myUpdateStatement.setNull(2, Types.CHAR);
		myUpdateStatement.setNull(3, Types.INTEGER);
		myUpdateStatement.setNull(4, Types.INTEGER);
		myUpdateStatement.setNull(5, Types.INTEGER);
		
		// If activity does not have a definition, throw an error
		if(!theActivity.hasDefinition()){
			throw new XapiInvalidActivityException("Activity Definition has to exists to update activity");
		}
		
		ResultSet myRS = myActivityReader.getActivityResultSet(theActvID);
		myRS.next();
		
		XapiActivityDefinition theDefinition = theActivity.getDefinition();

		XapiInteraction theInteraction = theDefinition.getInteractionProperties();
		
		// Update Activty Type
		if(theDefinition.hasType()){
			String theType = myActivityReader.retrieveActivityTypeCode(theDefinition.getType().toString());
			
			if(theType != null){
				myUpdateStatement.setString(1, theType);
			}else{
				myUpdateStatement.setString(1, insertActivityType(theDefinition.getType().toString()));
			}
		}
		
		// Update interaction type
		if(theDefinition.hasInteractionProperties()){
			theInteraction = theDefinition.getInteractionProperties();
			myUpdateStatement.setString(2, getInteractionTypeCode(theInteraction.getType()));
		}
		
		// Update the Name
		if(theDefinition.hasName()){
			int theNameID = myRS.getInt("namelanguagemapid");
			
			if(myRS.wasNull()){
				theNameID = myLanguageMapWriter.insertNewLanguageMap(theDefinition.getName());
			}else{
				myLanguageMapWriter.updateLanguageMap(theDefinition.getName(), theNameID);
			}
			myUpdateStatement.setInt(3, theNameID);
		}
		
		// Update the Description
		if(theDefinition.hasDescription()){
			int theDescID = myRS.getInt("desclanguagemapid");

			if(myRS.wasNull()){
				theDescID = myLanguageMapWriter.insertNewLanguageMap(theDefinition.getDescription());
			}else{
				myLanguageMapWriter.updateLanguageMap(theDefinition.getDescription(), theDescID);
			}
			myUpdateStatement.setInt(4, theDescID);
		}
		
		// Update the extension
		if(theDefinition.hasExtension()){
			int theExtID = myRS.getInt("xextensionid");
			
			if(myRS.wasNull()){
				theExtID = myExtensionWriter.insertNewExtension(theDefinition.getExtension());
			}else{
				myExtensionWriter.updateExtension(theDefinition.getExtension(), theExtID);
			}
			myUpdateStatement.setInt(5, theExtID);
		}
		
		// Set the activityID
		myUpdateStatement.setInt(6, theActvID);
		
		myUpdateStatement.executeUpdate();
		
		// Update the interaction properties
		if(theDefinition.hasInteractionProperties()){
			if(theInteraction.hasCorrectReponse()){
				updateCorrectResponse(theInteraction.getCorrectResponse(), theActvID);
			}
			
			ResultSet theResultSet = myActivityReader.getInteractionResultSet(theActvID);
			
			if(theInteraction.hasChoices()){
				updateInteractionComponent("CHOICES", theInteraction.getChoices(), theActvID,
						theResultSet);
			}
			if(theInteraction.hasScale()){
				updateInteractionComponent("SCALE", theInteraction.getScale(), theActvID, 
						theResultSet);
			}
			if(theInteraction.hasSource()){
				updateInteractionComponent("SOURCE", theInteraction.getSource(), theActvID, 
						theResultSet);
			}
			if(theInteraction.hasTarget()){
				updateInteractionComponent("TARGET", theInteraction.getTarget(), theActvID, 
						theResultSet);
			}
			if(theInteraction.hasSteps()){
				updateInteractionComponent("STEPS", theInteraction.getSteps(), theActvID, 
						theResultSet);
			}
		}
	}
	
	
	
	private void updateCorrectResponse(ArrayList<String> theCorrectResponses, int theActID)
			throws SQLException{
		ResultSet myRS = myActivityReader.getCorrectResponseResultSet(theActID);

		for (String s : theCorrectResponses){
			if(myRS.next()){
				updateOneCorrectResponse(s, myRS.getInt("correctresponseid"));
			}else{
				insertOneCorrectResponse(s, theActID);
			}
		}
	}
	
	
	
	private void updateOneCorrectResponse(String theResponse, int theID) throws SQLException{
		myCorrectResponseUpdateStatement.setString(1, theResponse);
		myCorrectResponseUpdateStatement.setInt(2, theID);
		myCorrectResponseUpdateStatement.executeUpdate();
	}
	
	
	
	private void updateInteractionComponent(String iCompType, ArrayList<XapiInteractionComponent> theComponents, 
			int theActivityID, ResultSet theResultSet) throws SQLException{
		for(XapiInteractionComponent iComp : theComponents){
			if(theResultSet.next()){
				updateOneInteractionComponent(iCompType, iComp, theResultSet.getInt("interactioncomponentid"));
			}else{
				insertOneInteractionComponent(iCompType, iComp, theActivityID);
			}
		}
	}
	
	
	
	
	private void updateOneInteractionComponent(String iCompType, XapiInteractionComponent iComp,
			int theICompID) throws SQLException{
		myInteractionCompUpdateStatement.setString(1, iComp.getID());
		myInteractionCompUpdateStatement.setString(2, iCompType);
		myInteractionCompUpdateStatement.setInt(3, theICompID);
		myInteractionCompUpdateStatement.executeUpdate();
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.sql.SQLWriter#close()
	 */
	@Override
	protected void close() throws SQLException {
		super.close();
		SQLUtility.closeStatement(myInsertStatement);
		SQLUtility.closeStatement(myCorrectResponseInsertStatement);
		SQLUtility.closeStatement(myInteractionCompInsertStatement);
		SQLUtility.closeStatement(myUpdateStatement);
		SQLUtility.closeStatement(myCorrectResponseUpdateStatement);
		SQLUtility.closeStatement(myInteractionCompUpdateStatement);
	}
	
	
	
	public static void main(String[] args) {
		try {
			Connection conn = SQLUtility.establishDefaultConnection();
			
			XapiExtensionSQLReader theExtR = new XapiExtensionSQLReader(conn);
			XapiExtensionSQLWriter theExtW = new XapiExtensionSQLWriter(conn, theExtR);
			
			XapiLanguageMapSQLReader theLReader = new XapiLanguageMapSQLReader(conn);
			XapiLanguageMapSQLWriter theLWriter = new XapiLanguageMapSQLWriter(conn, theLReader);
			
			XapiLanguageMapSQLReader theLMapReader = new XapiLanguageMapSQLReader(conn);
			XapiExtensionSQLReader theExtReader = new XapiExtensionSQLReader(conn);
			XapiActivitySQLReader theActReader = new XapiActivitySQLReader(conn, theLMapReader, theExtReader);
			
			XapiActivitySQLWriter theActWriter = new XapiActivitySQLWriter(conn, theLWriter, theExtW, theActReader);
			
			
			
		} catch (Exception e) {
		}
	}
	
}
