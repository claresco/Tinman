/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiClarescoException.java	Apr 8, 2014
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

import com.claresco.tinman.json.XapiParseException;

/**
 * XapiClarescoException
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiClarescoException extends Exception{
	
	private String myMessage;
	private int myErrorCode;
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiClarescoException(String theMessage, int theErrorCode) {
		myMessage = theMessage;
		myErrorCode = theErrorCode;
	}
	
	
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiClarescoException(XapiParseException e) {
		this(e.getMessage(), e.getErrorCode());
	}
	
	
	
	public int getErrorCode(){
		return myErrorCode;
	}
	
	
	
	public String getMessage(){
		return myMessage;
	}
	
}
