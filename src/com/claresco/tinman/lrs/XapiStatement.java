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

package com.claresco.tinman.lrs;

import java.util.*;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.claresco.tinman.lrs.*;

/**
 * XAPI_Statement.java
 *
 * The implementation for statement
 *
 *
 *
 * @author rheza
 * on Jan 14, 2014
 * 
 */

public class XapiStatement{
	
	private UUID myID;
	private XapiActor myActor;
	private XapiVerb myVerb;
	private XapiObject myObject;
	private XapiResult myResult;
	private XapiContext myContext;
	private DateTime myTimeStamp;
	private String myTimeStampAsString;
	private DateTime myStored;
	private XapiAuthority myAuthority;
	private boolean isVoiding;
	
	/**
	 * 
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiStatement(XapiActor theActor, XapiVerb theVerb, XapiObject theObject, XapiResult theResult,
			XapiContext theContext, String theTimeStamp){
		this.myID = UUID.randomUUID();
		this.myActor = theActor;
		this.myVerb = theVerb;
		this.myObject = theObject;
		this.myResult = theResult;
		this.myContext = theContext;
		this.myTimeStampAsString = theTimeStamp;
		
		DateTimeFormatter theFormatter = ISODateTimeFormat.dateTimeParser();
		if(theTimeStamp != null){
			myTimeStamp = theFormatter.parseDateTime(theTimeStamp);
		}
		else{
			myTimeStamp = null;
		}
		
		this.isVoiding = myVerb.isVoided();
	}
	
	
	
	public XapiStatement(UUID theID, XapiActor theActor, XapiVerb theVerb, XapiObject theObject, XapiResult theResult,
			XapiContext theContext, String theTimeStamp){
		this(theActor, theVerb, theObject, theResult, theContext, theTimeStamp);
		if(theID != null){
			this.myID = theID;
		}
	}
	
	
	
	public XapiStatement(UUID theID, XapiActor theActor, XapiVerb theVerb, XapiObject theObject, XapiResult theResult,
			XapiContext theContext, String theTimeStamp, DateTime theStored){
		this(theID, theActor, theVerb, theObject, theResult, theContext, theTimeStamp);
		this.myStored = theStored;
	}
	
	
	
	public boolean isNested(){
		if(this.myObject.getObjectType().equals("SubStatement")){
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * @return the myActor
	 */
	public XapiActor getActor() {
		return myActor;
	}
	
	
	/**
	 * @return the myObject
	 */
	public XapiObject getObject() {
		return myObject;
	}
	
	
	
	public XapiVerb getVerb(){
		return this.myVerb;
	}
	
	
	
	public String getId(){
		return this.myID.toString();
	}
	
	
	
	public XapiResult getResult(){
		return this.myResult;
	}
	
	
	
	public XapiContext getContext(){
		return this.myContext;
	}
	
	
	
	public DateTime getTimeStamp(){
		return this.myTimeStamp;
	}
	
	
	
	public String getTimeStampAsString(){
		return this.myTimeStampAsString;
	}
	
	
	
	public DateTime getStored(){
		return this.myStored;
	}
	
	
	
	public boolean hasID(){
		return this.myID != null;
	}
	
	
	
	public boolean hasActor(){
		return this.myActor != null;
	}
	
	
	
	public boolean hasVerb(){
		return this.myVerb != null;
	}
	
	
	
	public boolean hasObject(){
		return this.myObject != null;
	}
	
	
	
	public boolean hasResult(){
		return this.myResult != null;
	}
	
	
	
	public boolean hasContext(){
		return this.myContext != null;
	}
	
	
	
	public boolean hasTimeStamp(){
		return this.myTimeStamp != null;
	}
	
	
	
	public boolean isValid(){
		return hasID() && hasActor() && hasVerb() && hasObject();
	}
	
	
	
	public boolean isVoiding(){
		return isVoiding;
	}
	
	
	
	public void setID(String theID){
		this.myID = UUID.fromString(theID);
	}
	
	
	
	// bunch of other stuffs
	public String toString(){
		return "[id: " + myID.toString() + "\nactor: " + myActor + "\nverb: " + myVerb + "\nobject: " + myObject 
				+ "\nresult: " + myResult + "\ncontext: " + myContext   +"]";
	}
}
