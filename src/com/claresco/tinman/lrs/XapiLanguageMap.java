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

package com.claresco.tinman.lrs;

import java.util.HashMap;
import java.util.Set;
import java.util.Map;

import org.apache.jena.riot.web.*;

/**
 * XapiLanguageMap.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Jan 16, 2014
 * 
 */

public class XapiLanguageMap {
	
	private HashMap<String, String> myLanguageMapping;

	
	
	public XapiLanguageMap(){
		this.myLanguageMapping = new HashMap<String, String>();
	}
	
	
	
	public void registerLanguage(String theLanguage, String theWord){
		this.myLanguageMapping.put(theLanguage, theWord);
	}
	
	
	public boolean isEmpty(){
		return myLanguageMapping == null || myLanguageMapping.isEmpty();
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.myLanguageMapping.toString();
	}

	
	
	public String[][] getLanguageMapAsArray(){
		Set<Map.Entry<String,String>> myEntrySet = this.myLanguageMapping.entrySet();
		
		String[][] myArray = new String[myEntrySet.size()][2];

		int j = 0;

		for (Map.Entry<String, String> i : this.myLanguageMapping.entrySet()){
			myArray[j][0] = i.getKey();
			myArray[j][1] = i.getValue();
			j++;
		}
		
		return myArray;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof XapiLanguageMap){
			XapiLanguageMap theLmap = (XapiLanguageMap) obj;
			if(myLanguageMapping.equals(theLmap.myLanguageMapping)){
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		XapiLanguageMap lmap = new XapiLanguageMap();
		XapiLanguageMap lmap2 = new XapiLanguageMap();
		
		lmap.registerLanguage("a", "b");
		lmap2.registerLanguage("a", "b");
		
		System.out.println(lmap2.equals(lmap));
	}
}
