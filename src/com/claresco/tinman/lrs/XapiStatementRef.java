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

import java.util.*;

/**
 * XapiStatementRef.java
 *
 * Implementation for statement reference
 *
 *
 *
 * @author rheza
 * on Jan 15, 2014
 * 
 */

public class XapiStatementRef implements XapiObject {
	
	private String myObjectType;
	private UUID myId;

	
	
	public XapiStatementRef(String theID){
		this.myObjectType = "StatementRef";
		this.myId = UUID.fromString(theID);
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.lrs.XapiObject#getObjectType()
	 */
	@Override
	public String getObjectType() {
		return this.myObjectType;
	}
	
	
	
	public UUID getId(){
		return this.myId;
	}
	
	
	
	public boolean hasID(){
		return this.myId != null;
	}
	
	
	
	public boolean isValid(){
		return hasID();
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[Statement reference: " + this.myId.toString() + "]";
	}
}
