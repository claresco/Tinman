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

/**
 * XapiActivity.java
 *
 * Activity which forms the object part of the statement
 *
 *
 *
 * @author rheza
 * on Jan 16, 2014
 * 
 */

public class XapiActivity implements XapiObject {
	
	/*
	 * Local Variable(s) Description:
	 * 	- myObjectType: value always "Activity"
	 *  - [REQUIRED] myId: identifier of single unique activity 
	 *  - myDefinition : an object that will define the activity
	 */
	
	private String myObjectType;
	private XapiIRI myId;
	private XapiActivityDefinition myDefinition;
	
	
	
	public XapiActivity(String activityId){
		this.myObjectType = "Activity";
		this.myId = new XapiIRI(activityId);
		this.myDefinition = null;
	}
	
	
	
	public XapiActivity(String activityId, XapiActivityDefinition activityDef){
		this.myObjectType = "Activity";
		this.myId = new XapiIRI(activityId);
		this.myDefinition = activityDef;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.lrs.XapiObject#getObjectType()
	 */
	@Override
	public String getObjectType() {
		return this.myObjectType;
	}
	
	
	public XapiIRI getId(){
		return this.myId;
	}
	
	
	
	public XapiActivityDefinition getDefinition(){
		return this.myDefinition;
	}
	
	
	
	public boolean hasDefinition(){
		return this.myDefinition != null;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String theString = "Activity\n" + "Id: " + myId.toString()  + "\n" + myDefinition.toString() + "\n";
		return theString;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof XapiActivity){
			XapiActivity theActivity = (XapiActivity) obj;
			if(myDefinition != null){
				if(myId.equals(theActivity.myId) && myDefinition.equals(theActivity.myDefinition)){
					return true;
				}
			}else{
				return myId.equals(theActivity.myId);
			}
		}
		return false;
	}
}
