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

import java.lang.reflect.Type;

import com.claresco.tinman.lrs.XapiAccount;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiAccountDeserializer.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Jan 23, 2014
 * 
 */

public class XapiAccountJson implements JsonDeserializer<XapiAccount>, JsonSerializer<XapiAccount>{
	
	private static final String HOMEPAGE = "homePage";
	private static final String NAME = "name";
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiAccount arg0, Type arg1,
			JsonSerializationContext arg2) {
		if(arg0 == null){
			return null;
		}
		
		JsonObject result = new JsonObject();
		
		result.addProperty(HOMEPAGE, arg0.getHomePage().toString());
		result.addProperty(NAME, arg0.getName());
		
		return result;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiAccount deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		
		if(JsonUtility.isJsonElementNull(arg0)){
			return null;
		}
		
		JsonObject accountJson = JsonUtility.convertJsonElementToJsonObject(arg0);
		
		// Retrieve the homepage and name
		String theHomePage = JsonUtility.getElementAsString(accountJson, HOMEPAGE);
		String theName = JsonUtility.getElementAsString(accountJson, NAME);
		
		XapiAccount theAccount = new XapiAccount(theHomePage, theName);
		
		if(theAccount.isEmpty()){
			return null;
		}
		
		if(!theAccount.isValid()){
			throw new XapiAccountNotValidException("Missing element for the account");
		}
		
		// Return and account object
		return theAccount;
	}
}
