/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiExtension.java	Apr 30, 2014
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
import java.util.HashMap;
import java.util.Set;

/**
 * XapiExtension
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *	GREEN
 *
 */
public class XapiExtension {
	
	private HashMap<String, String> myMap;
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiExtension(HashMap<String, String> theMap) {
		myMap = theMap;
	}
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiExtension() {
		myMap = new HashMap<String, String>();
	}
	
	
	
	public void add(String key, String value){
		XapiIRI theKeyIRI = new XapiIRI(key);
		myMap.put(key, value);
	}
	
	
	
	public boolean isEmpty(){
		return myMap.isEmpty();
	}
	
	
	
	public ArrayList<String> getKeys(){
		Set<String> myKeySet = myMap.keySet();
		
		ArrayList<String> keyList = new ArrayList<String>();
		
		for(String s : myKeySet){
			keyList.add(s);
		}
		
		return keyList;
	}
	
	
	
	public String getValueOf(String key){
		return myMap.get(key);
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof XapiExtension){
			XapiExtension theExtension = (XapiExtension) obj;
			return myMap.equals(theExtension.myMap);
		}
		
		return false;
	}
	
	
	
	public String toString(){
		String theString = "extensions: " + myMap + "\n";
		return theString;
	}
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		XapiExtension ext1 = new XapiExtension();
		XapiExtension ext2 = new XapiExtension();
		
		ext1.add("a", "b");
		ext2.add("a", "b");
		
		System.out.println(ext1);
		System.out.println(ext1.equals(ext2));
	}
}
