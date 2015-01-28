/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * AccessedTimeComparator.java	Sep 9, 2014
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

import java.util.Comparator;
import java.util.HashMap;

import org.joda.time.DateTime;

/**
 * AccessedTimeComparator
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class AccessedTimeComparator implements Comparator<XapiKeySecret>{
	
	HashMap<XapiKeySecret, DateTime> myLastAccessedTimeMap;
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public AccessedTimeComparator(HashMap<XapiKeySecret, DateTime> theLastAccessedTimeMap) {
		myLastAccessedTimeMap = theLastAccessedTimeMap;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(XapiKeySecret keySecret1, XapiKeySecret keySecret2) {
		DateTime theTime1 = myLastAccessedTimeMap.get(keySecret1);
		DateTime theTime2 = myLastAccessedTimeMap.get(keySecret2);
		
		if(theTime1.isBefore(theTime2)){
			return 1;
		}
		
		return -1;
	}
}
