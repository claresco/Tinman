/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiActivityInvalidException.java	Apr 18, 2014
 *
 * Copyright 2014 Claresco Corporation, Berkeley, CA 94704. All Rights Reserved.
 *
 * This software is the proprietary information of Claresco Corporation.
 * Use is subject to license terms.
 *
 * Author : Rheza
 *
 */
package com.claresco.tinman.sql;

/**
 * XapiActivityInvalidException
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiActivityInvalidException extends XapiDataIntegrityException {

	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiActivityInvalidException(String theMessage) {
		// TODO Auto-generated constructor stub
		super(theMessage, 403);
	}
}
