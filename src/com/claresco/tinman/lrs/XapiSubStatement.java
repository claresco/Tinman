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
 * XapiSubStatement.java
 *
 * Implementation for substatements
 *
 *
 *
 * @author rheza
 * on Jan 15, 2014
 * 
 */

public class XapiSubStatement extends XapiStatement implements XapiObject{
	
	private String myObjectType;
	private XapiActor myActor;
	private XapiVerb myVerb;
	private XapiObject myObject;
	
	
	
	public XapiSubStatement(XapiActor theActor, XapiVerb theVerb, XapiObject theObject){
		super(theActor, theVerb, theObject, null, null, null);
		myObjectType = "SubStatement";
		myActor = theActor;
		myVerb = theVerb;
		myObject = theObject;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.lrs.XapiObject#getObjectType()
	 */
	@Override
	public String getObjectType() {
		return this.myObjectType;
	}
	
	
	
	public XapiActor  getActor() {
		return this.myActor;
	}
	
	
	
	public XapiVerb getVerb(){
		return this.myVerb;
	}
	
	
	
	public XapiObject getObject(){
		return this.myObject;
	}
	
	
	
	public boolean isValid(){
		return hasActor() && hasVerb() && hasObject() && !isNested();	
	}
	
	
	
	public boolean isNested(){
		return this.myObject instanceof XapiSubStatement;
	}

	
	
	public String toString(){
		return this.myActor + ", " + this.myVerb + ", " + this.myObject;
	}
}
