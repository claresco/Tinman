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

import com.claresco.tinman.lrs.XapiStatementRef;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiStatementRefDeserializer.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Jan 27, 2014
 * 
 */

public class XapiStatementRefJson implements JsonDeserializer<XapiStatementRef>, JsonSerializer<XapiStatementRef>{
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiStatementRef arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonObject result = new JsonObject();
		
		result.addProperty("objectType", arg0.getObjectType());
		result.addProperty("id", arg0.getId().toString());
		
		return result;
	}

	
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiStatementRef deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		JsonObject theStatementRef = JsonUtility.convertJsonElementToJsonObject(arg0);
		
		String theId = JsonUtility.getElementAsString(theStatementRef, "id");
		
		if(theId == null){
			throw new XapiBadStatementReferenceException("Can not find reference id");
		}
		
		return new XapiStatementRef(theId);
	}
	
}
