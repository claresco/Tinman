/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiServletException.java	Mar 31, 2014
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

/**
 * @author Rheza
 * 
 * Description:
 * 
 * 
 * Status:
 * 	RED
 *
 */
public class XapiServletException extends Exception{
	
	static final long serialVersionUID = 5L;
	
	private String myMessage;
	private int myErrorNumber = 400;
	
	public XapiServletException(String theMessage){
		super(theMessage);
		myMessage = theMessage;
	}
	
	public XapiServletException(int theErrorNumber, String theMessage){
		super(theMessage);
		myMessage = theMessage;
		myErrorNumber = theErrorNumber;
	}
		
	public String getMessage(){
		return myMessage;
	}
	
	public int getErrorNumber(){
		return myErrorNumber;
	}
	
	
	
	public static void main(String[] args) {
	}
}
