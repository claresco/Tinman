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

import java.util.ArrayList;

/**
 * XapiContextActivities.java
 *
 * Context of activity
 *
 *
 *
 * @author rheza
 * on Mar 5, 2014
 * 
 */

public class XapiContextActivities {

	private ArrayList<XapiActivity> myParent;
	private ArrayList<XapiActivity> myGrouping;
	private ArrayList<XapiActivity> myCategory;
	private ArrayList<XapiActivity> myOther;
	
	
	
	public XapiContextActivities(ArrayList<XapiActivity> theParent, ArrayList<XapiActivity> theGrouping){
		this.myParent = theParent;
		this.myGrouping = theGrouping;
		this.myCategory = null;
		this.myOther = null;
	}
	
	
	
	public XapiContextActivities(ArrayList<XapiActivity> theParent, ArrayList<XapiActivity> theGrouping,
			ArrayList<XapiActivity> theCategory, ArrayList<XapiActivity> theOther){
		this.myParent = theParent;
		this.myGrouping = theGrouping;
		this.myCategory = theCategory;
		this.myOther = theOther;
	}
	

	
	public ArrayList<XapiActivity> getParent(){
		return this.myParent;
	}
	
	
	
	public ArrayList<XapiActivity> getGrouping(){
		return this.myGrouping;
	}
	
	
	
	public ArrayList<XapiActivity> getCategory(){
		return this.myCategory;
	}
	
	
	
	public ArrayList<XapiActivity> getOther(){
		return this.myOther;
	}
	
	
	
	public boolean hasParent(){
		return this.myParent != null;
	}
	
	
	
	public boolean hasGrouping(){
		return this.myGrouping != null;
	}
	
	
	
	public boolean hasCategory(){
		return this.myCategory != null;
	}
	
	
	
	public boolean hasOther(){
		return this.myOther != null;
	}
	
	
	
	public boolean isEmpty(){
		return !hasParent() && !hasGrouping() && !hasCategory() && !hasOther();
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s = "parent: ";
		
		if(hasParent()){
			for(XapiActivity a : myParent){
				s = s + a.toString() + ", ";
			}
		}
		if(hasGrouping()){
			s = s + "\ngrouping: ";
			for(XapiActivity a : myGrouping){
				s = s + a.toString() + ", ";
			}
		}
		
		return s;
	}
}
