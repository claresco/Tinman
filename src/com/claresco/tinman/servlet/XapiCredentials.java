/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiCredentials.java	May 12, 2014
 *
 * Copyright 2014 Claresco Corporation, Berkeley, CA 94704. All Rights Reserved.
 *
 * This software is the proprietary information of Claresco Corporation.
 * Use is subject to license terms.
 *
 * Author : Rheza
 *
 */
package com.claresco.tinman.servlet;

import java.util.ArrayList;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.claresco.tinman.lrs.XapiActor;
import com.claresco.tinman.lrs.XapiAgent;
import com.claresco.tinman.lrs.XapiIRI;
import com.claresco.tinman.lrs.XapiInverseFunctionalIdentifier;
import com.claresco.tinman.lrs.XapiPerson;

/**
 * XapiCredentials
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiCredentials {
	
	private static ArrayList<String> myAcceptedScope;
	private ArrayList<String> myScope;
	private DateTime myExpiry;
	private boolean myHistorical;
	private XapiActor myActor;
	private XapiPerson myPerson;
	private ArrayList<XapiIRI> myActivityIDs = new ArrayList<XapiIRI>();
	private UUID myRegistration;
	private DateTime myReceivedTimestamp;
	
	private boolean myReadStatementsClearance = false;
	private boolean myWriteStatementsClearance = false;
	private boolean myReadAnyoneStatementsClearance = false;
	private boolean myReadStateClearance = false;
	private boolean myWriteStateClearance = false;
	private boolean myDefineClearance = false;
	private boolean myReadProfileClearance = false;
	private boolean myWriteProfileClearance = false;
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiCredentials(ArrayList<String> theScope, String theExpiry, boolean theHistorical,
			XapiPerson thePerson, ArrayList<String> theActivityIDs, String theRegistration, DateTime 
			theReceivedTimestamp) throws XapiBadParamException{
		myScope = theScope;
		myHistorical = theHistorical;
		myPerson = thePerson;
		
		if(theRegistration != null){
			myRegistration = UUID.fromString(theRegistration);
		}else{
			theRegistration = null;
		}
		
		myReceivedTimestamp = theReceivedTimestamp;
		
		DateTimeFormatter theFormatter = ISODateTimeFormat.dateTimeParser();
		if(theExpiry != null){
			myExpiry = theFormatter.parseDateTime(theExpiry);
		}
		else{
			// By default, 4 hours
			myExpiry = DateTime.now().plusHours(4);
		}
		
		for(String s : theActivityIDs){
			myActivityIDs.add(new XapiIRI(s));
		}
		
		populateAcceptedScope();
		
		// Assign appropriate clearance based on its scope
		for(String scope : theScope){
			if(!myAcceptedScope.contains(scope)){
				throw new XapiBadParamException("Scope is not accepted");
			}else{
				if(scope.equals("all")){
					myReadStatementsClearance = true;
					myWriteStatementsClearance = true;
					myReadStateClearance = true;
					myWriteStateClearance = true;
					myDefineClearance = true;
					myReadProfileClearance = true;
					myWriteProfileClearance = true;
					myReadAnyoneStatementsClearance = true;
				}else if(scope.equals("all/read")){
					myReadStatementsClearance = true;
					myReadAnyoneStatementsClearance = true;
					myReadStateClearance = true;
					myReadProfileClearance = true;
				}else if(scope.equals("profile")){
					myReadProfileClearance = true;
					myWriteProfileClearance = true;
				}else if(scope.equals("state")){
					myReadStateClearance = true;
					myWriteStateClearance = true;
				}else if(scope.equals("define")){
					myDefineClearance = true;
				}else if(scope.equals("statements/read")){
					myReadAnyoneStatementsClearance = true;
					myReadStatementsClearance = true;
				}else if(scope.equals("statements/read/mine")){
					myReadStatementsClearance = true;
				}else if(scope.equals("statements/write")){
					myWriteStatementsClearance = true;
				}
			}
		}
	}
	
	
	
	public XapiCredentials(boolean isDebugMode) throws XapiServletSecurityRiskException{
		if(!isDebugMode){
			throw new XapiServletSecurityRiskException("Had to abort due to security risk");
		}
	}
	
	
	
	// This is to help with LMS Credentials class
	protected XapiCredentials(){
	}
	
	
	
	public DateTime getExpiry(){
		return myExpiry;
	}
	
	
	
	public boolean getHistorical(){
		return myHistorical;
	}
	
	
	
	public XapiActor getActor(){
		return myPerson.getAnyAgent();
	}
	
	
	
	public XapiPerson getPerson(){
		return myPerson;
	}
	
	
	
	public UUID getRegistration(){
		return myRegistration;
	}
	
	
	
	public ArrayList<String> getScope(){
		return myScope;
	}
	
	
	
	public ArrayList<XapiIRI> getActivityIDs(){
		return myActivityIDs;
	}
	
	
	
	public boolean hasRegistration(){
		return myRegistration != null;
	}
	
	
	
	public boolean hasActivityIDs(){
		return myActivityIDs != null && !myActivityIDs.isEmpty();
	}
	
	
	
	public boolean hasScope(){
		return myScope != null && !myScope.isEmpty();
	}
	
	
	
	public boolean isDefiningAllowed(){
		return myDefineClearance;
	}
	
	
	
	private boolean isActorAllowed(XapiActor theActor, XapiServletActionType theAction){
		if(theAction.equals(XapiServletActionType.STATEMENTREADMINE)){
			return myPerson.containsActor(theActor);
		}else if(theAction.equals(XapiServletActionType.STATEMENTREADANY)){
			return true;
		}
		
		if(theActor == null){
			return false;
		}
		
		return myPerson.containsActor(theActor);
	}
	
	
	
	private boolean isTimestampAllowed(DateTime theTimestamp){
		if(theTimestamp == null){
			return false;
		}
		if(!myHistorical){
			return !theTimestamp.isBefore(myReceivedTimestamp) && theTimestamp.isBefore(myExpiry);
		}
		return theTimestamp.isBefore(this.myExpiry);
	}
	
	
	
	private boolean isActivityAllowed(ArrayList<XapiIRI> theActivityIDs){
		if(myActivityIDs == null || myActivityIDs.size() == 0){
			return true;
		}
		
		if(theActivityIDs == null || theActivityIDs.size() == 0){
			return true;
		}
		
		for(XapiIRI activityID : theActivityIDs){
			if(myActivityIDs.contains(activityID)){
				return true;
			}
		}
		
		for(XapiIRI theCredActvIRI : myActivityIDs){
			for(XapiIRI theActionActvIRI : theActivityIDs){
				if(theActionActvIRI.toString().startsWith(theCredActvIRI.toString())){
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	
	private boolean isRegistrationAllowed(String theRegistration){
		if(theRegistration == null){
			return true;
		}
		return myRegistration.equals(UUID.fromString(theRegistration));
	}
	
	
	
	private boolean isRegistrationAllowed(UUID theRegistration){
		if(theRegistration == null){
			return false;
		}
		return myRegistration.equals(theRegistration);
	}
	
	
	
	private boolean isActionAllowed(XapiServletActionType theActionType){
		switch (theActionType) {
		case STATEMENTWRITE:
			return myWriteStatementsClearance;
		case STATEMENTREADMINE:
			return myReadStatementsClearance;
		case STATEMENTREADANY:
			return myReadAnyoneStatementsClearance;
		case STATEWRITE:
			return myWriteStateClearance;
		case STATEREAD:
			return myReadStateClearance;
		case DEFINE:
			return myDefineClearance;
		case PROFILEWRITE:
			return myWriteProfileClearance;
		case PROFILEREAD:
			return myReadProfileClearance;
		default:
			return false;
		}
	}
	
	
	
	public boolean isValid(){
		for(String scope : myScope){
			if(!myAcceptedScope.contains(scope)){
				return false;
			}
		}
		return true;
	}
	
	
	
	// FIX !!!
	public boolean isActionRequestedAllow(XapiServletActionRequested theActionRequested){
		if(!isActorAllowed(theActionRequested.getActor(), theActionRequested.
				getServletActionType())){
			return false;
		}
		if(!isActivityAllowed(theActionRequested.getActivityID())){
			return false;
		}
		if(!isTimestampAllowed(theActionRequested.getTimestamp())){
			return false;
		}
		if(!isActionAllowed(theActionRequested.getServletActionType())){
			return false;
		}
		if(theActionRequested.hasRegistration()){
			if(!isRegistrationAllowed(theActionRequested.getRegistration())){
				return false;
			}
		}
		return true;
	}
	
	
	
	private void populateAcceptedScope(){
		myAcceptedScope = new ArrayList<String>();
		myAcceptedScope.add("statements/write");
		myAcceptedScope.add("statements/read/mine");
		myAcceptedScope.add("statements/read");
		myAcceptedScope.add("state");
		myAcceptedScope.add("define");
		myAcceptedScope.add("profile");
		myAcceptedScope.add("all/read");
		myAcceptedScope.add("all");
	}
	
	
	
	/**
	public static void main(String[] args) {
		ArrayList<String> theScope = new ArrayList<String>();
		theScope.add("statements/write");
		theScope.add("statements/write/mine");
		theScope.add("statements/read");
		theScope.add("state");
		theScope.add("define");
		theScope.add("profile");
		theScope.add("all/read");
		theScope.add("all");
		
		XapiPerson thePerson = new XapiPerson(theName, theMbox, theMboxSha1sum, theOpenID, theAccount)
		
		XapiActor theActor = new XapiAgent("Mars",new XapiInverseFunctionalIdentifier("mars@m.com", null, null, null));
		XapiAgent theAgent = new XapiAgent("Mars",new XapiInverseFunctionalIdentifier("mars@m.com", null, null, null));
		XapiAgent theBadAgent = new XapiAgent("Mars",new XapiInverseFunctionalIdentifier("mars@mars.com", null, null, null));
		
		ArrayList<String> theActv = new ArrayList<String>();
		theActv.add("activity1");
		
		String theR = UUID.randomUUID().toString();
		
		DateTime theReceived = DateTime.now().minusMinutes(10);
		
		String theExpiry = null;
		
		try {
			XapiCredentials theCredentials = new XapiCredentials(theScope, theExpiry, false, theActor, theActv, theR, theReceived);
			System.out.println(theCredentials.isActorAllowed(theBadAgent));
			
			System.out.println("timestamp permission:" + theCredentials.isTimestampAllowed(DateTime.now().minusHours(3)));
			System.out.println("activity permission: " + theCredentials.isActivityAllowed(new XapiIRI("activity3")));
			
			XapiServletActionType a = XapiServletActionType.STATEREAD;
			System.out.println("action permission for " + a.toString() + " is " + theCredentials.isActionAllowed(a));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	**/
}
