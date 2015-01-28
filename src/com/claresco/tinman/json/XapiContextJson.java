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
import java.util.UUID;

import com.claresco.tinman.lrs.XapiActor;
import com.claresco.tinman.lrs.XapiContext;
import com.claresco.tinman.lrs.XapiContextActivities;
import com.claresco.tinman.lrs.XapiExtension;
import com.claresco.tinman.lrs.XapiGroup;
import com.claresco.tinman.lrs.XapiStatementRef;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiContextJson.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Mar 5, 2014
 * 
 */

public class XapiContextJson implements JsonSerializer<XapiContext>, JsonDeserializer<XapiContext>{
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiContext arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonObject result = new JsonObject();
		
		if(arg0.hasInstructor()){
			result.add("instructor", arg2.serialize(arg0.getInstructor(), XapiActor.class));
		}
		
		if(arg0.hasTeam()){
			result.add("team", arg2.serialize(arg0.getTeam(), XapiGroup.class));
		}
		
		if(arg0.hasRegistration()){
			result.addProperty("registration", arg0.getRegistration().toString());
		}
		
		if(arg0.hasRevision()){
			result.addProperty("revision", arg0.getRevision());
		}
		
		if(arg0.hasPlatform()){
			result.addProperty("platform", arg0.getPlatform());
		}
		
		if(arg0.hasLanguage()){
			result.addProperty("language", arg0.getLanguage());
		}
		
		if(arg0.hasContextActivities()){
			result.add("contextActivities", arg2.serialize(arg0.getContextActivities(), XapiContextActivities.class));
		}
		
		if(arg0.hasStatementReference()){
			result.add("statement", arg2.serialize(arg0.getStatementReference(), XapiStatementRef.class));
		}
		
		return result;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiContext deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		JsonObject theContext = JsonUtility.convertJsonElementToJsonObject(arg0);
		JsonElement theElement;
		
		String theUUIDString = "";
		UUID theRegistration = null;
		if(JsonUtility.hasElement(theContext, "registration")){
			try{
				theUUIDString = JsonUtility.getElementAsString(theContext, "registration");
				theRegistration = UUID.fromString(theUUIDString);
			}catch(IllegalArgumentException e){
				String theMessage = "Bad UUID in context : ";
				if(theUUIDString == null){
					theMessage += "the UUID is null";
				}
				else if(theUUIDString.isEmpty()){
					theMessage += "the UUID is empty";
				}
				else{
					theMessage += "the UUID is invalid ";
				}
				throw new XapiBadUUIDException(theMessage);
			}
		} 
		
		XapiActor theInstructor = null;
		if(JsonUtility.hasElement(theContext, "instructor")){
			theElement = JsonUtility.get(theContext, "instructor");
			theInstructor = JsonUtility.delegateDeserialization(arg2, theElement, XapiActor.class);
		}
		
		XapiGroup theTeam = null;
		if(JsonUtility.hasElement(theContext, "team")){
			theElement = JsonUtility.get(theContext, "team");
			theTeam = JsonUtility.delegateDeserialization(arg2, theElement, XapiGroup.class);
		}
		
		String theRevision = JsonUtility.getElementAsString(theContext, "revision");
		String thePlatform = JsonUtility.getElementAsString(theContext, "platform");
		String theLanguage = JsonUtility.getElementAsString(theContext, "language");
		
		XapiContextActivities theContextActivities = null;
		if(JsonUtility.hasElement(theContext, "contextActivities")){
			theContextActivities = JsonUtility.delegateDeserialization(arg2, theContext.get("contextActivities"),
					XapiContextActivities.class);
		}
		
		XapiStatementRef theStatementReference = null;
		if(JsonUtility.hasElement(theContext, "statement")){
			theElement = JsonUtility.get(theContext, "statement");
			theStatementReference = JsonUtility.delegateDeserialization(arg2, theElement, XapiStatementRef.class);
		}
		
		XapiExtension theExtension = null;
		if(JsonUtility.hasElement(theContext, "extensions")){
			theElement = JsonUtility.get(theContext, "extensions");
			theExtension = JsonUtility.delegateDeserialization(arg2, theElement, XapiExtension.class);
		}
		
		return new XapiContext(theInstructor, theTeam, theRegistration, theRevision, theLanguage, thePlatform,
				theContextActivities, theStatementReference, theExtension);
	}
	
	
	
	public static void main(String[] args) {
		Gson gson = JsonUtility.createGson();
		
		String path = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/Context.json";
		 
		 try{
			 BufferedReader bf = new BufferedReader(new FileReader(path));
			 XapiContext s = gson.fromJson(bf, XapiContext.class);
			 
			 System.out.println(s);
			 
			 String json = gson.toJson(s);
			 System.out.println(json);
			 
		 }catch(Exception e){
			e.printStackTrace();
		 }
				
	}
}
