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

import com.claresco.tinman.lrs.XapiLanguageMap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiLanguageMapJson.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Feb 25, 2014
 * 
 */

public class XapiLanguageMapJson implements JsonSerializer<XapiLanguageMap>, JsonDeserializer<XapiLanguageMap> {
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiLanguageMap arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonObject result = new JsonObject();
		
		for(String[] s : arg0.getLanguageMapAsArray()){
			result.addProperty(s[0], s[1]);
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiLanguageMap deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		JsonObject theLMapJSON = JsonUtility.convertJsonElementToJsonObject(arg0);
		
		XapiLanguageMap theLMap = new XapiLanguageMap();
		
		for (Map.Entry<String,JsonElement> entry : theLMapJSON.entrySet()) {
		    theLMap.registerLanguage(entry.getKey(), entry.getValue().getAsString());
		}
		
		return theLMap;
	}
	
}
