/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiServletActionRequested.java	May 22, 2014
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

import com.claresco.tinman.lrs.XapiActor;
import com.claresco.tinman.lrs.XapiIRI;

/**
 * XapiServletActionRequested
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiServletActionRequested {
	
	private XapiServletActionType myActionType;
	private XapiActor myActor;
	private DateTime myTimestamp;
	private ArrayList<XapiIRI> myActivityIDs;
	private UUID myRegistration;
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiServletActionRequested(XapiServletActionType theActionType, XapiActor theActor,
			DateTime theTimestamp, XapiIRI theActivityID, UUID theRegistration) {
		this(theActionType, theActor, theTimestamp, new ArrayList<XapiIRI>(), theRegistration);
		if(theActivityID != null){
			myActivityIDs.add(theActivityID);
		}
	}
	
	
	
	public XapiServletActionRequested(XapiServletActionType theActionType, XapiActor theActor,
			DateTime theTimestamp, String theActivityID, UUID theRegistration) {
		this(theActionType, theActor, theTimestamp, new ArrayList<XapiIRI>(), theRegistration);
		if(theActivityID != null){
			myActivityIDs.add(new XapiIRI(theActivityID));
		}
	}
	
	
	
	public XapiServletActionRequested(XapiServletActionType theActionType, XapiActor theActor,
			DateTime theTimestamp, ArrayList<XapiIRI> theActivityIDs, UUID theRegistration) {
		myActionType = theActionType;
		myActor = theActor;
		myTimestamp = theTimestamp;
		myActivityIDs = theActivityIDs;
		myRegistration = theRegistration;
	}
	
	
	
	protected XapiServletActionType getServletActionType(){
		return myActionType;
	}
	
	
	
	protected XapiActor getActor(){
		return myActor;
	}
	
	
	
	protected DateTime getTimestamp(){
		return myTimestamp;
	}
	
	
	
	protected ArrayList<XapiIRI> getActivityID(){
		return myActivityIDs;
	}
	
	
	
	protected UUID getRegistration(){
		return myRegistration;
	}
	
	
	
	protected boolean hasRegistration(){
		return myRegistration != null;
	}
}
