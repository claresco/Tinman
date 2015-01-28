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
 * XapiInteractionComponent.java
 *
 * Component of the interaction (XapiInteraction)
 *
 *
 *
 * @author rheza
 * on Mar 5, 2014
 * 
 */

public class XapiInteractionComponent {
	
	private String myID;
	private XapiLanguageMap myDescription;
	
	public XapiInteractionComponent(String theID, XapiLanguageMap theDescription){
		this.myID = theID;
		this.myDescription = theDescription;
	}
	
	public String getID(){
		return this.myID;
	}
	
	public XapiLanguageMap getDescription(){
		return this.myDescription;
	}
	
	public boolean hasID(){
		return this.myID != null;
	}
	
	public boolean hasDescription(){
		return this.myDescription != null && !this.myDescription.isEmpty();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "id: " + myID + "|description: " + myDescription;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof XapiInteractionComponent){
			XapiInteractionComponent theComp = (XapiInteractionComponent) obj;
			if(hasID() && !myID.equals(theComp.myID)){
				return false;
			}
			if(theComp.hasID() && !theComp.myID.equals(myID)){
				return false;
			}
			if(hasDescription() && !myDescription.equals(theComp.myDescription)){
				return false;
			}
			if(theComp.hasDescription() && !theComp.myDescription.equals(myDescription)){
				return false;
			}
			return true;
		}
		return false;
	}
	
	
	
	public boolean isEmpty(){
		return !hasID() && !hasDescription();
	}
	
	
	
	
	
	public static void main(String[] args) {
		XapiLanguageMap lmap = new XapiLanguageMap();
		XapiLanguageMap lmap2 = new XapiLanguageMap();
		
		lmap.registerLanguage("a", "b");
		lmap2.registerLanguage("a", "b");
		
		XapiInteractionComponent ic1 = new XapiInteractionComponent("id", lmap);
		XapiInteractionComponent ic2 = new XapiInteractionComponent("id", lmap2);
		
		System.out.println(ic2.equals(ic1));
	}
}
