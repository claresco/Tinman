/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiCredentialsJson.java	May 12, 2014
 *
 * Copyright 2014 Claresco Corporation, Berkeley, CA 94704. All Rights Reserved.
 *
 * This software is the proprietary information of Claresco Corporation.
 * Use is subject to license terms.
 *
 * Author : Rheza
 *
 */
package com.claresco.tinman.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

import org.joda.time.DateTime;

import com.claresco.tinman.lrs.XapiActor;
import com.claresco.tinman.lrs.XapiPerson;
import com.claresco.tinman.servlet.XapiBadParamException;
import com.claresco.tinman.servlet.XapiCredentials;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiCredentialsJson
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiCredentialsJson implements JsonDeserializer<XapiCredentials>, JsonSerializer<XapiCredentials>{
	
	private static final String ACTORS = "actors";
	private static final String HISTORICAL = "historical";
	private static final String SCOPE = "scope";
	private static final String EXPIRY = "expiry";
	private static final String REGISTRATION = "registration";
	private static final String ACTIVITY = "activity";
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiCredentials arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonObject theResult = new JsonObject();
		
		theResult.add(ACTORS, arg2.serialize(arg0.getPerson(), XapiPerson.class));
		
		theResult.add(HISTORICAL, new JsonPrimitive(arg0.getHistorical()));
		
		theResult.add(SCOPE, JsonUtility.convertToJsonArray(arg0.getScope()));
		
		theResult.addProperty(EXPIRY, arg0.getExpiry().toString());
		
		if(arg0.hasRegistration()){
			theResult.addProperty(REGISTRATION, arg0.getRegistration().toString());
		}
		
		if(arg0.hasActivityIDs()){
			theResult.add(ACTIVITY, JsonUtility.convertToJsonArrayFromIRIList(arg0.getActivityIDs()));
		}
		
		return theResult;
	}
	
	
	/* (non-Javadoc)
 	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiCredentials deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		JsonObject theJsonCredentials = JsonUtility.convertJsonElementToJsonObject(arg0);
		
		// Default values
		XapiPerson thePerson = null;
		boolean theHistorical = false;
		ArrayList<String> theScope = new ArrayList<String>();
		String theExpiry = null;
		ArrayList<String> theActivityIDs = new ArrayList<String>();
		String theRegistration = null;
		
		// Retrieve actor
		if(JsonUtility.hasElement(theJsonCredentials, ACTORS)){
			thePerson = JsonUtility.delegateDeserialization(arg2, JsonUtility.get(theJsonCredentials, ACTORS)
					, XapiPerson.class);
		}
		// Retrieve historical
		if(JsonUtility.hasElement(theJsonCredentials, HISTORICAL)){
			theHistorical = JsonUtility.getElementAsBool(theJsonCredentials, HISTORICAL);
		}
		// Retrieve expiry
		if(JsonUtility.hasElement(theJsonCredentials, EXPIRY)){
			theExpiry = JsonUtility.getElementAsString(theJsonCredentials, EXPIRY);
		}
		//Retrieve registration
		if(JsonUtility.hasElement(theJsonCredentials, REGISTRATION)){
			theRegistration = JsonUtility.getElementAsString(theJsonCredentials, REGISTRATION);
		}
		//Retrieve scope
		if(JsonUtility.hasElement(theJsonCredentials, SCOPE)){
			JsonArray theArray = theJsonCredentials.getAsJsonArray(SCOPE);
			for(JsonElement e : theArray){
				theScope.add(e.getAsString());
			}
		}
		//Retrieve activities
		if(JsonUtility.hasElement(theJsonCredentials, ACTIVITY)){
			JsonArray theArray = theJsonCredentials.getAsJsonArray(ACTIVITY);
			for(JsonElement e : theArray){
				theActivityIDs.add(e.getAsString());
			}
		}
		
		DateTime theReceivedTimestamp = DateTime.now();
		
		try{
			return new XapiCredentials(theScope, theExpiry, theHistorical, thePerson, theActivityIDs,
					theRegistration, theReceivedTimestamp);
		}catch(XapiBadParamException e){
			throw new XapiBadRequestException(e.getMessage());
		}
		
	}
}
