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

import com.claresco.tinman.lrs.XapiInteractionComponent;
import com.claresco.tinman.lrs.XapiLanguageMap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiInteractionComponentJson.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Mar 6, 2014
 * 
 */

public class XapiInteractionComponentJson implements JsonDeserializer<XapiInteractionComponent>, JsonSerializer<XapiInteractionComponent>{

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiInteractionComponent arg0, Type arg1,
			JsonSerializationContext arg2) {
		if(arg0.isEmpty()){
			return null;
		}
		
		JsonObject result = new JsonObject();
		
		result.addProperty("id", arg0.getID());
		
		if(arg0.hasDescription()){
			result.add("description", arg2.serialize(arg0.getDescription()));
		}
		
		return result;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiInteractionComponent deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		JsonObject theComponent = JsonUtility.convertJsonElementToJsonObject(arg0);
		
		String theID = JsonUtility.getElementAsString(theComponent, "id");
		
		//id is required
		if(theID == null){
			throw new XapiBadInteractionException("ID is required for interaction component");
		}
		
		XapiLanguageMap theDescription = JsonUtility.delegateDeserialization(arg2,
				JsonUtility.get(theComponent, "description"), XapiLanguageMap.class);
		return new XapiInteractionComponent(theID, theDescription);
	}
}
