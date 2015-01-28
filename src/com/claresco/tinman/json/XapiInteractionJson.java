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
import java.util.ArrayList;

import com.claresco.tinman.lrs.XapiInteraction;
import com.claresco.tinman.lrs.XapiInteractionComponent;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiInteractionJson.java
 *
 *
 * Status:
 * 	GREEN
 *
 *
 * @author rheza
 * on Mar 6, 2014
 * 
 */

public class XapiInteractionJson implements JsonDeserializer<XapiInteraction>{	
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiInteraction deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		JsonObject theInteractionJson = JsonUtility.convertJsonElementToJsonObject(arg0);
		
		String theType = JsonUtility.getElementAsString(theInteractionJson, "interactionType");
		ArrayList<String> theCorrectResponse = new ArrayList<String>();
		ArrayList<XapiInteractionComponent> theChoices = JsonUtility.handleInteraction
				(theInteractionJson, "choices", arg2);
		ArrayList<XapiInteractionComponent> theScale = JsonUtility.handleInteraction
				(theInteractionJson, "scale", arg2);
		ArrayList<XapiInteractionComponent> theTarget = JsonUtility.handleInteraction
				(theInteractionJson, "target", arg2);
		ArrayList<XapiInteractionComponent> theSource = JsonUtility.handleInteraction
				(theInteractionJson, "source", arg2);
		ArrayList<XapiInteractionComponent> theSteps = JsonUtility.handleInteraction
				(theInteractionJson, "steps", arg2);
		
		// Getting correct response pattern
		if(JsonUtility.hasElement(theInteractionJson, "correctResponsesPattern")){
			JsonArray theResponse = JsonUtility.getAnArray(theInteractionJson, "correctResponsesPattern");
			for(JsonElement element : theResponse){
				theCorrectResponse.add(element.getAsString());
			}
		}
		
		XapiInteraction theInteraction = new XapiInteraction(theType, theCorrectResponse, theChoices, theScale, 
				theTarget, theSource, theSteps);
		
		// This is to make sure the interaction type valid
		if(!theInteraction.isTypeValid()){
			throw new XapiBadInteractionException("Interaction type : " + theInteraction.getType() + " is not valid");
		}
		
		return theInteraction;
	}
	
	
	
	public static void main(String[] args) {
		Gson gson = JsonUtility.createGson();
		
		String path = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/Interaction.json";
		String path2 = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/Matching.json";
		 
		 try{
			 BufferedReader bf = new BufferedReader(new FileReader(path));
			 XapiInteraction s = gson.fromJson(bf, XapiInteraction.class);
			 
			 System.out.println(s);
			 
			 bf = new BufferedReader(new FileReader(path2));
			 s = gson.fromJson(bf, XapiInteraction.class);
			 System.out.println(s);
			 
		 }catch(Exception e){
			e.printStackTrace();
		 }
				
	}
}
