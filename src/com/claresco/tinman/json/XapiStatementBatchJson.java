/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiStatementBatchJson.java	Jun 11, 2014
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

import com.claresco.tinman.lrs.XapiStatement;
import com.claresco.tinman.lrs.XapiStatementBatch;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiStatementBatchJson
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiStatementBatchJson implements JsonDeserializer<XapiStatementBatch>, JsonSerializer<XapiStatementBatch>{
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiStatementBatch arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonArray theStatementArray = new JsonArray();
		
		if(arg0.size() == 1){
			JsonElement theStatement = arg2.serialize(arg0.getStatementAtIndex(0), XapiStatement.class);
			theStatementArray.add(theStatement);
			
			return theStatementArray;
		}else {
			for(XapiStatement s : arg0){
				theStatementArray.add(arg2.serialize(s, XapiStatement.class));
			}
			return theStatementArray;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiStatementBatch deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		
		XapiStatementBatch theBatch = new XapiStatementBatch();
		
		// if it is not an array of statement
		if(!arg0.isJsonArray()){
			XapiStatement theStatement = JsonUtility.delegateDeserialization(arg2, arg0, XapiStatement.class);
			
			theBatch.addStatementToBatch(theStatement);
		}else{
			JsonArray statementArrayJson = arg0.getAsJsonArray();
			
			for(JsonElement element : statementArrayJson){
				XapiStatement theStatement = JsonUtility.delegateDeserialization(arg2, element, XapiStatement.class);
				theBatch.addStatementToBatch(theStatement);
			}
		}
			
		return theBatch;
	}
}
