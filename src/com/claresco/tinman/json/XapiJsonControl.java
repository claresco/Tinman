/**
 * ClarescoExperienceAPI
 * Copyright 1999, 2014
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * Please contact Claresco, www.claresco.com, if you have any questions.
 **/

package com.claresco.tinman.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.claresco.tinman.lrs.XapiAccount;
import com.claresco.tinman.lrs.XapiActivity;
import com.claresco.tinman.lrs.XapiActor;
import com.claresco.tinman.lrs.XapiPerson;
import com.claresco.tinman.lrs.XapiResult;
import com.claresco.tinman.lrs.XapiStatement;
import com.claresco.tinman.lrs.XapiStatementBatch;
import com.claresco.tinman.lrs.XapiStatementResult;
import com.claresco.tinman.servlet.XapiCredentials;
import com.claresco.tinman.servlet.XapiCredentialsList;
import com.claresco.tinman.servlet.XapiKeySecret;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;

/**
 * XapiJsonControl.java
 *
 * 
 *
 *
 *
 * @author rheza
 * on Mar 28, 2014
 * 
 */

public class XapiJsonControl {

	private Gson myGson;

	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiJsonControl() {
		myGson = JsonUtility.createGson();
	}

	/**
	 * Definition:
	 *
	 *
	 * Params:
	 *
	 *
	 */
	public XapiStatement deserializeStatement(String s){
		return myGson.fromJson(s, XapiStatement.class);
	}



	public XapiStatementBatch deserializeStatementBatch(HttpServletRequest theRequest) throws XapiParseException{
		try{
			XapiStatementBatch theStatementBatch = myGson.fromJson(theRequest.getReader(), XapiStatementBatch.class);
			return theStatementBatch;
		}catch(MalformedJsonException e){
			//e.printStackTrace();
			throw new XapiParsingOperationProblemException("Having trouble parsing the statements : MalformedJson");
		}catch(IOException e){
			//e.printStackTrace();
			throw new XapiParsingOperationProblemException("Having trouble parsing the statements");
		}catch(JsonSyntaxException e){
			//e.printStackTrace();
			throw new XapiParsingOperationProblemException("Having trouble parsing the statement : Bad Json syntax");
		}
	}
	
	
	
	public XapiStatementBatch deserializeStatementBatch(String theStatementBatchString) throws XapiParseException{
		try{
			XapiStatementBatch theStatementBatch = myGson.fromJson(theStatementBatchString,
					XapiStatementBatch.class);
			return theStatementBatch;
		}catch(JsonSyntaxException e){
			//e.printStackTrace();
			throw new XapiParsingOperationProblemException("Having trouble parsing the statement : Bad Json syntax");
		}
	}



	public XapiStatement deserializeStatement(HttpServletRequest theRequest) throws XapiParseException{
		try{
			XapiStatement theStatement = myGson.fromJson(theRequest.getReader(), XapiStatement.class);
			return theStatement;
		}catch(MalformedJsonException e){
			//e.printStackTrace();
			throw new XapiParseException("Having trouble parsing the statements : MalformedJson");
		}catch(IOException e){
			//e.printStackTrace();
			throw new XapiParsingOperationProblemException("Having trouble parsing the statement");
		}catch(JsonSyntaxException e){
			//e.printStackTrace();
			throw new XapiParseException("Having trouble parsing the statement : Bad Json syntax");
		}

	}



	public XapiActor deserializeActor(String theActor) throws XapiParseException{
		try{
			return myGson.fromJson(theActor, XapiActor.class);
		}catch(JsonSyntaxException e){
			//e.printStackTrace();
			throw new XapiParseException("Having trouble parsing the actor : Bad Json syntax");
		}
		
	}



	public XapiActor deserializeActor(HttpServletRequest theRequest) throws XapiParseException{
		try{
			XapiActor theActor = myGson.fromJson(theRequest.getReader(), XapiActor.class);
			return theActor;
		}catch(MalformedJsonException e){
			//e.printStackTrace();
			throw new XapiParseException("Having trouble parsing the actor : MalformedJson");
		}catch(IOException e){
			//e.printStackTrace();
			throw new XapiParsingOperationProblemException("Having trouble parsing the actor");
		}catch(JsonSyntaxException e){
			//e.printStackTrace();
			throw new XapiParseException("Having trouble parsing the actor : Bad Json syntax");
		}
	}



	public XapiPerson deserializePerson(String thePersonString) throws XapiParseException{
		XapiPerson thePerson = myGson.fromJson(thePersonString, XapiPerson.class);
		return thePerson;
	}



	public XapiResult deserializeResult(String theJsonResult) throws XapiParseException{
		XapiResult theResult = myGson.fromJson(theJsonResult, XapiResult.class);
		return theResult;
	}



	public XapiCredentials deserializeCredentials(BufferedReader theReader) throws XapiParseException{
		XapiCredentials theCredentials = myGson.fromJson(theReader, XapiCredentials.class);
		return theCredentials;
	}



	public XapiCredentials deserializeCredentials(String theJsonCredentials) throws XapiParseException{
		XapiCredentials theCredentials = myGson.fromJson(theJsonCredentials, XapiCredentials.class);
		return theCredentials;
	}
	
	
	
	public XapiCredentialsList deserialiCredentialsList(BufferedReader theReader){
		return myGson.fromJson(theReader, XapiCredentialsList.class);
	}



	/**
	 * Definition:
	 *
	 *
	 * Params:
	 *
	 *
	 */
	public String serializeStatement(XapiStatement theStatement){
		return myGson.toJson(theStatement);
	}



	public String serializeStatementBatch(XapiStatementBatch theStatementBatch){
		return myGson.toJson(theStatementBatch);
	}



	public String serializeStatementResult(XapiStatementResult theStatementResult){
		return myGson.toJson(theStatementResult);
	}



	public String serializeActivity(XapiActivity theActivity){
		return myGson.toJson(theActivity);
	}
	
	
	
	public String serializeCredentialsList(XapiCredentialsList theCredentialsList){
		return myGson.toJson(theCredentialsList, XapiCredentialsList.class);
	}



	public String createKeySecretJson(XapiKeySecret theKeySecret){
		JsonObject theKeySecretJson = new JsonObject();

		// Send client's login and password as a JSON
		theKeySecretJson.addProperty("login", theKeySecret.getKey());
		theKeySecretJson.addProperty("password", theKeySecret.getSecret());

		return theKeySecretJson.toString();
	}

	
	
	public static void main(String[] args) {
		XapiJsonControl theControl = new XapiJsonControl();
		
		ArrayList<String> theName = new ArrayList<String>();
		theName.add("Mars"); 
		theName.add("Mecury");
		
		ArrayList<XapiAccount> theAccount = new ArrayList<XapiAccount>();
		theAccount.add(new XapiAccount("homePage", "name"));
		
		XapiPerson theP = new XapiPerson(theName, null, null, null, theAccount);
		
		String personJson = theControl.myGson.toJson(theP, XapiPerson.class); 
		
		System.out.println(personJson);
		
		XapiPerson theP2 = theControl.myGson.fromJson(personJson, XapiPerson.class);
		
		System.out.println(theP2);
	}
}
