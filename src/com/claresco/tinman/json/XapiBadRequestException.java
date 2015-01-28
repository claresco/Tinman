/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiBadRequestException.java	Apr 8, 2014
 *
 * Copyright 2014 Claresco Corporation, Berkeley, CA 94704. All Rights Reserved.
 *
 * This software is the proprietary information of Claresco Corporation.
 * Use is subject to license terms.
 *
 * Author : Rheza
 *
 */
package com.claresco.tinman.json;

import com.claresco.tinman.lrs.XapiClarescoException;

/**
 * XapiBadRequestException
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiBadRequestException extends XapiParseException{
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiBadRequestException(String theMessage) {
		// TODO Auto-generated constructor stub
		super(theMessage, 400);
	}
}
