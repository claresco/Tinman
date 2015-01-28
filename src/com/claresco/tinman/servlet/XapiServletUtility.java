/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiServletUtility.java	May 13, 2014
 *
 * Copyright 2014 Claresco Corporation, Berkeley, CA 94704. All Rights Reserved.
 *
 * This software is the proprietary information of Claresco Corporation.
 * Use is subject to license terms.
 *
 * Author : Rheza
 *
 */
package com.claresco.tinman.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import com.claresco.tinman.sql.XapiSQLControl;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

/**
 * XapiServletUtility
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public abstract class XapiServletUtility {
	
	/**
	 * 
	 * Definition:
	 *	Splitting the URL array based on forward slash
	 *
	 * Params:
	 *
	 *
	 */
	protected static String[] getRequestURLArray(HttpServletRequest req){
		String theURL = req.getRequestURI();
		String[] urlArray;
		
		if(theURL.endsWith("/")){
			urlArray = theURL.substring(0, theURL.length()-2).split("/");
		}else{
			urlArray = theURL.split("/");
		}
		
		
		String[] returnURL = new String[urlArray.length-1];
		
		for(int i =1; i < urlArray.length; i++){
			returnURL[i-1] = urlArray[i];
		}
		
		return returnURL;
	}
	
	
	
	protected static boolean checkAPI(String theAPI, String theSupposedAPI){
		return theAPI.equalsIgnoreCase(theSupposedAPI);
	}

	
	
	protected static boolean checkAction(String theAction, String theSupposedAction){
		return theAction != null && theAction.equalsIgnoreCase(theSupposedAction);
	}
	
	
	
	protected static String getStringFromReader(BufferedReader theReader) throws 
			XapiServletOperationProblemException{
		int c;
		StringBuffer theStringBuffer = new StringBuffer();
		try{
			while((c = theReader.read()) != -1){
				theStringBuffer.append((char) c);
			}
			String s = theStringBuffer.toString();
			return theStringBuffer.toString();
		}catch(IOException e){
			throw new XapiServletOperationProblemException("Having problem reading the document");
		}
		
	}
	
	
	
	protected static BufferedReader getReader(HttpServletRequest req) throws 
			XapiServletOperationProblemException{
		try{
			return req.getReader();
		}catch(IOException e){
			throw new XapiServletOperationProblemException("Having problem reading the request");
		}
	}
	
	
	
	protected static PrintWriter getWriter(HttpServletResponse resp) throws
			XapiServletOperationProblemException{
		try{
			return resp.getWriter();
		}catch(IOException e){
			throw new XapiServletOperationProblemException("Having problem writing a response");
		}
	}
	
	
	
	protected static void closeConnection(Connection conn){
		try{
			if(conn != null){
				conn.close();
			}
		}catch(Exception e){
			
		}
	}
	
	
	
	protected static void closeSQLControl(XapiSQLControl theSQLControl){
		try{
			if(theSQLControl != null){
				theSQLControl.close();
			}
		}catch(Exception e){
			
		}
	}
	
	
	
	protected static String createJsonArray(ArrayList<String> theData){
		JsonArray theArray = new JsonArray();
		
		for(String s : theData){
			theArray.add(new JsonPrimitive(s));
		}
		
		return theArray.toString();
	}
	
	
	
	protected static String createJsonArray(Set<String> theData){
		JsonArray theArray = new JsonArray();
		
		for(String s : theData){
			theArray.add(new JsonPrimitive(s));
		}
		
		return theArray.toString();
	}
	
	
	
	protected static String decodeBase64(String theSecret) throws UnsupportedEncodingException{
		Base64 decoder = new Base64();
		
		byte[] decodedBytes = decoder.decodeBase64(theSecret);
		return new String(decodedBytes, "UTF-8");
	}
	
	
	
	protected static String encodeBase64(String theSecret){
		Base64 encoder = new Base64();
		
		return encoder.encodeAsString(theSecret.getBytes());
	}
	
	
	
	protected static String getOriginURL(HttpServletRequest req){
		return req.getHeader("Origin");
	}
	
	
	
	protected static String createHeaderString(ArrayList<String> theList){
		String theResult = "";
		
		for(String s : theList){
			theResult += s + ", ";
		}
		
		theResult = theResult.trim();
		theResult = theResult.substring(0, theResult.length()-1);
		
		return theResult;
	}
	
	
	
	protected static void rollBack(Connection conn){
		try{
			conn.rollback();
		}catch(SQLException e){
		}
	}
	
	
	
	protected static UUID validateUUID(String theUUIDString) throws XapiBadParamException{
		try{
			if(theUUIDString != null && !theUUIDString.isEmpty()){
				return UUID.fromString(theUUIDString);
			}else{
				throw new XapiBadParamException("Bad UUID");
			}			
		}catch(IllegalArgumentException e){
			throw new XapiBadParamException("Bad UUID");
		}
	}
	
	
	
	public static void main(String[] args) {
		ArrayList<String> theData = new ArrayList<String>();
		theData.add("aldfjaklfja");
		theData.add("duo");
		
		System.out.println(createJsonArray(theData));
		
		try{
			System.out.println(decodeBase64("dGVzdDpwYXNzd29yZA=="));
		}catch(Exception e){

		}
		
		String st = "Basic dGVzdDpwYXNzd29yZA==";
		System.out.println(st.substring(5));
		
		System.out.println(encodeBase64("cremebrulee#$%:Comeatmebro***"));
		
		System.out.println(createHeaderString(theData));
	}
	
	
}
