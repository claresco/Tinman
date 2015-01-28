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
import com.claresco.tinman.lrs.XapiActor;
import com.claresco.tinman.lrs.XapiAgent;
import com.claresco.tinman.lrs.XapiGroup;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiActorDeserializer.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Jan 21, 2014
 * 
 */

public class XapiActorJson implements JsonDeserializer<XapiActor>, JsonSerializer<XapiActor>{

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiActor arg0, Type arg1,
			JsonSerializationContext arg2) {
		return arg2.serialize(arg0);
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiActor deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		
		// Disassemble the who elements into elements mapped to its key
		JsonObject localActor = JsonUtility.convertJsonElementToJsonObject(arg0); 
		
		JsonElement objectTypeElement = JsonUtility.findJsonElementWithKey(localActor, "objectType");
				
		XapiActor tempActor = null;
		
		// Check if the actor is an agent or a group instead
		if (objectTypeElement != null){
			// objectType equals Group
			if (JsonUtility.elementEqualString(objectTypeElement, "Group")){
				tempActor = JsonUtility.delegateDeserialization(arg2, arg0, XapiGroup.class);
			}
			
			// objectType equals Agent
			else if (JsonUtility.elementEqualString(objectTypeElement, "Agent")){
				tempActor = JsonUtility.delegateDeserialization(arg2, arg0, XapiAgent.class);
			}
			else{
				// Statement should not be valid since a group always has to have an object type
				throw new XapiElementNotValidException("It has to be either agent or group");
			}
		}
		// element is null, should be an agent
		else{
			tempActor = JsonUtility.delegateDeserialization(arg2, arg0, XapiAgent.class);
		}
		
		return tempActor;
	}

	
}
