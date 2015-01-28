/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiPerson.java	Jul 11, 2014
 *
 * Copyright 2014 Claresco Corporation, Berkeley, CA 94704. All Rights Reserved.
 *
 * This software is the proprietary information of Claresco Corporation.
 * Use is subject to license terms.
 *
 * Author : Rheza
 *
 */
package com.claresco.tinman.lrs;

import java.util.ArrayList;
import java.util.Iterator;

import com.claresco.tinman.servlet.XapiNotAuthorizedException;

/**
 * XapiPerson
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiPerson{
	
	private String myObjectType = "Person";
	private ArrayList<String> myNames;
	private ArrayList<XapiIRI> myMboxes;
	private ArrayList<String> myMboxSha1sums;
	private ArrayList<String> myOpenIDs;
	private ArrayList<XapiAccount> myAccounts;
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiPerson(ArrayList<String> theName, ArrayList<XapiIRI> theMbox, ArrayList<String>
			theMboxSha1sum, ArrayList<String> theOpenID, ArrayList<XapiAccount> theAccount){
		myNames = theName;
		myMboxes = theMbox;
		myMboxSha1sums = theMboxSha1sum;
		myOpenIDs = theOpenID;
		myAccounts = theAccount;
	}

	
	
	public ArrayList<String> getNames(){
		return myNames;
	}
	
	
	
	public ArrayList<XapiIRI> getMboxes(){
		return myMboxes;
	}
	
	
	
	public ArrayList<String> getMboxSha1sums(){
		return myMboxSha1sums;
	}
	
	
	
	public ArrayList<String> getOpenIDs(){
		return myOpenIDs;
	}
	
	
	
	public ArrayList<XapiAccount> getAccounts(){
		return myAccounts;
	}
	
	
	
	public boolean hasNames(){
		return myNames != null && !myNames.isEmpty();
	}
	
	
	
	public boolean hasMboxes(){
		return myMboxes != null && !myMboxes.isEmpty();
	}
	
	
	
	public boolean hasMboxSha1sums(){
		return myMboxSha1sums != null && !myMboxSha1sums.isEmpty();
	}
	
	
	
	public boolean hasOpendIDs(){
		return myOpenIDs != null && !myOpenIDs.isEmpty();
	}
	
	
	
	public boolean hasAccounts(){
		return myAccounts != null && !myAccounts.isEmpty();
	}
	
	
	
	/**
	 * 
	 * Definition:
	 * 	Helper method for Basic Authorization
	 *	Check if the person contains the agent
	 *
	 * Params:
	 *
	 *
	 */
	public boolean containsActor(XapiActor theActor){
		if(theActor.hasName()){
			if(!myNames.contains(theActor.getName())){
				return false;
			}
		}
		if(theActor.hasInverseFuncID()){
			XapiInverseFunctionalIdentifier theID = theActor.getInverseFuncId();
			switch (theID.getIdentifierIndex()) {
			case 1:
				if(!myMboxes.contains(theID.getMbox())){
					return false;
				}
				break;
				
			case 2:
				if(!myMboxSha1sums.contains(theID.getMboxSha1Sum())){
					return false;
				}
				break;
			
			case 3:
				if(!myOpenIDs.contains(theID.getOpenId().toString())){
					return false;
				}
				break;
				
			case 4:
				if(!myAccounts.contains(theID.getAccount())){
					return false;
				}
				break;

			default:
				return false;
			}
		}else{
			if(theActor instanceof XapiGroup){
				// Run the function against the members of the group
				XapiGroup theGroup = (XapiGroup) theActor;
				if(theGroup.hasMember()){
					for(XapiAgent ag : theGroup.getMember()){
						// Recursively check for the members
						if(!containsActor(ag)){
							return false;
						}
					}
				}
			}else{
				return false;
			}
		}
		
		return true;
	}
	
	
	
	
	
	
	
	/**
	 * 
	 * Definition:
	 *	Helper function for the servlet checks permission
	 *	For Basic Authorization 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiAgent getAnyAgent(){
		String theName = null;
		String theMbox = null;
		String theMboxSha1sum = null;
		String theOpenID = null;
		XapiAccount theAccount = null;
		
		if(hasNames()){
			theName = myNames.get(0);
		}
		
		if(hasMboxes()){
			theMbox = myMboxes.get(0).toString();
		}
		
		if(hasMboxSha1sums()){
			theMboxSha1sum = myMboxSha1sums.get(0);
		}
		
		if(hasOpendIDs()){
			theOpenID = myMboxSha1sums.get(0);
		}
		
		if(hasAccounts()){
			theAccount = myAccounts.get(0);
		}
		
		return new XapiAgent(theName, new XapiInverseFunctionalIdentifier(theMbox,
				theMboxSha1sum, theOpenID, theAccount));
	}
	
	
	
	public static void main(String[] args) {
		/**
		XapiPerson thePerson = new XapiPerson();
		XapiAgent theAg = new XapiAgent("john brooks", new XapiInverseFunctionalIdentifier("mailto:jb.com", null, null, null));
		
		thePerson.addActor(theAg);
		
		XapiAgent theAg2 = new XapiAgent("john brooks", new XapiInverseFunctionalIdentifier("mailto:jb.com", null, null, null));
		System.out.println(thePerson.contains(theAg2));
		**/
		
		ArrayList<String> theName = new ArrayList<String>();
		theName.add("Mars"); 
		theName.add("Mecury");
		
		ArrayList<XapiAccount> theAccount = new ArrayList<XapiAccount>();
		theAccount.add(new XapiAccount("homePage", "name"));
		
		XapiPerson theP = new XapiPerson(theName, null, null, null, theAccount);
		
		System.out.println(theP.hasNames());
		System.out.println(theP.hasMboxes());
		System.out.println(theP.hasAccounts());
	}
}
