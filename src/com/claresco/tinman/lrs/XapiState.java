/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiState.java	May 6, 2014
 *
 * Copyright 2014 Claresco Corporation, Berkeley, CA 94704. All Rights Reserved.
 *
 * This software is the proprietary information of Claresco Corporation.
 * Use is subject to license terms.
 *
 * Author : Rheza
 *
 */
package com.claresco.tinman.lrs;

import java.util.UUID;

/**
 * XapiState
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiState {
	
	private String myID;
	private String myActivityIRI;
	private XapiActor myActor;
	private UUID myRegistation;
	private String myDocument;
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiState(String theID, String theIRI, XapiActor theActor, String theRegistration,
			String theDocument) {
		myID = theID;
		myActivityIRI = theIRI;
		myActor = theActor;
		
		if(theRegistration != null){
			myRegistation = UUID.fromString(theRegistration);
		}else{
			myRegistation = null;
		}
		
		myDocument = theDocument;
	}
	
	
	public String getID(){
		return myID;
	}
	
	
	
	public String getActivityIRI(){
		return myActivityIRI;
	}
	
	
	
	public XapiActor getActor(){
		return myActor;
	}
	
	
	
	public UUID getRegistration(){
		return myRegistation;
	}
	
	
	
	public String getDocument(){
		return myDocument;
	}
	
	
	
	public boolean hasRegistration(){
		return myRegistation != null;
	}
}
