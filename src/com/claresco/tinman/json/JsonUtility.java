/**
 * ClarescoExperienceAPI
 * Copyright 
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General protected License version 2 only, as
 * published by the Free Software Foundation.
 *
 * Please contact Claresco, www.claresco.com, if you have any questions.
 **/

package com.claresco.tinman.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import com.claresco.tinman.lrs.XapiAccount;
import com.claresco.tinman.lrs.XapiActivity;
import com.claresco.tinman.lrs.XapiActivityDefinition;
import com.claresco.tinman.lrs.XapiActor;
import com.claresco.tinman.lrs.XapiAgent;
import com.claresco.tinman.lrs.XapiContext;
import com.claresco.tinman.lrs.XapiContextActivities;
import com.claresco.tinman.lrs.XapiExtension;
import com.claresco.tinman.lrs.XapiGroup;
import com.claresco.tinman.lrs.XapiIRI;
import com.claresco.tinman.lrs.XapiInteraction;
import com.claresco.tinman.lrs.XapiInteractionComponent;
import com.claresco.tinman.lrs.XapiInverseFunctionalIdentifier;
import com.claresco.tinman.lrs.XapiLanguageMap;
import com.claresco.tinman.lrs.XapiObject;
import com.claresco.tinman.lrs.XapiPerson;
import com.claresco.tinman.lrs.XapiResult;
import com.claresco.tinman.lrs.XapiScore;
import com.claresco.tinman.lrs.XapiStatement;
import com.claresco.tinman.lrs.XapiStatementBatch;
import com.claresco.tinman.lrs.XapiStatementRef;
import com.claresco.tinman.lrs.XapiStatementResult;
import com.claresco.tinman.lrs.XapiSubStatement;
import com.claresco.tinman.lrs.XapiVerb;
import com.claresco.tinman.servlet.XapiCredentials;
import com.claresco.tinman.servlet.XapiCredentialsList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

/**
 * JsonUtility.java
 *
 * Description:
 * 	Collection of useful methods for Json related purposes
 *
 * Status:
 * 	GREEN
 *
 * @author rheza
 * on Feb 25, 2014
 * 
 */

public final class JsonUtility {

	/**
	 * 
	 * Description:
	 *	Building Gson
	 *
	 * Params:
	 *
	 */
	public static Gson createGson(){
		final GsonBuilder gsonBuilder = new GsonBuilder();
		 
		 //Register to gsonBuilder
		 gsonBuilder.registerTypeAdapter(XapiStatement.class, new XapiStatementJson());
		 gsonBuilder.registerTypeAdapter(XapiActor.class, new XapiActorJson());
		 gsonBuilder.registerTypeAdapter(XapiAgent.class, new XapiAgentJson());
		 gsonBuilder.registerTypeAdapter(XapiGroup.class, new XapiGroupJson());
		 gsonBuilder.registerTypeAdapter(XapiInverseFunctionalIdentifier.class,
				 new XapiInverseFuncIdJson());
		 gsonBuilder.registerTypeAdapter(XapiAccount.class, new XapiAccountJson());
		 gsonBuilder.registerTypeAdapter(XapiVerb.class, new XapiVerbJson());
		 gsonBuilder.registerTypeAdapter(XapiObject.class, new XapiObjectJson());
		 gsonBuilder.registerTypeAdapter(XapiStatementRef.class, new XapiStatementRefJson());
		 gsonBuilder.registerTypeAdapter(XapiSubStatement.class, new XapiSubStatementJson());
		 gsonBuilder.registerTypeAdapter(XapiActivity.class, new XapiActivityJson());
		 gsonBuilder.registerTypeAdapter(XapiActivityDefinition.class,
				 new XapiActivityDefinitionJson());
		 gsonBuilder.registerTypeAdapter(XapiLanguageMap.class, new XapiLanguageMapJson());
		 gsonBuilder.registerTypeAdapter(XapiResult.class, new XapiResultJson());
		 gsonBuilder.registerTypeAdapter(XapiScore.class, new XapiScoreJson());
		 gsonBuilder.registerTypeAdapter(XapiContextActivities.class,
				 new XapiContextActivitiesJson());
		 gsonBuilder.registerTypeAdapter(XapiContext.class, new XapiContextJson());
		 gsonBuilder.registerTypeAdapter(XapiInteraction.class, new XapiInteractionJson());
		 gsonBuilder.registerTypeAdapter(XapiInteractionComponent.class, 
				 new XapiInteractionComponentJson());
		 gsonBuilder.registerTypeAdapter(XapiCredentials.class, new XapiCredentialsJson());
		 gsonBuilder.registerTypeAdapter(XapiStatementBatch.class, new XapiStatementBatchJson());
		 gsonBuilder.registerTypeAdapter(XapiPerson.class, new XapiPersonJson());
		 gsonBuilder.registerTypeAdapter(XapiStatementResult.class
				 , new XapiStatementResultJson());
		 gsonBuilder.registerTypeAdapter(XapiExtension.class, new XapiExtensionsJson());
		 gsonBuilder.registerTypeAdapter(XapiCredentialsList.class, new XapiCredentialsListJson());
		 
		 final Gson gson = gsonBuilder.create();
		 
		 return gson;
	}
	
	/**
	 * Description:
	 *		Convert JsonElement to JsonObject, i.e. Mapping
	 * Params:
	 *
	 */
	protected static JsonObject convertJsonElementToJsonObject(JsonElement element){
		if(element == null){
			throw new XapiBadJsonObjectException("element is null");
		}
		
		if(isNotJsonObject(element)){
			throw new XapiBadJsonObjectException(element.getAsString() + " is not an object");
		}
		return element.getAsJsonObject();
	}
	
	
	
	/**
	 * Description:
	 *		Based on the mapping, find the element I want
	 *		Remember that JsonObject is collection of element mapped by its key
	 *
	 * Params:
	 *
	 */
	protected static JsonElement findJsonElementWithKey(JsonObject theObject, String key){
		return theObject.get(key);
	}
	
	
	
	/**
	 * Description:
	 *		Check if the element equals certain string
	 *
	 * Params:
	 *
	 */
	protected static Boolean elementEqualString(JsonElement theElement, String theString){
		return theElement.getAsString().equals(theString);
	}
	
	
	
	/**
	 * Description:
	 *		Let some piece of element to be deserialized by another class
	 *
	 * Params:
	 *
	 */
	protected static <T>T delegateDeserialization(JsonDeserializationContext theContext, JsonElement theElement, Type theType){
		return theContext.deserialize(theElement, theType);
	}
	
	
	
	/**
	 * Description:
	 *		Get the piece of the whole statement as a String
	 *
	 * Params:
	 *
	 */
	protected static String getElementAsString(JsonObject theObject, String theKey){
		if (hasElement(theObject, theKey)){
			return theObject.get(theKey).getAsString();
		}
		else{
			return null;
		}
	}

	
	
	protected static Integer getElementAsInt(JsonObject theObject, String theKey) {
		if (hasElement(theObject, theKey)){
			return theObject.get(theKey).getAsInt();
		} else{
			return null;
		}
	}
	
	
	
	protected static Double getElementAsDouble(JsonObject theObject, String theKey) {
		if (hasElement(theObject, theKey)){
			return theObject.get(theKey).getAsDouble();
		} else{
			return null;
		}
	}
	
	
	
	protected static Boolean getElementAsBool(JsonObject theObject, String theKey){
		if (hasElement(theObject, theKey)){
			return theObject.get(theKey).getAsBoolean();
		} else{
			return null;
		}
	}
	
	
	
	protected static boolean isJsonElementNull(JsonElement theElement){
		return theElement.isJsonNull();
	}
	
	
	
	protected static boolean hasElement(JsonObject theObject, String theKey){
		if(theObject.has(theKey)){
			if(!isJsonElementNull(get(theObject, theKey))){
				return true;
			}
		}
		return false;
	}
	
	
	
	protected static boolean isActivityOfTypeInteraction(String theType){
		return theType.equals("http://adlnet.gov/expapi/activities/cmi.interaction");
	}
	
	
	
	protected static JsonArray getAnArray(JsonObject theObject, String theKey){
		if (hasElement(theObject, theKey)){
			return theObject.get(theKey).getAsJsonArray();
		} else{
			return null;
		}
	}
	
	
	
	protected static JsonElement get(JsonObject theObject, String theKey){
			return theObject.get(theKey);
		
	}
	
	/**
	 * 
	 * Description:
	 *	Handling interaction
	 *
	 * Params:
	 *
	 */
	protected static ArrayList<XapiInteractionComponent> handleInteraction(JsonObject theObject, String theKey,
			JsonDeserializationContext theContext){
		if(hasElement(theObject, theKey)){
			JsonArray theJsonArray = getAnArray(theObject, theKey);
			ArrayList<XapiInteractionComponent> theArray = new ArrayList<XapiInteractionComponent>();
			// Convert Json Array into array of XapiInteractionComponent
			for(JsonElement element : theJsonArray){
				theArray.add((XapiInteractionComponent)JsonUtility.delegateDeserialization(theContext, element, 
						XapiInteractionComponent.class));
			}
			return theArray;
		}
		return null;
	}
	
	/**
	 * 
	 * Description:
	 *	Create a JsonArray from XapiInteractionComponents
	 *
	 * Params:
	 *
	 */
	protected static JsonArray jsonArrayFromXapiInteractionComponent(ArrayList<XapiInteractionComponent>
			theComponents, JsonSerializationContext theContext){
		JsonArray theArray = new JsonArray();
		
		for(XapiInteractionComponent component : theComponents){
			theArray.add(theContext.serialize(component));
		}
		
		return theArray;
	}
	
	
	
	protected static XapiLanguageMap constructLanguageMap(JsonObject theObject, String key){
		
		XapiLanguageMap theLanguageMap = new XapiLanguageMap();
		
		if (theObject.get(key) == null){
			return null;
		}
		
		for (Map.Entry<String,JsonElement> entry : theObject.get(key).getAsJsonObject().entrySet()) {
			theLanguageMap.registerLanguage(entry.getKey(), entry.getValue().getAsString());
		}
		
		return theLanguageMap;
	}
	
	
	
	protected static ArrayList<XapiActivity> convertJsonArrayOfActivities(JsonObject theObject, String theElementName, 
			ArrayList<XapiActivity> theArrayList, JsonDeserializationContext theContext){
		JsonElement theActivities;
		
		if(hasElement(theObject, theElementName)){
			theActivities = get(theObject, theElementName);
			if(theActivities.isJsonArray()){
				JsonArray theJsonArray = getAnArray(theObject, theElementName);
				for(JsonElement e : theJsonArray){
					theArrayList.add((XapiActivity) delegateDeserialization(theContext, e, XapiActivity.class));
				}
			}else{
				theArrayList.add((XapiActivity) delegateDeserialization(theContext, theActivities, XapiActivity.class));
			}
			
		}
		return theArrayList;
	}
	
	
	
	protected static JsonArray convertToJsonArray(ArrayList<String> theList){
		JsonArray theArray = new JsonArray();
		
		for(String s : theList){
			theArray.add(new JsonPrimitive(s));
		}
		
		return theArray;
	}
	
	
	
	protected static JsonArray convertToJsonArrayFromIRIList(ArrayList<XapiIRI> theList){
		ArrayList<String> theStringList = new ArrayList<String>();
		
		for(XapiIRI theIRI : theList){
			theStringList.add(theIRI.toString());
		}
		
		return convertToJsonArray(theStringList);
	}
	
	
	
	protected static boolean isJsonObject(JsonElement theElement){
		return theElement.isJsonObject();
	}
	
	
	protected static boolean isNotJsonObject(JsonElement theElement){
		return !isJsonObject(theElement);
	}
	
}
