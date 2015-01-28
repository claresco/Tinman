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
import java.util.HashMap;

/**
 * XapiActivityDefinition.java
 *
 * The definition of the activity
 *
 *
 *
 * @author rheza
 * on Jan 16, 2014
 * 
 */

public class XapiActivityDefinition {
	
	// worry about extension!
	private XapiLanguageMap myName;
	private XapiLanguageMap myDescription;
	// Corresponds to the activity type
	private XapiIRI myType;						
	private XapiIRL myMoreInfo;
	private XapiInteraction myInteractionProperties;
	private XapiExtension myExtension;
	
	
	/**
	 * 
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiActivityDefinition(XapiLanguageMap activityName, XapiLanguageMap activityDescription,
			String activityType, String moreInfo, XapiInteraction theInteraction,
			XapiExtension theExtension){
		this.myName = activityName;
		this.myDescription = activityDescription;
		
		if(activityType != null){
			this.myType = new XapiIRI(activityType);
		}else{
			this.myType = null;
		}
		
		if(moreInfo != null){
			this.myMoreInfo = new XapiIRL(moreInfo);
		}else{
			this.myMoreInfo = null;
		}
		
		this.myInteractionProperties = theInteraction;
		this.myExtension = theExtension;
	}
	
	
	
	public XapiLanguageMap getName(){
		return this.myName;
	}
	
	
	
	public XapiLanguageMap getDescription(){
		return this.myDescription;
	}
	
	
	
	public XapiIRI getType(){
		return this.myType;
	}
	

	
	public XapiInteraction getInteractionProperties(){
		return this.myInteractionProperties;
	}
	
	
	
	public XapiExtension getExtension(){
		return this.myExtension;
	}
	
	
	
	public XapiIRL getMoreInfo(){
		return this.myMoreInfo;
	}
	
	
	
	public boolean hasName(){
		return this.myName != null && !this.myName.isEmpty();
	}
	
	
	
	public boolean hasDescription(){
		return this.myDescription != null && !this.myDescription.isEmpty();
	}
	
	
	
	public boolean hasMoreInfo(){
		return this.myMoreInfo != null;
	}
	
	
	
	public boolean hasType(){
		return this.myType != null;
	}
	
	
	
	public boolean hasInteractionProperties(){
		return this.myInteractionProperties != null && !this.myInteractionProperties.isEmpty();
	}
	
	
	
	public boolean hasExtension(){
		return this.myExtension != null;
	}
	
	
	
	public boolean isEmpty(){
		return !hasName() && !hasDescription() && !hasType() && !hasInteractionProperties()
				&& !hasExtension() && !hasMoreInfo();
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String theString = "Activity Definition:\n";
		theString += "name: " + myName + "\n";
		theString += "description: " + myDescription + "\n";
		theString += "type: " + myType + "\n";
		theString += myInteractionProperties;
		theString += myExtension;
		return theString;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		
		if(obj instanceof XapiActivityDefinition){
			XapiActivityDefinition theDef = (XapiActivityDefinition) obj;
			if(hasDescription() && !myDescription.equals(theDef.getDescription())){
				return false;
			}
			if(theDef.hasDescription() && !theDef.myDescription.equals(myDescription)){
				return false;
			}
			if(hasName() && !myName.equals(theDef.myName)){
				return false;
			}
			if(theDef.hasName() && !theDef.myName.equals(myName)){
				return false;
			}
			if(hasMoreInfo() && !myMoreInfo.equals(theDef.getMoreInfo())){
				return false;
			}
			if(theDef.hasMoreInfo() && !theDef.myMoreInfo.equals(myMoreInfo)){
				return false;
			}
			if(hasType() && !myType.equals(theDef.getType())){
				return false;
			}
			if(theDef.hasType() && !theDef.getType().equals(myType)){
				return false;
			}
			if(hasInteractionProperties() && !myInteractionProperties.equals(theDef.
					getInteractionProperties())){
				return false;
			}
			if(theDef.hasInteractionProperties() && !theDef.myInteractionProperties.
					equals(myInteractionProperties)){
				return  false;
			}
			if(hasExtension() && !myExtension.equals(theDef.getExtension())){
				return false;
			}
			if(theDef.hasExtension() && !theDef.myExtension.equals(myExtension)){
				return false;
			}
			return true;
		}
		return false;
	}

	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		XapiExtension ext1 = new XapiExtension();
		XapiExtension ext2 = new XapiExtension();
		
		ext1.add("a", "b");
		ext2.add("a", "b");
		
		XapiLanguageMap lmap = new XapiLanguageMap();
		XapiLanguageMap lmap2 = new XapiLanguageMap();
		
		lmap.registerLanguage("a", "b");
		lmap2.registerLanguage("a", "b");
		
		XapiInteractionComponent ic1 = new XapiInteractionComponent("id", lmap);
		XapiInteractionComponent ic2 = new XapiInteractionComponent("id", lmap2);
		
		ArrayList<XapiInteractionComponent> theICArray1 = new ArrayList<XapiInteractionComponent>();
		ArrayList<XapiInteractionComponent> theICArray2 = new ArrayList<XapiInteractionComponent>();
		
		theICArray1.add(ic1);
		theICArray2.add(ic2);
		
		ArrayList<String> as1 = new ArrayList<String>();
		as1.add("1");
		XapiInteraction i1 = new XapiInteraction("a", as1, null, null, null, null, null);
		
		ArrayList<String> as2 = new ArrayList<String>();
		as2.add("1");
		XapiInteraction i2 = new XapiInteraction("a", as2, null, null, null, null, null);
		
		XapiActivityDefinition theD1 = new XapiActivityDefinition(lmap, lmap, "type", null, i1, ext1);
		XapiActivityDefinition theD2 = new XapiActivityDefinition(lmap2, lmap2, "type", null, i2, ext2);
		
		System.out.println(theD1);
		
		System.out.println(theD1.equals(theD2));
		System.out.println(theD2.equals(theD1));
	}
}
