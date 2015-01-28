/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiPersonJson.java	Jul 11, 2014
 *
 * Copyright 2014 Claresco Corporation, Berkeley, CA 94704. All Rights Reserved.
 *
 * This software is the proprietary information of Claresco Corporation.
 * Use is subject to license terms.
 *
 * Author : Rheza
 *
 */
package com.claresco.tinman.json;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.claresco.tinman.lrs.XapiAccount;
import com.claresco.tinman.lrs.XapiActor;
import com.claresco.tinman.lrs.XapiIRI;
import com.claresco.tinman.lrs.XapiPerson;
import com.claresco.tinman.lrs.XapiStatement;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiPersonJson
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiPersonJson implements JsonDeserializer<XapiPerson>, JsonSerializer<XapiPerson>{
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiPerson arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonObject theResult = new JsonObject();
		
		theResult.addProperty("objectType", "Person");
		
		if(arg0.hasNames()){
			JsonArray theNamesJson = JsonUtility.convertToJsonArray(arg0.getNames());
			
			if(theNamesJson.size() > 0){
				theResult.add("name", theNamesJson);
			}
		}
		
		
		if(arg0.hasMboxes()){
			JsonArray theMboxesJson = JsonUtility.convertToJsonArrayFromIRIList(arg0.getMboxes());
			
			if(theMboxesJson.size() > 0){
				theResult.add("mbox", theMboxesJson);
			}
			
		}
		
		
		if(arg0.hasMboxSha1sums()){
			JsonArray theMboxSha1sumsJson = JsonUtility.convertToJsonArray(arg0.getMboxSha1sums());

			if(theMboxSha1sumsJson.size() > 0){
				theResult.add("mbox_sha1sum", theMboxSha1sumsJson);
			}
		}
		
		
		
		if(arg0.hasOpendIDs()){
			JsonArray theOpenIDsJson = JsonUtility.convertToJsonArray(arg0.getOpenIDs());
			
			if(theOpenIDsJson.size() > 0){
				theResult.add("openid", theOpenIDsJson);
			}
		}
		
		
		
		if(arg0.hasAccounts()){
			ArrayList<XapiAccount> theAccounts = arg0.getAccounts();
			
			JsonArray theAccountsJson = new JsonArray();
			
			for(XapiAccount a : theAccounts){
				theAccountsJson.add(arg2.serialize(a, XapiAccount.class));
			}
			
			if(theAccountsJson.size() > 0){
				theResult.add("account", theAccountsJson);
			}
		}
		
		
		return theResult;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiPerson deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {

		if(arg0.isJsonObject()){
			JsonObject theJsonObject = JsonUtility.convertJsonElementToJsonObject(arg0);
			ArrayList<String> theName = fillTheList("name", theJsonObject);
			ArrayList<String> theMboxSha1sum = fillTheList("mbox_sha1sum", theJsonObject);
			ArrayList<String> theOpenid = fillTheList("openid", theJsonObject);
			ArrayList<XapiIRI> theMbox = new ArrayList<XapiIRI>();
			ArrayList<XapiAccount> theAccounts = new ArrayList<XapiAccount>();
			
			JsonArray theArray;
			
			if(JsonUtility.hasElement(theJsonObject, "mbox")){
				
				theArray = JsonUtility.getAnArray(theJsonObject, "mbox");
				for(JsonElement e : theArray){
					if(!e.isJsonNull()){
						String theEmailAddress = e.getAsString();
						if(!theEmailAddress.startsWith("mailto:")){
							throw new XapiBadIdentifierException("Mbox has to start with \'mailto:\'");
						}
						theMbox.add(new XapiIRI(e.getAsString()));
					}
				}
			}
			
			if(JsonUtility.hasElement(theJsonObject, "account")){
				theArray = JsonUtility.getAnArray(theJsonObject, "account");
				for(JsonElement e : theArray){
					theAccounts.add((XapiAccount) JsonUtility.delegateDeserialization(arg2, e, XapiAccount.class));
				}
			}
			
			return new XapiPerson(theName, theMbox, theMboxSha1sum, theOpenid, theAccounts);
			
		}else{
			throw new XapiBadPersonException("XapiPerson should be a JSON object");
		}
	}
	
	
	
	private ArrayList<String> fillTheList(String key, JsonObject theObject){
		ArrayList<String> theList = new ArrayList<String>();
		
		if(JsonUtility.hasElement(theObject, key)){
			JsonElement theElement = JsonUtility.get(theObject, key);
			if(theElement.isJsonArray()){
				JsonArray theArray = JsonUtility.getAnArray(theObject, key);
				for(JsonElement e : theArray){
					theList.add(e.getAsString());
				}
			}
		}
		
		return theList;
	}
}
