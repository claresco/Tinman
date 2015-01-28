/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiServletAction.java	Apr 2, 2014
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
 * @author rheza
 *
 */
public enum XapiServletActionType {
	
	STATEMENTWRITE("Writing statements"),
	STATEMENTREADMINE("Reading statements belongs to client"),
	STATEMENTREADANY("Readding any statements"),
	STATEWRITE("Writing state"),
	STATEREAD("Reading state"),
	PROFILEWRITE("Writing profile"),
	PROFILEREAD("Reading profile"),
	DEFINE("Defining");
	
	private String myExplanation;
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	private XapiServletActionType(String explanation) {
		this.myExplanation = explanation;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return this.myExplanation;
	}
}
