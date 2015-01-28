/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiLMSCredentials.java	Jul 16, 2014
 *
 * Copyright 2014 Claresco Corporation, Berkeley, CA 94704. All Rights Reserved.
 *
 * This software is the proprietary information of Claresco Corporation.
 * Use is subject to license terms.
 *
 * Author : Rheza
 *
 */
package com.claresco.tinman.servlet;

/**
 * XapiLMSCredentials
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public final class XapiLMSCredentials extends XapiCredentials {
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	protected XapiLMSCredentials() {
		super();
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.servlet.XapiCredentials#isDefiningAllowed()
	 */
	@Override
	public boolean isDefiningAllowed() {
		return true;
	}
}
