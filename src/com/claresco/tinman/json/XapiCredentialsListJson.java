/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiCredentialsListJson.java	Oct 1, 2014
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
import java.util.HashMap;

import com.claresco.tinman.servlet.XapiCredentials;
import com.claresco.tinman.servlet.XapiCredentialsList;
import com.claresco.tinman.servlet.XapiKeySecret;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiCredentialsListJson
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiCredentialsListJson implements JsonSerializer<XapiCredentialsList>,
		JsonDeserializer<XapiCredentialsList>{
	private static final String KEYSECRET = "keySecret";
	private static final String CREDENTIALS = "credentials";
	private static final String KEY = "key";
	private static final String SECRET = "secret";
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiCredentialsList arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonArray theResult = new JsonArray();
		
		for(XapiKeySecret theKS : arg0.keySet()){
			JsonObject theObject = new JsonObject();
			
			JsonObject theKeySecretJson = new JsonObject();
			
			theKeySecretJson.addProperty(KEY, theKS.getKey());
			theKeySecretJson.addProperty(SECRET, theKS.getSecret());
			
			theObject.add(KEYSECRET, theKeySecretJson);
			
			theObject.add(CREDENTIALS, arg2.serialize(arg0.get(theKS), XapiCredentials.class));
			
			theResult.add(theObject);
		}
		
		return theResult;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiCredentialsList deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		if(!arg0.isJsonArray()){
			return null;
		}
			
		
		JsonArray theArray = arg0.getAsJsonArray();
		
		HashMap<XapiKeySecret, XapiCredentials> theCredentialMap = new HashMap<XapiKeySecret,
				XapiCredentials>();
		
		for(JsonElement e : theArray){
			JsonObject theObject = JsonUtility.convertJsonElementToJsonObject(e);
			JsonObject theKeySecretJson = JsonUtility.convertJsonElementToJsonObject
					(JsonUtility.get(theObject, KEYSECRET));
			
			XapiKeySecret theKS = new XapiKeySecret(JsonUtility.getElementAsString(theKeySecretJson, KEY), 
					JsonUtility.getElementAsString(theKeySecretJson, SECRET));
			
			XapiCredentials theCred = JsonUtility.delegateDeserialization(arg2, JsonUtility.get(theObject,
					CREDENTIALS), XapiCredentials.class);
			
			theCredentialMap.put(theKS, theCred);
		}
		
		return new XapiCredentialsList(theCredentialMap);
	}
}
