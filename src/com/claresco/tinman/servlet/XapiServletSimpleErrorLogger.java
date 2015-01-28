/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiServletSimpleErrorLogger.java	Jul 17, 2014
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;

import com.claresco.tinman.lrs.XapiClarescoException;

/**
 * XapiServletSimpleErrorLogger
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiServletSimpleErrorLogger {

	private static long logID = 100000;
	private static String myPath;
	private String myFileName;
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	protected XapiServletSimpleErrorLogger(String path, String fileName) {
		myPath = path;
		myFileName = fileName;
	}
	
	protected long log(Exception theException, int errorNumber, String errorMessage){
		DateTime theDate = DateTime.now();
		
		long theReturnedValue = logID;
		
		//String fileName = theDate.year().getAsShortText() + "-" + theDate.monthOfYear()
		//		.getAsString() + "-" +theDate.dayOfMonth().getAsShortText() + ".txt";
		String fileName = myFileName;
		
		File theLogFile = new File(myPath + fileName);
		
		try{
			if(!theLogFile.exists()){
				theLogFile.createNewFile();
			}
			
			PrintWriter theWriter = new PrintWriter(new BufferedWriter(new FileWriter
					(myPath + fileName, true)));
			
			theWriter.println("===============================================================");
			theWriter.println(logID);
			theWriter.println(theDate.toString());
			logID++;
			theException.printStackTrace(theWriter);
			theWriter.println(errorNumber + " : " + errorMessage);
			theWriter.println("===============================================================\n\n\n");
			
			theWriter.close();
			
			return theReturnedValue;
		}catch(Exception e){
			e.printStackTrace();
		}
		return -1;
	}
	
	
	
	protected long log(Exception theException, int errorNumber, String errorMessage, HttpServletRequest req){
		DateTime theDate = DateTime.now();
		
		long theReturnedValue = logID;
		
		//String fileName = theDate.year().getAsShortText() + "-" + theDate.monthOfYear()
		//		.getAsString() + "-" +theDate.dayOfMonth().getAsShortText() + ".txt";
		String fileName = myFileName;
		
		File theLogFile = new File(myPath + fileName);
		
		try{
			if(!theLogFile.exists()){
				theLogFile.createNewFile();
			}
			
			PrintWriter theWriter = new PrintWriter(new BufferedWriter(new FileWriter
					(myPath + fileName, true)));
			
			theWriter.println("===============================================================");
			theWriter.println(logID);
			theWriter.println(theDate.toString());
			logID++;
			theException.printStackTrace(theWriter);
			theWriter.println(errorNumber + " : " + errorMessage);

			theWriter.println();
			theWriter.println("request.getRequstURI() gives :");
			theWriter.println(req.getRequestURI());
			theWriter.println("request.getQueryString() gives :");
			theWriter.println(req.getQueryString());
		
			theWriter.println("iterating through parameter names gives :");
			Enumeration<String> e = req.getParameterNames();
			while(e.hasMoreElements()){
				String paramName = e.nextElement();
				theWriter.println(paramName + " : " + req.getParameter(paramName));
			}
			
			
			theWriter.println("\nthe header names :\n");
			e = req.getHeaderNames();
			while(e.hasMoreElements()){
				String headerName = e.nextElement();
				theWriter.println(headerName + " : " + req.getHeader(headerName) + "\n");
			}
			
			theWriter.println(req.getMethod());
			
			theWriter.println("Path info : " + req.getPathInfo());
			
			theWriter.println("Reading from the request's reader gives :");
			theWriter.println(XapiServletUtility.getStringFromReader(req.getReader()));
			
			
			theWriter.println("===============================================================\n\n\n");
			theWriter.close();
			
			return theReturnedValue;
		}catch(Exception e){
			e.printStackTrace();
		}
		return -1;
	}
	
	
	
	protected long log(XapiClarescoException theException){
		return log(theException, theException.getErrorCode(), theException.getMessage());
	}
	
	
	
	protected long log(XapiClarescoException theException, HttpServletRequest req){
		return log(theException, theException.getErrorCode(), theException.getMessage(), req);
	}
	
	
	
	protected long log(XapiServletException theException){
		return log(theException, theException.getErrorNumber(), theException.getMessage());
	}
	
	
	
	protected long log(XapiServletException theException, HttpServletRequest req){
		return log(theException, theException.getErrorNumber(), theException.getMessage(), req);
	}
	
	
	/**
	public static void main(String[] args) {
		String path = "/Users/rheza/Documents/workspace/TinmanDeploy/www/log/";
		XapiServletSimpleErrorLogger theL = new XapiServletSimpleErrorLogger(path);
		theL.log(new XapiClarescoException("sdlkfajkfj", 3294));
		theL.log(new Exception(), 123, "adfklajdf");
	}
	**/
}
 