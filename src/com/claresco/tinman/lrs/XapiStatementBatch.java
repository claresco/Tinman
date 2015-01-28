/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiStatementBatch.java	Jun 11, 2014
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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * XapiStatementBatch
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiStatementBatch implements Iterable<XapiStatement>{
	
	private ArrayList<XapiStatement> myStatementBatch;
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiStatementBatch() {
		myStatementBatch = new ArrayList<XapiStatement>();
	}
	
	
	
	/**
	 * 
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiStatementBatch(ArrayList<XapiStatement> theStatementBatch){
		myStatementBatch = theStatementBatch;
	}
	
	
	
	public void addStatementToBatch(XapiStatement theStatement){
		myStatementBatch.add(theStatement);
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<XapiStatement> iterator() {
		Iterator<XapiStatement> statementIterator = myStatementBatch.iterator();
		return statementIterator;
	}
	
	
	
	public int size(){
		return myStatementBatch.size();
	}
	
	
	
	public XapiStatement getStatementAtIndex(int theIndex){
		return myStatementBatch.get(theIndex);
	}
}
