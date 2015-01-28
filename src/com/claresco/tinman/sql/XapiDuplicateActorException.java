/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiDuplicateActorException.java	Apr 17, 2014
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
 * XapiDuplicateActorException
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiDuplicateActorException extends XapiDataIntegrityException{
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiDuplicateActorException(String theMessage) {
		super(theMessage);
	}
}
