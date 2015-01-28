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

import com.claresco.tinman.lrs.XapiStatement;
import com.claresco.tinman.lrs.XapiActivity;
import com.claresco.tinman.lrs.XapiAgent;
import com.claresco.tinman.lrs.XapiGroup;
import com.claresco.tinman.lrs.XapiObject;
import com.claresco.tinman.lrs.XapiStatementRef;
import com.claresco.tinman.lrs.XapiSubStatement;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiObjectDeserializer.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Jan 24, 2014
 * 
 */

public class XapiObjectJson implements JsonDeserializer<XapiObject>, JsonSerializer<XapiObject>{
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiObject arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonObject result = new JsonObject();
		
		if(arg0.getObjectType().equals("Agent")){
			result = (JsonObject) arg2.serialize(arg0, XapiAgent.class);
		} else if(arg0.getObjectType().equals("Group")){
			result = (JsonObject) arg2.serialize(arg0, XapiGroup.class);
		} else if(arg0.getObjectType().equals("Activity")){
			result = (JsonObject) arg2.serialize(arg0, XapiActivity.class);
		} else if(arg0.getObjectType().equals("StatementRef")){
			result = (JsonObject) arg2.serialize(arg0, XapiStatementRef.class);
		} else if(arg0.getObjectType().equals("SubStatement")){
			result = (JsonObject) arg2.serialize(arg0, XapiSubStatement.class);
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiObject deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		// TODO Auto-generated method stub
		
		JsonObject theObjectJson = JsonUtility.convertJsonElementToJsonObject(arg0);
		
		String theObjectType = JsonUtility.getElementAsString(theObjectJson, "objectType");
		
		XapiObject theObject = null;
		
		if (theObjectType != null){
			if (theObjectType.equals("Agent")){
				theObject = JsonUtility.delegateDeserialization(arg2, arg0, XapiAgent.class);
			}else if (theObjectType.equals("Group")){
				theObject = JsonUtility.delegateDeserialization(arg2, arg0, XapiGroup.class);
			}else if (theObjectType.equals("StatementRef")){
				theObject = JsonUtility.delegateDeserialization(arg2, arg0, XapiStatementRef.class);
			}else if (theObjectType.equals("SubStatement")){
				theObject = JsonUtility.delegateDeserialization(arg2, arg0, XapiSubStatement.class);
			}else if (theObjectType.equals("Activity")){
				theObject = JsonUtility.delegateDeserialization(arg2, arg0, XapiActivity.class);
			}else{
				throw new XapiElementNotValidException("There is no object with the type " + theObjectType);
			}
		}else{
			// This should be an activity
			theObject = JsonUtility.delegateDeserialization(arg2, arg0, XapiActivity.class);
		}
		
		return theObject;
	}
	
	public static void main(String[] args) {
		Gson gson = JsonUtility.createGson();
		
		 String path1 = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/SubStatementDatabaseTesting.json";
		 
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
