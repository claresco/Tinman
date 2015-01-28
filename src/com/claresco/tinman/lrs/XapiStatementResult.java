/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiStatementResult.java	Jul 17, 2014
 *
 * Copyright 2014 Claresco Corporation, Berkeley, CA 94704. All Rights Reserved.
 *
 * This software is the proprietary information of Claresco Corporation.
 * Use is subject to license terms.
 *
 * Author : Rheza
 *
 */
package com.claresco.tinman.lrs;

/**
 * XapiStatementResult
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiStatementResult {
	
	private XapiStatementBatch myStatements;
	private XapiIRL myMore;
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiStatementResult(XapiStatementBatch theStatements) {
		this(theStatements, null);
	}
	
	
	
	public XapiStatementResult(XapiStatementBatch theStatements, XapiIRL theMore){
		myStatements = theStatements;
		myMore = theMore;
	}
	
	
	
	public boolean hasStatements(){
		return myStatements != null;
	}
	
	
	
	public boolean hasMore(){
		return myMore != null;
	}
	
	
	
	public XapiStatementBatch getStatements(){
		return myStatements;
	}
	
	
	
	public XapiIRL getMore(){
		return myMore;
	}
}
