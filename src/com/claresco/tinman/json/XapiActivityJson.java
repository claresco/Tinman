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

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Type;

import com.claresco.tinman.lrs.XapiActivity;
import com.claresco.tinman.lrs.XapiActivityDefinition;
import com.claresco.tinman.lrs.XapiAgent;
import com.claresco.tinman.lrs.XapiInverseFunctionalIdentifier;
import com.claresco.tinman.lrs.XapiStatement;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.Gson;

/**
 * XapiActivityDeserializer.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Jan 27, 2014
 * 
 */

public class XapiActivityJson implements JsonDeserializer<XapiActivity>, JsonSerializer<XapiActivity>{

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiActivity arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonObject result = new JsonObject();
		
		result.addProperty("id", arg0.getId().toString());
		result.addProperty("objectType", arg0.getObjectType());
		
		JsonElement theDefinition = arg2.serialize(arg0.getDefinition(), XapiActivityDefinition.class);
		if(theDefinition != null){
			result.add("definition", theDefinition);
		}
		
		return result;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiActivity deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		
		JsonObject theActivity = JsonUtility.convertJsonElementToJsonObject(arg0);
		
		String theId;
		
		// check if id is there, it is required
		if(!JsonUtility.hasElement(theActivity, "id")){
			throw new XapiBadActivityException("Activity must have an id");
		}
		
		theId  = JsonUtility.getElementAsString(theActivity, "id");
		
		XapiActivityDefinition theActivityDefinition = JsonUtility.delegateDeserialization(arg2, theActivity.get("definition"),
				XapiActivityDefinition.class);
		
		return new XapiActivity(theId, theActivityDefinition);
	}
	
	
	
	public static void main(String[] args) {
		Gson gson = JsonUtility.createGson();
		
		 String path1 = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/ActivityDatabaseTesting.json";
		 
		 try{
			 BufferedReader bf = new BufferedReader(new FileReader(path1));
			 XapiStatement s = gson.fromJson(bf, XapiStatement.class);
			 
			 System.out.println(gson.toJson(s.getObject()));
		 } catch(Exception e){
			 e.printStackTrace();
		 } finally{
		 }
	}
}
