/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiServletAPIType.java	May 13, 2014
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

/**
 * XapiServletAPIType
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public enum XapiServletAPIType {
	
	STATEMENTS("statements");
	
	private String myAPIType;
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	private XapiServletAPIType(String theAPIType) {
		myAPIType = theAPIType;
	}
}
