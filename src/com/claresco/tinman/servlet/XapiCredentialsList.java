/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiCredentialsList.java	Oct 1, 2014
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

import java.util.HashMap;
import java.util.Set;

import org.joda.time.DateTime;

/**
 * XapiCredentialsList
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiCredentialsList {

	private HashMap<XapiKeySecret, XapiCredentials> myCredentialMap;
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiCredentialsList(HashMap<XapiKeySecret, XapiCredentials> theCredentialMap) {
		myCredentialMap = theCredentialMap;
	}
	
	
	
	protected void put(XapiKeySecret theKeySecret, XapiCredentials theCredentials) {
		myCredentialMap.put(theKeySecret, theCredentials);
	}
	
	
	
	public XapiCredentials get(XapiKeySecret theKeySecret){
		return myCredentialMap.get(theKeySecret);
	}
	
	
	
	protected boolean containsKey(XapiKeySecret theKeySecret){
		return myCredentialMap.containsKey(theKeySecret);
	}
	
	
	
	public Set<XapiKeySecret> keySet(){
		return myCredentialMap.keySet();
	}
	
	
	
	protected void remove(XapiKeySecret theKeySecret){
		myCredentialMap.remove(theKeySecret);
	}
}
