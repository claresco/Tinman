/**
 * ClarescoExperienceAP	I
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
 * XAPI_Group.java
 *
 * Group object which can be either object or actor of the statement
 *
 *
 *
 * @author rheza
 * on Jan 13, 2014
 * 
 * Needs to figure out the identifier first
 * 
 */

public class XapiGroup implements XapiActor, XapiObject {

	/*
	 * Local Variables Descriptions:
	 * 	- myObjectType: value always "Group"
	 *  - myName: name of the group
	 *  - myMember : list of Agents who is part of this group
	 *	- myInverseFuncId : identifier unique to the group
	 *
	 * There are two kinds of group:
	 * 	- Anonymous group: myMember can not be null, myInverseFuncId must be null
	 * 	- Identified group: myMember can be null, but myInverseFucnId must not be null
	 */
	
	private String myObjectType;
	private String myName;
	private XapiAgent[] myMember;
	private XapiInverseFunctionalIdentifier myInverseFuncId;

	
	
	/**
	 * Description:
	 *
	 * Params:
	 *
	 */
	public XapiGroup(String name, XapiAgent[] member, XapiInverseFunctionalIdentifier inverseFuncId) {
		this.myObjectType = "Group";
		this.myName = name;
		this.myMember = member;
		this.myInverseFuncId = inverseFuncId;
	}

	
	
	public XapiGroup(String name, ArrayList<XapiAgent> member, XapiInverseFunctionalIdentifier inverseFuncId) {
		this.myObjectType = "Group";
		this.myName = name;
		this.myInverseFuncId = inverseFuncId;
		
		this.myMember = new XapiAgent[member.size()];
		int i = 0;
		for(XapiAgent a : member){
			this.myMember[i] = a;
			i++;
		}
	}
	
	
	
	public String getObjectType(){
		return this.myObjectType;
	}
	
	
	
	public String getName(){
		return this.myName;
	}
	
	
	
	public XapiInverseFunctionalIdentifier getInverseFuncId(){
		return this.myInverseFuncId;
	}
	
	
	
	public XapiAgent[] getMember(){
		return this.myMember;
	}
	
	
	
	public boolean hasName(){
		return this.myName != null;
	}
	
	
	
	public boolean hasInverseFuncID(){
		return this.myInverseFuncId != null;
	}
	
	
	
	public boolean hasMember(){
		return this.myMember != null;
	}
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.lrs.XapiActor#isAgent()
	 */
	@Override
	public boolean isAgent() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.lrs.XapiActor#isGroup()
	 */
	@Override
	public boolean isGroup() {
		return true;
	}
	
	
	
	public boolean isValid(){
		return hasMember() || hasInverseFuncID();
	}
	
	
	
	/**
	 * Description:
	 *		For debugging purposes for now
	 * 
	 * Params:
	 *
	 */
	public String memberToString(){
		String localString = "";
		if(hasMember()){
			for (int i = 0; i < myMember.length; i++) {
				localString = localString + myMember[i].toString() + ",\n";
			}
			return localString;
		}
		return null;
	}
	
	
	
	/**
	 * Description:
	 *		For debugging purposes for now
	 *
	 * Params:
	 *
	 */
	public String toString(){
		return "\n\t[Group-name:" + this.myName + "\n\tGroup-id:" + this.myInverseFuncId + "\n\tGroup-member:" + this.memberToString() + "]";
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof XapiGroup){
			XapiGroup theG = (XapiGroup) obj;
			if(!myObjectType.equals(theG.myObjectType)){
				return false;
			}
			if(hasName()  && !myName.equals(theG.getName())){
				return false;
			}
			if(hasInverseFuncID() && !myInverseFuncId.equals(theG.myInverseFuncId)){
				return false;
			}
		}
		
		return false;
	}
}
