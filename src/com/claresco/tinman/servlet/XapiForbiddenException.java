/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiForbiddenException.java	May 22, 2014
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
 * XapiForbiddenException
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiForbiddenException extends XapiServletException {
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiForbiddenException(String theMessage) {
		super(403, theMessage);
	}
}
