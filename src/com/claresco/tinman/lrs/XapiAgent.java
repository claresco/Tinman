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
 * XapiAgent.java
 *
 * When actor in the statement is an agent, this class will be instantiated.
 *
 *
 *
 * @author rheza
 * on Jan 13, 2014
 * 
 */

public class XapiAgent implements XapiActor, XapiObject{
	
	/*
	 * Local Variables Descriptions:
	 * 	- myObjectType: value always "Agent"
	 *  - myName: name of the agent
	 *	- [Required]myInverseFuncId : identifier unique to the agent
	 */
	
	private String myObjectType;
	private String myName;
	private XapiInverseFunctionalIdentifier myInverseFunctionalIdentifier; 
	
	
	
	/**
	 * Description:
	 *
	 * Params:
	 *  - name : name of the agent.
	 *
	 */
	public XapiAgent(String name, XapiInverseFunctionalIdentifier inverseFuncId) {
		this.myObjectType = "Agent";
		this.myName = name;
		
		// ***Important*** can only be one type of inverse functional identifier used!
		this.myInverseFunctionalIdentifier = inverseFuncId;
	}

	
	
	public String getObjectType(){
		return this.myObjectType;
	}
	
	
	
	public String getName(){
		return this.myName;
	}
	
	
	
	public XapiInverseFunctionalIdentifier getInverseFuncId(){
		return this.myInverseFunctionalIdentifier;
	}
	
	
	
	public boolean hasName(){
		return myName != null;
	}
	
	
	
	public boolean hasInverseFuncID(){
		return myInverseFunctionalIdentifier != null;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.lrs.XapiActor#isAgent()
	 */
	@Override
	public boolean isAgent() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.claresco.tinman.lrs.XapiActor#isGroup()
	 */
	@Override
	public boolean isGroup() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	public boolean isValid(){
		return (myInverseFunctionalIdentifier != null) && (myInverseFunctionalIdentifier.isValid());
	}
	
	
	/**
	 * Description:
	 *		For debugging purposes for now
	 *
	 * Params:
	 *
	 */
	public String toString(){
		return "\n\t[Agent-name: " + this.myName + "\n\tAgent-id: " + this.myInverseFunctionalIdentifier + "]";
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof XapiAgent){
			XapiAgent theAgent = (XapiAgent) obj;
			if(!myObjectType.equals(theAgent.myObjectType)){
				return false;
			}if(hasName() && !myName.equals(theAgent.myName)){
				return false;
			}if(hasInverseFuncID() && !myInverseFunctionalIdentifier.equals(theAgent.myInverseFunctionalIdentifier)){
				return false;
			}
			return true;
		}
		return false;
	}
	
	
	
	
	
	public static void main(String[] args) {
		XapiAccount ac1 = new XapiAccount("a", "b");
		XapiAccount ac2 = new XapiAccount("a", "b");
		
		XapiInverseFunctionalIdentifier id1 = new XapiInverseFunctionalIdentifier(null, null, null, ac1);
		XapiInverseFunctionalIdentifier id2 = new XapiInverseFunctionalIdentifier(null, null, null, ac2);
		
		System.out.println(id1.equals(id2));
		
		XapiAgent ag1 = new XapiAgent("abc", id1);
		XapiAgent ag2 = new XapiAgent("abc", id2);
		
		System.out.println(ag1.equals(ag2));
	}
}
