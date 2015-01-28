/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiBadParamsException.java	May 2, 2014
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
 * XapiBadParamsException
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiConflictingParamException extends XapiDataIntegrityException {

	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiConflictingParamException(String message) {
		super(message, 400);
	}
}
