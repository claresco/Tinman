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
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.atlas.json.JsonValue;

import com.claresco.tinman.lrs.XapiActivityDefinition;
import com.claresco.tinman.lrs.XapiAgent;
import com.claresco.tinman.lrs.XapiExtension;
import com.claresco.tinman.lrs.XapiIRI;
import com.claresco.tinman.lrs.XapiInteraction;
import com.claresco.tinman.lrs.XapiInverseFunctionalIdentifier;
import com.claresco.tinman.lrs.XapiLanguageMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiActivityDefinitionDeserializer.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Jan 27, 2014
 * 
 */

public class XapiActivityDefinitionJson implements JsonDeserializer<XapiActivityDefinition>, JsonSerializer<XapiActivityDefinition>{
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiActivityDefinition arg0, Type arg1,
			JsonSerializationContext arg2) {
		if(arg0.isEmpty()){
			return null;
		}
		
		JsonObject result = new JsonObject();
		
		if(arg0.hasName()){
			result.add("name", arg2.serialize(arg0.getName(), XapiLanguageMap.class));
		}
		
		if(arg0.hasDescription()){
			result.add("description", arg2.serialize(arg0.getDescription(), XapiLanguageMap.class));
		}
		
		if(arg0.hasType()){
			result.addProperty("type", arg0.getType().toString());
		}
		
		if(arg0.hasInteractionProperties()){
			XapiInteraction theInteraction = arg0.getInteractionProperties();
			if(theInteraction.hasChoices()){
				result.add("choices", JsonUtility.jsonArrayFromXapiInteractionComponent(theInteraction.getChoices(), arg2));
			}
			if(theInteraction.hasScale()){
				result.add("scale", JsonUtility.jsonArrayFromXapiInteractionComponent(theInteraction.getScale(), arg2));
			}
			if(theInteraction.hasTarget()){
				result.add("target", JsonUtility.jsonArrayFromXapiInteractionComponent(theInteraction.getTarget(), arg2));
			}
			if(theInteraction.hasSource()){
				result.add("source", JsonUtility.jsonArrayFromXapiInteractionComponent(theInteraction.getSource(), arg2));
			}
			if(theInteraction.hasSteps()){
				result.add("steps", JsonUtility.jsonArrayFromXapiInteractionComponent(theInteraction.getSteps(), arg2));
			}
			if(theInteraction.hasCorrectReponse()){
				ArrayList<String> theCorrectResponses = theInteraction.getCorrectResponse();
				JsonArray theCorrectResponseArray = new JsonArray();
				for(String correctResponse : theCorrectResponses){
					theCorrectResponseArray.add(new JsonPrimitive(correctResponse));
				}
				result.add("correctResponsesPattern", theCorrectResponseArray);
			}
			if(theInteraction.hasType()){
				result.addProperty("interactionType", theInteraction.getType());
			}
		}
		
		if(arg0.hasExtension()){
			XapiExtension theExt = arg0.getExtension();
			JsonObject theExtJson = new JsonObject();
			for(String key : theExt.getKeys()){
				theExtJson.addProperty(key, theExt.getValueOf(key));
			}
			
			result.add("extensions", theExtJson);
		}
				
		return result;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiActivityDefinition deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		if(JsonUtility.isJsonElementNull(arg0)){
			return null;
		}
		
		JsonObject theActivityDefinition = JsonUtility.convertJsonElementToJsonObject(arg0);
		
		// Construct the name
		XapiLanguageMap theName = JsonUtility.constructLanguageMap(theActivityDefinition, "name");
		
		// Construct the description
		XapiLanguageMap theDescription = JsonUtility.constructLanguageMap(theActivityDefinition, "description");
		
		String theType = JsonUtility.getElementAsString(theActivityDefinition, "type");
		String theMoreInfo = JsonUtility.getElementAsString(theActivityDefinition, "moreInfo"); 
		
		XapiInteraction theInteraction = null;
		XapiExtension theExtension = null;
		
		// If activity has interaction, delegate it
		if(theType != null && JsonUtility.isActivityOfTypeInteraction(theType)){
			theInteraction = JsonUtility.delegateDeserialization(arg2, arg0, XapiInteraction.class);
		}
		
		// Check for extension
		if(JsonUtility.hasElement(theActivityDefinition, "extensions")){
			JsonElement theExtJson = JsonUtility.get(theActivityDefinition, "extensions");
			JsonObject extJsonMap = JsonUtility.convertJsonElementToJsonObject(theExtJson);
			theExtension = new XapiExtension();
			for (Map.Entry<String,JsonElement> entry : extJsonMap.entrySet()) {
				JsonElement theValue = entry.getValue();
				if(theValue.isJsonPrimitive()){
					theExtension.add(entry.getKey(), entry.getValue().getAsString());
				}else{
					theExtension.add(entry.getKey(), entry.getValue().toString());
				}
			  
			}
		}
		
		if (theExtension != null && theExtension.isEmpty()) {
			return new XapiActivityDefinition(theName, theDescription, theType, theMoreInfo, theInteraction, null);
		}
		
		return new XapiActivityDefinition(theName, theDescription, theType, theMoreInfo, theInteraction, theExtension);
	}
	
	
	public static void main(String[] args) {
		Gson gson = JsonUtility.createGson();
		
		String path = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/Interaction.json";
		String path2 = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/Matching.json";
		 
		 try{
			 BufferedReader bf = new BufferedReader(new FileReader(path));
			 XapiActivityDefinition ac = gson.fromJson(bf, XapiActivityDefinition.class);
			 
			 System.out.println(ac);
			 
			 String json = gson.toJson(ac);
			 System.out.println(json);
			 
			 bf = new BufferedReader(new FileReader(path2));
			 ac = gson.fromJson(bf, XapiActivityDefinition.class);
			 System.out.println(ac);
			 
			 json = gson.toJson(ac);
			 System.out.println(json);
			 
		 }catch(Exception e){
			e.printStackTrace();
		 }
	}
	
}
