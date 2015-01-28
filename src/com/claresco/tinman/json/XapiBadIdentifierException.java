/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiBadIdentifierException.java	Apr 14, 2014
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

/**
 * XapiBadIdentifierException
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiBadIdentifierException extends XapiBadRequestException {

	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiBadIdentifierException() {
		// TODO Auto-generated constructor stub
		super("The identifier is invalid");
	}
	
	public XapiBadIdentifierException(String errorMessage){
		super(errorMessage);
	}
}
