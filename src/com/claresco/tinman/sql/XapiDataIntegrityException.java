/**
 * ClarescoExperienceAPI
 * Copyright 
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * Please contact Claresco, www.claresco.com, if you have any questions.
 **/

package com.claresco.tinman.sql;

import com.claresco.tinman.lrs.XapiClarescoException;

/**
 * XapiSQLException.java
 *
 *
 * 
 * Status:
 * 	RED
 *
 * @author rheza
 * on Mar 31, 2014
 * 
 */

public class XapiDataIntegrityException extends XapiClarescoException {
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiDataIntegrityException(String theMessage) {
		super(theMessage, 409);
	}
	
	
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiDataIntegrityException(String theMessage, int theErrorCode) {
		super(theMessage, theErrorCode);
	}
}
