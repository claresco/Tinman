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

import org.joda.time.Duration;
import org.joda.time.Period;

import com.claresco.tinman.lrs.XapiExtension;
import com.claresco.tinman.lrs.XapiResult;
import com.claresco.tinman.lrs.XapiScore;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiResultJson.java
 *
 * Responsible from deserializing and serializing to and for XapiResult respectively
 *
 *
 *
 * @author rheza
 * on Mar 5, 2014
 * 
 */

public class XapiResultJson implements JsonSerializer<XapiResult>, JsonDeserializer<XapiResult>{

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiResult arg0, Type arg1,
			JsonSerializationContext arg2) {
		
		JsonObject result = new JsonObject();
		
		if(arg0.hasScore()){
			result.add("score", arg2.serialize(arg0.getScore(), XapiScore.class));
		}
		
		if(arg0.hasSuccess()){
			result.addProperty("success", arg0.getSuccess());
		}
		
		if(arg0.hasCompletion()){
			result.addProperty("completion", arg0.getCompletion());
		}
		
		if(arg0.hasResponse()){
			result.addProperty("response", arg0.getResponse());
		}
		
		if(arg0.hasDuration()){
			result.addProperty("duration", arg0.getDuration().toString());
		}
		
		if(arg0.hasExtension()){
			result.add("extensions", arg2.serialize(arg0.getExtension(), XapiExtension.class));
		}
				
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiResult deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		JsonObject theResult = JsonUtility.convertJsonElementToJsonObject(arg0);
		
		XapiScore theScore = JsonUtility.delegateDeserialization(arg2, JsonUtility.findJsonElementWithKey(theResult,
				"score"), XapiScore.class);
		Boolean theSuccess = JsonUtility.getElementAsBool(theResult, "success");
		Boolean theCompletion = JsonUtility.getElementAsBool(theResult, "completion");
		String theResponse = JsonUtility.getElementAsString(theResult, "response");
		String theStringDuration = JsonUtility.getElementAsString(theResult, "duration");
		
		Period thePeriodDuration = null;
		Duration theDuration = null;
		try{
			if(theStringDuration != null){
				thePeriodDuration = Period.parse(theStringDuration);
				theDuration = thePeriodDuration.toStandardDuration();
			}
		}catch(IllegalArgumentException e){
			throw new XapiBadResultException("Invalid Duration");
		}
		
		XapiExtension theExtension = null;
		if(JsonUtility.hasElement(theResult, "extensions")){
			theExtension = JsonUtility.delegateDeserialization(arg2, JsonUtility.get(theResult, "extensions"),
					XapiExtension.class);
		}
		
		XapiResult theResultObject = new XapiResult(theScore, theSuccess, theCompletion, theResponse, theDuration,
				theExtension);
		
		if(theResultObject.isEmpty()){
			return null;
		}
		
		return theResultObject;
	}
	
	public static void main(String[] args) {
		Gson gson = JsonUtility.createGson();
		
		String path = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/Result.json";
		 
		 try{
			 BufferedReader bf = new BufferedReader(new FileReader(path));
			 XapiResult r = gson.fromJson(bf, XapiResult.class);
			 
			 System.out.println(r);
			 
			 String json = gson.toJson(r);
			 System.out.println(json);
			 
		 }catch(Exception e){
			e.printStackTrace();
		 }
				
	}
}
