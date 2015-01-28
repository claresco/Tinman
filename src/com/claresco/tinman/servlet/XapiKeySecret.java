/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiLogin.java	May 13, 2014
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

import java.util.HashMap;

import com.claresco.tinman.lrs.XapiAccount;

/**
 * XapiLogin
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiKeySecret {

	private String myKey;
	private String mySecret;
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiKeySecret(String theKey, String theSecret) {
		myKey = theKey;
		mySecret = theSecret;
	}
	
	
	
	public String getKey(){
		return myKey;
	}
	
	
	
	public String getSecret(){
		return mySecret;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int prime = 6397;
		return myKey.hashCode() % prime + mySecret.hashCode();
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof XapiKeySecret){
			XapiKeySecret theKeySecret = (XapiKeySecret) obj;
			return myKey.equals(theKeySecret.myKey) && mySecret.equals(theKeySecret.mySecret);
			}
		return false;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{" + myKey + " : " + mySecret + "}";
	}
	
	
	
	public static void main(String[] args) {
		XapiKeySecret k1 = new XapiKeySecret("a", "b");
		XapiKeySecret k2 = new XapiKeySecret("a", "b");
		
		System.out.println(k1.equals(k2));
		
		HashMap<XapiKeySecret, String> mapper = new HashMap<XapiKeySecret, String>();
		mapper.put(k1, "c");
		System.out.println(mapper.containsKey(k2));
	}
}
