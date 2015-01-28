/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiExtensionsJson.java	Aug 7, 2014
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
import java.util.Map;

import com.claresco.tinman.lrs.XapiExtension;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiExtensionsJson
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiExtensionsJson implements JsonSerializer<XapiExtension>, JsonDeserializer<XapiExtension> {
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiExtension arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonObject theResult = new JsonObject();

		for(String key : arg0.getKeys()){
			theResult.addProperty(key, arg0.getValueOf(key));
		}
		
		return theResult;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiExtension deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		JsonObject extensionJsonMap = JsonUtility.convertJsonElementToJsonObject(arg0);
		 
		XapiExtension theExtension = new XapiExtension();
		
		for (Map.Entry<String,JsonElement> entry : extensionJsonMap.entrySet()) {
			JsonElement theValue = entry.getValue();
			if(theValue.isJsonPrimitive()){
				theExtension.add(entry.getKey(), entry.getValue().getAsString());
			}else{
				theExtension.add(entry.getKey(), entry.getValue().toString());
			}
		  
		}
		
		return theExtension;
	}
}
