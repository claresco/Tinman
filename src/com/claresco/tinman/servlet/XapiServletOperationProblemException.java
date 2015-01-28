/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiServletOperationProblemException.java	Jun 25, 2014
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
 * XapiServletOperationProblemException
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiServletOperationProblemException extends XapiServletException {

	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiServletOperationProblemException(String theMessage) {
		super(500, theMessage);
	}
}
