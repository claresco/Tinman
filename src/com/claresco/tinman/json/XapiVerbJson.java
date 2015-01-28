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
import java.util.Map;

import com.claresco.tinman.lrs.XapiVerb;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiVerbDeserializer.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Jan 23, 2014
 * 
 */

public class XapiVerbJson implements JsonDeserializer<XapiVerb>, JsonSerializer<XapiVerb>{
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiVerb arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonObject result = new JsonObject();
		result.addProperty("id", arg0.getId().toString());

		// Add display
		if(arg0.hasDisplay()){
			JsonObject theDisplay = new JsonObject();
			for(String[] s : arg0.getDisplay().getLanguageMapAsArray()){
				theDisplay.addProperty(s[0], s[1]);
			}
			
			result.add("display", theDisplay);
		}
		
		return result;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiVerb deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		JsonObject theVerbJSON = JsonUtility.convertJsonElementToJsonObject(arg0);
		
		String theStringIRI = JsonUtility.getElementAsString(theVerbJSON, "id");
				
		// Id is a required field
		if(theStringIRI == null){
			throw new XapiBadVerbException("Verb's IRI is required");
		}
		
		XapiVerb theXapiVerb = new XapiVerb(theStringIRI);
		
		for (Map.Entry<String,JsonElement> entry : theVerbJSON.get("display").getAsJsonObject().entrySet()) {
		    theXapiVerb.registerDisplay(entry.getKey(), entry.getValue().getAsString());
		}
		
		return theXapiVerb;
	}
}
