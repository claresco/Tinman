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

import org.apache.http.message.BufferedHeader;

import com.claresco.tinman.lrs.XapiAccount;
import com.claresco.tinman.lrs.XapiScore;
import com.claresco.tinman.lrs.XapiStatement;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiScoreJson.java
 *
 * Responsible from deserializing and serializing to and for XapiScore respectively
 *
 *
 *
 * @author rheza
 * on Mar 5, 2014
 * 
 */

public class XapiScoreJson implements JsonDeserializer<XapiScore>, JsonSerializer<XapiScore>{

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiScore arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonObject result = new JsonObject();
		if(arg0.hasScaledScore()){
			result.addProperty("scaled", arg0.getScaledScore().toString());
		}
		if(arg0.hasRawScore()){
			result.addProperty("raw", arg0.getRawScore().toString());
		}
		if(arg0.hasMinScore()){
			result.addProperty("min", arg0.getMinScore().toString());
		}
		if(arg0.hasMaxScore()){
			result.addProperty("max", arg0.getMaxScore().toString());
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiScore deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {

		JsonObject theScoreJSON = JsonUtility.convertJsonElementToJsonObject(arg0);

		// Find the name of the agent
		Double theScaled = JsonUtility.getElementAsDouble(theScoreJSON, "scaled");
		Double theRaw = JsonUtility.getElementAsDouble(theScoreJSON, "raw");
		Double theMin = JsonUtility.getElementAsDouble(theScoreJSON, "min");
		Double theMax = JsonUtility.getElementAsDouble(theScoreJSON, "max");
		
		XapiScore theScore = new XapiScore(theScaled, theRaw, theMin, theMax);
		
		if(!theScore.isValid()){
			throw new XapiBadResultException("Score is not valid");
		}

		return theScore;
	}
	
	public static void main(String[] args) {
		Gson gson = JsonUtility.createGson();
		
		String path = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/score.json";
		 
		 try{
			 BufferedReader bf = new BufferedReader(new FileReader(path));
			 XapiScore s = gson.fromJson(bf, XapiScore.class);
			 
			 System.out.println(s);
			 
			 XapiScore theS = new XapiScore(0.3, 3, 0, 10);
			 
			 String json = gson.toJson(theS);
			 System.out.println(json);
			 
		 }catch(Exception e){
			e.printStackTrace();
		 }
				
	}
	
}
