/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiBadURLException.java	May 13, 2014
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
 * XapiBadURLException
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiBadURLException extends XapiServletException{
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiBadURLException(String theMessage) {
		super(404, theMessage);
	}
}
