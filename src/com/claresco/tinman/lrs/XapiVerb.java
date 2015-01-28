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
 * XapiVerb.java
 *
 * Implementation for statement's verb 
 *
 *
 *
 * @author rheza
 * on Jan 14, 2014
 * 
 */

public class XapiVerb {

	/*
	 * Local Variable(s) Description:
	 * 	- [REQUIRED]myId: verb definition
	 *  - myDisplay: human readable representation of the Verb in one or more languages
	 *  	It merely serves as a helper/enhancer
	 */
	
	private XapiIRI myID;
	private XapiLanguageMap myDisplay;
	
	
	public XapiVerb(String verbId){
		this.myDisplay = new XapiLanguageMap();
		
		if (verbId!= null){
			this.myID = new XapiIRI(verbId);
		}
		else{
			this.myID = null;
		}
	}
	
	
	
	public XapiVerb(String verbId, XapiLanguageMap theDisplay){
		this(verbId);
		this.myDisplay = theDisplay;
	}
	
	
	
	public void registerDisplay(String theLanguage, String theWord){
		this.myDisplay.registerLanguage(theLanguage, theWord);
	}
	
	
	
	/**
	 * @return the myDisplay
	 */
	public XapiLanguageMap getDisplay() {
		return myDisplay;
	}
	
	
	
	/**
	 * @return the myId
	 */
	public XapiIRI getId() {
		return myID;
	}
	
	
	
	public String getIdAsString(){
		return myID.toString();
	}
	
	
	
	public boolean hasDisplay(){
		return myDisplay != null && !myDisplay.isEmpty();
	}
	
	
	
	public boolean isVoided(){
		return myID.toString().equals("http://adlnet.gov/expapi/verbs/voided");
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String theString = "Verb : \n" + "ID: " + this.myID.toString() + "\nDisplay: " +
				myDisplay.toString() + "\n";
		return theString;	
 	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof XapiVerb){
			XapiVerb theVerb = (XapiVerb) obj;
			return myID.equals(theVerb.myID) && myDisplay.equals(theVerb.myDisplay);
		}
		return false;
	}
	
	
	
	public static void main(String[] args) {
		XapiVerb theV = new XapiVerb("http://adlnet.gov/expapi/verbs/launched");
		
		System.out.println(theV);
	}

}
