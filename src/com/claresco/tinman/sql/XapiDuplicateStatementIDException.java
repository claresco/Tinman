/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiStatementExistsException.java	Apr 16, 2014
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
 * XapiStatementExistsException
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiDuplicateStatementIDException extends XapiDataIntegrityException {
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiDuplicateStatementIDException(String theMessage) {
		super(theMessage);
	}
}
