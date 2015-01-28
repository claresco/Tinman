/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiLMSNotAuthorized.java	May 14, 2014
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
 * XapiLMSNotAuthorized
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiNotAuthorizedException extends XapiServletException {
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiNotAuthorizedException(String theMessage) {
		super(401, theMessage);
	}
}
