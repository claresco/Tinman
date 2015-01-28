/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiBadIRIException.java	Jun 24, 2014
 *
 * Copyright 2014 Claresco Corporation, Berkeley, CA 94704. All Rights Reserved.
 *
 * This software is the proprietary information of Claresco Corporation.
 * Use is subject to license terms.
 *
 * Author : Rheza
 *
 */
package com.claresco.tinman.lrs;

/**
 * XapiBadIRIException
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiBadIRIException extends XapiClarescoException {
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiBadIRIException(String theMessage) {
		super(theMessage, 400);
	}
}
