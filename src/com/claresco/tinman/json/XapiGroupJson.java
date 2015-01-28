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
import com.claresco.tinman.lrs.XapiGroup;
import com.claresco.tinman.lrs.XapiInverseFunctionalIdentifier;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiGroupDeserializer.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Jan 22, 2014
 * 
 */

public class XapiGroupJson implements JsonDeserializer<XapiGroup>, JsonSerializer<XapiGroup>{
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiGroup arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonObject result = new JsonObject();
		
		result.addProperty("name", arg0.getName());
		result.addProperty("objectType", arg0.getObjectType());
		
		XapiInverseFunctionalIdentifier theIdentifier = arg0.getInverseFuncId();
		
		if(theIdentifier.hasMbox()){
			result.addProperty("mbox", theIdentifier.getMbox().toString());
		}
		result.addProperty("mbox_sha1sum", theIdentifier.getMboxSha1Sum());
		if(theIdentifier.hasOpenId()){
			result.addProperty("openid", theIdentifier.getOpenId().toString());
		}
		result.add("account", arg2.serialize(theIdentifier.getAccount()));
		
		// Iterate through the member to create a jsonarray
		JsonArray memberArray = new JsonArray();
		for (XapiAgent a : arg0.getMember()) {
			memberArray.add(arg2.serialize(a));
		}
		result.add("member", memberArray);
		
		return result;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiGroup deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		JsonObject theJsonGroup = JsonUtility.convertJsonElementToJsonObject(arg0);
		
		String theName = JsonUtility.getElementAsString(theJsonGroup, "name");

		JsonArray theJsonMember = null;
		// Retrieve member as JsonArray
		if(theJsonGroup.has("member")){
			theJsonMember = theJsonGroup.get("member").getAsJsonArray();
		}
		
		XapiAgent[] theMember = null;
		
		// Initialize an array of agents
		if(theJsonMember != null){
			theMember = new XapiAgent[theJsonMember.size()]; 
			
			// Iterate through the JsonArray
			for(int i = 0; i < theMember.length; i++){
 				theMember[i] = JsonUtility.delegateDeserialization(arg2, theJsonMember.get(i), XapiAgent.class);
			}
		}
		
		
		XapiInverseFunctionalIdentifier theId = JsonUtility.delegateDeserialization(arg2, arg0, XapiInverseFunctionalIdentifier.class);
		
		XapiGroup theGroup = new XapiGroup(theName, theMember, theId);
		
		if(!theGroup.isValid()){
			throw new XapiBadGroupException("The group is not valid");
		}
		
		return theGroup;
	}
}
