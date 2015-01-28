/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiLogger.java	Jul 30, 2014
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * XapiLogger
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiLogger {

	private static Logger myRootLogger;
	private static Logger myBugLogger;
	private static Logger mySecurityThreatLogger;
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiLogger() {
		myRootLogger = LogManager.getLogger();
		myBugLogger = LogManager.getLogger("com.claresco.bugs");
		mySecurityThreatLogger = LogManager.getLogger("com.claresco.security.threats");
	}
	
	
	
	protected void logBug(String theMessage, Exception theException){
		myBugLogger.fatal(theMessage, theException);
	}
	
	
	
	protected void logThreats(String theMessage){
		mySecurityThreatLogger.warn(theMessage);
	}
	
	
	
	protected void keepTrack(String theMessage){
		myRootLogger.trace(theMessage);
	}
	
}
