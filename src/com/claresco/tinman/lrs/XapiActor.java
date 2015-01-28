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
 * XapiActor.java
 *
 * This interface is for actor in the statement.
 * Agent and group will implement this class
 *
 *
 * @author rheza
 * on Jan 13, 2014
 * 
 * Needs to add inverse functional identifier which I need to write the class for
 * 
 */
 
public interface XapiActor {

	public String getObjectType();
	
	public boolean isAgent();
	
	public boolean isGroup();
	
	public String getName();
	
	public XapiInverseFunctionalIdentifier getInverseFuncId();
	
	public boolean hasName();
	
	public boolean hasInverseFuncID();
	
	public String toString();
}
