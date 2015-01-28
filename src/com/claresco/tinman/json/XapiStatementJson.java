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
import com.claresco.tinman.lrs.XapiObject;
import com.claresco.tinman.lrs.XapiResult;
import com.claresco.tinman.lrs.XapiStatement;
import com.claresco.tinman.lrs.XapiVerb;
import com.google.gson.*;


/**
 * XapiStatementDeserializer.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Jan 21, 2014
 * 
 */

public class XapiStatementJson implements JsonDeserializer<XapiStatement>, JsonSerializer<XapiStatement>{

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiStatement arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonObject result = new JsonObject();

		result.addProperty("id", arg0.getId());
		
		result.add("actor", arg2.serialize(arg0.getActor()));
		result.add("verb", arg2.serialize(arg0.getVerb()));
		result.add("object", arg2.serialize(arg0.getObject()));
		
		if(arg0.hasResult()){
			result.add("result", arg2.serialize(arg0.getResult()));
		}
		
		if(arg0.hasContext()){
			result.add("context", arg2.serialize(arg0.getContext()));
		}
		
		if(arg0.hasTimeStamp()){
			result.addProperty("timestamp", arg0.getTimeStampAsString());
		}
		
		return result;
	}

	
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiStatement deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		final JsonObject theStatementJSON = JsonUtility.convertJsonElementToJsonObject(arg0);
		
		UUID theUUID = null;
		if(JsonUtility.hasElement(theStatementJSON, "id")){
			String theStatementID = JsonUtility.getElementAsString(theStatementJSON, "id");
			theUUID = UUID.fromString(theStatementID);
		}
		
		// Retrieve actor, verb, and object
		XapiActor theActor = null;
		XapiVerb theVerb = null;
		XapiObject theObject = null;
		XapiResult theResult = null;
		XapiContext theContext = null;
		String theTimeStamp = null;
		
		// Get actor
		if(JsonUtility.hasElement(theStatementJSON, "actor")){
			theActor = JsonUtility.delegateDeserialization(arg2, theStatementJSON.get("actor"), XapiActor.class);
		}
			
		// Get verb
		if(JsonUtility.hasElement(theStatementJSON, "verb")){
			theVerb = JsonUtility.delegateDeserialization(arg2, theStatementJSON.get("verb"), XapiVerb.class);
		}
		
		// Get object
		if(JsonUtility.hasElement(theStatementJSON, "object")){
			theObject = JsonUtility.delegateDeserialization(arg2, theStatementJSON.get("object"), XapiObject.class);
		}
		
		// Get result
		if(JsonUtility.hasElement(theStatementJSON, "result")){
			theResult = JsonUtility.delegateDeserialization(arg2, theStatementJSON.get("result"), XapiResult.class);
		}
		
		// Get context
		if(JsonUtility.hasElement(theStatementJSON, "context")){
			theContext = JsonUtility.delegateDeserialization(arg2, theStatementJSON.get("context"), XapiContext.class);
		}
		
		// Get timestamp
		if(JsonUtility.hasElement(theStatementJSON, "timestamp")){
			theTimeStamp = JsonUtility.getElementAsString(theStatementJSON, "timestamp");
		}
		
		XapiStatement theStatement = new XapiStatement(theUUID, theActor, theVerb, theObject, theResult,
				theContext, theTimeStamp); 
		
		// Check if the either actor, verb, or object is missing
		if(!theStatement.isValid()){
			throw new XapiMissingElementException("Missing at least one of actor, verb, or object");
		}
		
		return theStatement;
	}
	
	
	
	public static void main(String[] args) {
		Gson gson = JsonUtility.createGson();
		
		String path = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/Statement9.json";
		 
		 try{
			 BufferedReader bf = new BufferedReader(new FileReader(path));
			 XapiStatement s = gson.fromJson(bf, XapiStatement.class);
			 
			 System.out.println(s);
			 
		 }catch(Exception e){
			e.printStackTrace();
		 }
				
	}
	
	
	
}
