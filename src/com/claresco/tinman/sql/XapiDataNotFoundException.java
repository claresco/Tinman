/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiDataNotFoundException.java	Jun 20, 2014
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

import com.claresco.tinman.lrs.XapiClarescoException;

/**
 * XapiDataNotFoundException
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiDataNotFoundException extends XapiClarescoException{
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiDataNotFoundException(String theMessage) {
		super(theMessage, 404);
	}
}
