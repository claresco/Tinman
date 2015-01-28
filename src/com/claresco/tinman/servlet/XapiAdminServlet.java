/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiAdminServlet.java	Sep 2, 2014
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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;

import com.claresco.tinman.lrs.XapiActor;

import riotcmd.trig;

/**
 * XapiAdminServlet
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiAdminServlet extends HttpServlet {
	
	static final long serialVersionUID = 1L;
	private static final String POST = "POST";
	private static final String PUT = "PUT";
	private static final String GET = "GET";
	
	private static final String CONTENTTYPE = "Content-Type";
	private static final String PLAINTEXT = "text/plain";
	private static final String HTMLTEXT = "text/html";
	private static final String JSON = "application/json";
	
	private ServletContext myServletContext;
	
	private XapiLogger myLogger;
	
	private ArrayList<XapiKeySecret> myLMSKeySecret;
	
	private XapiAccessManager myAccessManager;
	
	private ArrayList<XapiKeySecret> myAdminKeySecret;

	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		
		myServletContext = getServletContext();
		
		myLogger = new XapiLogger();
		
		myAccessManager = (XapiAccessManager) myServletContext.getAttribute("Access Manager");
		
		myAdminKeySecret = new ArrayList<XapiKeySecret>();
		
		String theAdminKey = myServletContext.getInitParameter("AdminLogin");
		String theAdminSecret = myServletContext.getInitParameter("AdminPassword");
		myAdminKeySecret.add(new XapiKeySecret(theAdminKey, theAdminSecret));
 		
		myLogger.keepTrack("Servlet initialized");
	}
	
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp, GET);
	}
	
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp, POST);
	}
	
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp, PUT);
	}
	
	
	
	private void handleRequest(HttpServletRequest request, HttpServletResponse response,
			String theMethodName){
		try{
			if(isAuthorized(request)){
				if(myAccessManager != null){
					PrintWriter theWriter = response.getWriter();
					
					ArrayList<String> theAccountNameList = new ArrayList<String>();
					
					int index = 1;
					
					theWriter.println("Last Access --- Name --- Account name --- Expiry");
					
					for(Map.Entry<XapiKeySecret, DateTime> entry : myAccessManager.getSortedSet()){
						XapiKeySecret theKeySecret = entry.getKey();
						
						XapiCredentials theCredentials = myAccessManager.getCredential(theKeySecret);
						XapiActor theActor = theCredentials.getActor();
						
						String theAccountName = null;
						if(theActor.getInverseFuncId().hasAccount()){
							theAccountName = theActor.getInverseFuncId().getAccount().getName();
						}
						
						if(!theAccountNameList.contains(theAccountName)){
							theWriter.print(index + ") ");
							
							theWriter.print(entry.getValue() + " --- ");
							
							if(theActor.hasName()){
								theWriter.print(theActor.getName() + " --- ");
							}else{
								theWriter.print("*No Name*" + " --- ");
							}
							
							if(theAccountName == null){
								theWriter.print("*No Account Name* --- ");
							}else{
								theWriter.print(theAccountName + " --- ");
							}
							
							theWriter.println(theCredentials.getExpiry() + "\n");
							
							theAccountNameList.add(theAccountName);
							
							index++;
						}
							
					}
				}
			}else{
				response.setHeader("WWW-Authenticate", "Basic realm=\"Username and password please\"");
				response.setStatus(401);
			}
		}catch(XapiServletException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	private boolean isAuthorized(HttpServletRequest request) throws XapiServletOperationProblemException{
		XapiKeySecret theKeySecret = handleAuthorizationHeader(request);
		
		if(myAdminKeySecret.contains(theKeySecret)){
			return true;
		}
		
		return false;
	}
	
	
	
	/**
	 * 
	 * Definition:
	 *	Helper method to get key secret passed on as Authorization header
	 *
	 * Params:
	 *
	 *
	 */
	private XapiKeySecret handleAuthorizationHeader(HttpServletRequest request) throws 
	XapiServletOperationProblemException{
		String theAuthorizationHeader = request.getHeader("authorization");

		if(theAuthorizationHeader == null){
			theAuthorizationHeader = request.getHeader("Authorization");
		}
		
		if(theAuthorizationHeader == null){
			return null;
		}
		
		theAuthorizationHeader = theAuthorizationHeader.trim();
		if(!theAuthorizationHeader.startsWith("Basic")){
			return null;
		}

		String temp = theAuthorizationHeader.substring(5);
		temp = temp.trim();
		try{
			temp = XapiServletUtility.decodeBase64(temp);
		}catch(UnsupportedEncodingException e){
			throw new XapiServletOperationProblemException("Can't decode the message");
		}

		String[] theLoginInfo = temp.split(":");

		return new XapiKeySecret(theLoginInfo[0], theLoginInfo[1]);
	}
	
	
	
	private void validateURL(HttpServletRequest request) throws XapiServletException{
		String[] urlArray = XapiServletUtility.getRequestURLArray(request);
		
		if(!isURLSupported(urlArray)){
			throw new XapiBadURLException("We do not support the URL");
		}
	}
	
	
	
	/**
	 * 
	 * Definition:
	 *	Assuming that the second string in the array is "xapi"
	 *
	 * Params:
	 *
	 *
	 */
	private boolean isURLSupported(String[] urlArray){
		// Too short
		if(urlArray.length < 2){
			return false;
		}

		// Too long
		if(urlArray.length > 3){
			return false;
		}

		// does not have keyword 'xapi'
		if(!urlArray[0].equalsIgnoreCase("control")){
			return false;
		}

		return true;
	}
}
