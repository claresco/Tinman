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

import com.claresco.tinman.lrs.XapiAccount;
import com.claresco.tinman.lrs.XapiInverseFunctionalIdentifier;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiInverseFuncIdDeserializer.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Jan 22, 2014
 * 
 */

public class XapiInverseFuncIdJson implements JsonDeserializer<XapiInverseFunctionalIdentifier>,
		JsonSerializer<XapiInverseFunctionalIdentifier>{

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiInverseFunctionalIdentifier arg0,
			Type arg1, JsonSerializationContext arg2) {
		JsonObject result = new JsonObject();
		if(arg0.hasMbox()){
			result.addProperty("mbox", arg0.getMbox().toString());
		}
		result.addProperty("mbox_sha1sum", arg0.getMboxSha1Sum());
		if(arg0.hasOpenId()){
			result.addProperty("openid", arg0.getOpenId().toString());
		}
		result.add("account", arg2.serialize(arg0.getAccount()));
		return result;		
	}
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiInverseFunctionalIdentifier deserialize(JsonElement arg0,
			Type arg1, JsonDeserializationContext arg2)
			throws JsonParseException {
		JsonObject localStatementPiece = JsonUtility.convertJsonElementToJsonObject(arg0);
		
		// Retrieve the elements that will construct the identifier
		String theEmailAddress = JsonUtility.getElementAsString(localStatementPiece, "mbox");
		String theOpenId = JsonUtility.getElementAsString(localStatementPiece, "openid");
		String theSha1Sum = JsonUtility.getElementAsString(localStatementPiece, "mbox_sha1sum");
		XapiAccount theAccount = JsonUtility.delegateDeserialization(arg2, localStatementPiece.get("account"),
				XapiAccount.class);

		if(theEmailAddress != null){
			if(!theEmailAddress.startsWith("mailto:")){
				throw new XapiBadIdentifierException("The mbox must start with \'mailto:\'");
			}
		}
		
		XapiInverseFunctionalIdentifier theIdentifier = new XapiInverseFunctionalIdentifier(theEmailAddress, 
				theSha1Sum, theOpenId, theAccount);
		
		// If every field in the identifier is null, return null instead
		if(theIdentifier.isEmpty()){
			return null;
		}
		
		// There are two types of identifier
		if(!theIdentifier.isValid()){
			throw new XapiBadIdentifierException();
		}
		
		// Return a new function identifier
		return theIdentifier;
	}	
	
}
