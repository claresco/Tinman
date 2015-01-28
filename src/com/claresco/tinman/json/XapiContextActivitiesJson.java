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
import java.util.ArrayList;
import java.util.Arrays;

import com.claresco.tinman.lrs.XapiActivity;
import com.claresco.tinman.lrs.XapiContextActivities;
import com.claresco.tinman.lrs.XapiScore;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sun.org.apache.bcel.internal.generic.NEWARRAY;

/**
 * XapiContextActivitiesJson.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Mar 5, 2014
 * 
 */

public class XapiContextActivitiesJson implements JsonSerializer<XapiContextActivities>, JsonDeserializer<XapiContextActivities>{
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(XapiContextActivities arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonObject result = new JsonObject();
		
		JsonArray theParentArray = new JsonArray();
		if(arg0.hasParent()){
			for(XapiActivity activity : arg0.getParent()){
				theParentArray.add(arg2.serialize(activity));
			}
			result.add("parent", theParentArray);
		}

		
		JsonArray theGroupingArray = new JsonArray();
		if(arg0.hasGrouping()){
			for(XapiActivity activity : arg0.getGrouping()){
				theGroupingArray.add(arg2.serialize(activity));
			}
			result.add("grouping", theGroupingArray);
		}
		
		if(arg0.hasCategory()){
			JsonArray theCategoryArray = serialize(arg0.getCategory(), arg2);
			if(theCategoryArray != null){
				result.add("category", theCategoryArray);
			}
		}
		
		if(arg0.hasOther()){
			JsonArray theOtherArray = serialize(arg0.getOther(), arg2);
			if(theOtherArray != null){
				result.add("other", theOtherArray);
			}
		}
		
		return result;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public XapiContextActivities deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		// TODO Auto-generated method stub
		JsonObject theContextActivities = JsonUtility.convertJsonElementToJsonObject(arg0);
		
		ArrayList<XapiActivity> theParent = new ArrayList<XapiActivity>();
		ArrayList<XapiActivity> theGrouping = new ArrayList<XapiActivity>();
		ArrayList<XapiActivity> theCategory = new ArrayList<XapiActivity>();
		ArrayList<XapiActivity> theOther = new ArrayList<XapiActivity>();
		
		theParent = JsonUtility.convertJsonArrayOfActivities(theContextActivities, "parent", theParent, arg2);
		
		theGrouping = JsonUtility.convertJsonArrayOfActivities(theContextActivities, "grouping", theGrouping, arg2);
		
		theCategory = JsonUtility.convertJsonArrayOfActivities(theContextActivities, "category", theCategory, arg2);
		
		theOther = JsonUtility.convertJsonArrayOfActivities(theContextActivities, "other", theOther, arg2);

		if(theParent.isEmpty()){
			theParent = null;
		}
		
		if(theGrouping.isEmpty()){
			theGrouping = null;
		}
		
		if(theCategory.isEmpty()){
			theCategory = null;
		}
		
		if(theOther.isEmpty()){
			theOther = null;
		}
		
		XapiContextActivities myContextActivities = new XapiContextActivities(theParent, theGrouping, theCategory, theOther);
		
		if(myContextActivities.isEmpty()){
			return null;
		}
		
		return myContextActivities;
	}
	
	
	
	private JsonArray serialize(ArrayList<XapiActivity> theActivityArray, JsonSerializationContext theSerializationContext){
		JsonArray theArray = new JsonArray();
		for(XapiActivity activity : theActivityArray){
			theArray.add(theSerializationContext.serialize(activity, XapiActivity.class));
		}
		if(theArray.size() == 0){
			return null;
		}
		return theArray;
	}
	
	
	public static void main(String[] args) {
		Gson gson = JsonUtility.createGson();
		
		String path = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/ContextActivities.json";
		 
		 try{
			 BufferedReader bf = new BufferedReader(new FileReader(path));
			 XapiContextActivities s = gson.fromJson(bf, XapiContextActivities.class);
			 
			 System.out.println(s);
			 
			 System.out.println(gson.toJson(s));
			 
		 }catch(Exception e){
			e.printStackTrace();
		 }
				
	}
}
