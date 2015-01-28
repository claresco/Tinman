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

import java.util.UUID;

/**
 * XapiContext.java
 *
 * The context of the statement
 *
 * Status:
 * 	YELLOW
 *
 * @author rheza
 * on Mar 5, 2014
 * 
 */

public class XapiContext {

	private XapiActor myInstructor;
	private XapiGroup myTeam;
	private UUID myRegistration;
	private String myRevision;
	private String myPlatform;
	private String myLanguage;
	private XapiContextActivities myContextActivities;
	private XapiStatementRef myStatementReference;
	private XapiExtension myExtension;
	
	
	/**
	 * 
	 * Description:
	 *	Constructor
	 *
	 * Params:
	 *
	 */
	public XapiContext(UUID theRegistration, String theRevision, String thePlatform, String theLanguage, 
			XapiContextActivities theContextActivities){
		this.myRegistration = theRegistration;
		this.myRevision = theRevision;
		this.myLanguage = theLanguage;
		this.myPlatform = thePlatform;
		this.myContextActivities = theContextActivities;
		this.myInstructor = null;
		this.myTeam = null;
	}
	
	
	
	public XapiContext(XapiActor theInstructor, XapiGroup theTeam, UUID theRegistration, String theRevision
			, String theLanguage, String thePlatform, XapiContextActivities theContextActivities, 
			XapiStatementRef theStatementReference, XapiExtension theExtension){
		this.myInstructor = theInstructor;
		this.myTeam = theTeam;
		this.myRegistration = theRegistration;
		this.myRevision = theRevision;
		this.myLanguage = theLanguage;
		this.myPlatform = thePlatform;
		this.myContextActivities = theContextActivities;
		this.myStatementReference = theStatementReference;
		this.myExtension = theExtension;
	}
	
	
	
	public UUID getRegistration(){
		return this.myRegistration;
	}
	
	
	
	public String getRevision(){
		return this.myRevision;
	}
	
	
	
	public String getPlatform(){
		return this.myPlatform;
	}
	
	
	
	public String getLanguage(){
		return this.myLanguage;
	}
	
	
	
	public XapiContextActivities getContextActivities(){
		return this.myContextActivities;
	}
	
	
	
	public XapiActor getInstructor(){
		return this.myInstructor;
	}
	
	
	
	public XapiGroup getTeam(){
		return this.myTeam;
	}
	
	
	
	public XapiStatementRef getStatementReference(){
		return this.myStatementReference;
	}
	
	
	
	public XapiExtension getExtensions(){
		return this.myExtension;
	}
	
	
	
	public boolean hasRegistration(){
		return this.myRegistration != null;
	}
	
	
	
	public boolean hasRevision(){
		return this.myRevision != null;
	}
	
	
	
	public boolean hasLanguage(){
		return this.myLanguage != null;
	}
	
	
	
	public boolean hasPlatform(){
		return this.myPlatform != null;
	}
	
	
	
	public boolean hasContextActivities(){
		return this.myContextActivities != null;
	}
	
	
	
	public boolean hasInstructor(){
		return this.myInstructor != null;
	}
	
	
	
	public boolean hasTeam(){
		return this.myTeam != null;
	}
	
	
	
	public boolean hasStatementReference(){
		return this.myStatementReference != null;
	}
	
	
	
	public boolean hasExtensions(){
		return myExtension != null && !myExtension.isEmpty();
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s = "registration: " + myRegistration.toString() +"\n";
		s += "revision: " + myRevision + "\n";
		s += "language: " + myLanguage + "\n";
		s += "platform: " + myPlatform + "\n";
		s += "contextActivities: " + myContextActivities + "\n";
		s += "statementRef: " + myStatementReference + "\n";
		s += "extensions: " + myExtension + "\n";
		return s;
	}
}
