/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiStatementResultJson.java	Jul 17, 2014
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

import com.claresco.tinman.lrs.XapiStatementBatch;
import com.claresco.tinman.lrs.XapiStatementResult;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * XapiStatementResultJson
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiStatementResultJson implements JsonSerializer<XapiStatementResult>{
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiStatementResult arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonObject result = new JsonObject();
		
		if(arg0.hasStatements()){
			result.add("statements", arg2.serialize(arg0.getStatements(), XapiStatementBatch.class));
		}
		
		if(arg0.hasMore()){
			result.addProperty("more", arg0.getMore().toString());
		}
		
		return result;
	}
}
