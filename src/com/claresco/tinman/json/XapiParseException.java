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

package com.claresco.tinman.json;

import com.claresco.tinman.lrs.XapiClarescoException;
import com.google.gson.JsonParseException;

/**
 * XapiParsingException.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Jan 21, 2014
 * 
 */

public class XapiParseException extends JsonParseException{
	
	private int myErrorCode;
	private String myMessage;
	
	protected XapiParseException(String errorMessage){
		super(errorMessage);
		this.myMessage = errorMessage;
		myErrorCode = 400;
	}
	
	protected XapiParseException(String errorMessage, int errorCode){
		super(errorMessage);
		myErrorCode = errorCode;
		myMessage = errorMessage;
	}
	
	public int getErrorCode(){
		return myErrorCode;
	}
	
	public String getMessage(){
		return this.myMessage;
	}
}
