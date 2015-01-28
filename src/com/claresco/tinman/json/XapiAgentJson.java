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

import com.claresco.tinman.lrs.XapiAgent;
import com.claresco.tinman.lrs.XapiInverseFunctionalIdentifier;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiAgentDeserializer.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Jan 22, 2014
 * 
 */

public class XapiAgentJson implements JsonDeserializer<XapiAgent>, JsonSerializer<XapiAgent>{
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiAgent arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonObject result = new JsonObject();
		
		if(arg0.hasName()){
			result.addProperty("name", arg0.getName());
		}
		
		result.addProperty("objectType", arg0.getObjectType());
		
		XapiInverseFunctionalIdentifier theIdentifier = arg0.getInverseFuncId();
		
		if(theIdentifier.hasMbox()){
			result.addProperty("mbox", theIdentifier.getMbox().toString());
		}
		
		if(theIdentifier.hasMboxSha1Sum()){
			result.addProperty("mbox_sha1sum", theIdentifier.getMboxSha1Sum());
		}
		
		if(theIdentifier.hasOpenId()){
			result.addProperty("openid", theIdentifier.getOpenId().toString());
		}
		
		if(theIdentifier.hasAccount()){
			result.add("account", arg2.serialize(theIdentifier.getAccount()));
		}
		
		return result;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiAgent deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		
		JsonObject localJsonObject = JsonUtility.convertJsonElementToJsonObject(arg0);
		
		// This to handles if the member of a group is another group
		if(localJsonObject.has("objectType")){
			if(JsonUtility.getElementAsString(localJsonObject, "objectType").equals("Group")){
				throw new XapiBadGroupException("A group must not have another group as its member");
			}
		}
		
		// Find the name of the agent
		String theName = JsonUtility.getElementAsString(localJsonObject, "name"); 
		
		// Delegate the inverse functional identifier
		XapiInverseFunctionalIdentifier theID = JsonUtility.delegateDeserialization(arg2, arg0, XapiInverseFunctionalIdentifier.class);
		
		XapiAgent theAgent = new XapiAgent(theName, theID);
		
		// Check if there is indeed an identifier
		if(!theAgent.isValid()){
			throw new XapiBadAgentException("Identifier must not be null");
		}
		
		return theAgent;
	}
	
}
