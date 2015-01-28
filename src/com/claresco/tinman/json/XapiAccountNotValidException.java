/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiAccountNotValidException.java	Apr 14, 2014
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

import org.postgresql.translation.messages_bg;

/**
 * XapiAccountNotValidException
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiAccountNotValidException extends XapiBadRequestException {

	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiAccountNotValidException() {
		// TODO Auto-generated constructor stub
		super("The account is invalid");
	}
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiAccountNotValidException(String theMessage) {
		// TODO Auto-generated constructor stub
		super(theMessage);
	}
}
