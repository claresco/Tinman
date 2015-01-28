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
import java.util.UUID;

import com.claresco.tinman.lrs.XapiActor;
import com.claresco.tinman.lrs.XapiObject;
import com.claresco.tinman.lrs.XapiStatement;
import com.claresco.tinman.lrs.XapiSubStatement;
import com.claresco.tinman.lrs.XapiVerb;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiSubStatementDeserializer.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Jan 27, 2014
 * 
 */

public class XapiSubStatementJson implements JsonDeserializer<XapiSubStatement> , JsonSerializer<XapiSubStatement> {

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiSubStatement arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonObject result = new JsonObject();
		
		result.add("actor", arg2.serialize(arg0.getActor()));
		result.add("verb", arg2.serialize(arg0.getVerb()));
		result.add("object", arg2.serialize(arg0.getObject()));
		
		return result;
	}

	
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiSubStatement deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		JsonObject theJsonSubStatement = JsonUtility.convertJsonElementToJsonObject(arg0);
		
		// check if there are invalid elements: id, stored, version, authority
		if(JsonUtility.getElementAsString(theJsonSubStatement, "id") != null){
			throw new XapiBadSubStatementException("Should not have an id");
		}
		
		if(JsonUtility.getElementAsString(theJsonSubStatement, "version") != null){
			throw new XapiBadSubStatementException("Should not have a version");
		}
		
		if(JsonUtility.getElementAsString(theJsonSubStatement, "authority") != null){
			throw new XapiBadSubStatementException("Should not have an authority");
		}
		
		// Retrieve actor, verb, and object
		XapiActor theActor;
		XapiVerb theVerb;
		XapiObject theObject;
		
		
		// Fix this, when it is null. do something else	
		if(JsonUtility.hasElement(theJsonSubStatement, "actor")){
			theActor = JsonUtility.delegateDeserialization(arg2, theJsonSubStatement.get("actor"), XapiActor.class);
		} else{
			theActor = null;
		}
		
		if(JsonUtility.hasElement(theJsonSubStatement, "verb")){
			theVerb = JsonUtility.delegateDeserialization(arg2, theJsonSubStatement.get("verb"), XapiVerb.class);
		}
		else{
			theVerb = null;
		}
		
		if(JsonUtility.hasElement(theJsonSubStatement, "object")){
			theObject = JsonUtility.delegateDeserialization(arg2, theJsonSubStatement.get("object"), XapiObject.class);
			if(theObject.getObjectType().equals("SubStatement")){
				throw new XapiBadSubStatementException("The substatement must not contain another substatement");
			}
			if(theObject instanceof XapiSubStatement){
				throw new XapiBadSubStatementException("The substatement must not contain another substatement");
			}
		} else{
			theObject = null;
		}
		
		// Add the rest later on
		XapiSubStatement theSubStatement = new XapiSubStatement(theActor, theVerb, theObject);
		
		if(!theSubStatement.isValid()){
			throw new XapiBadSubStatementException("Substatement missing at least one of the actor, verb, or object");
		}
		
		return new XapiSubStatement(theActor, theVerb, theObject);
	}
}
