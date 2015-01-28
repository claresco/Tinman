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

import java.util.HashMap;

/**
 * XapiAccount.java
 *
 * Represents the object account of actor
 *
 *
 *
 * @author rheza
 * on Jan 15, 2014
 * 
 */

public class XapiAccount {

	/* 
	 * Field description:
	 * 	[Required]myHomePage : The canonical home page for the system the account is on.
	 * 	[Required]myName : unique id used to log on to this account.
	 */
	private XapiIRL myHomePage;
	private String myName;
	
	
	
	public XapiAccount(String homePage, String name){
		
		if(homePage == null){
			myHomePage = null;
		}
		else{
			myHomePage = new XapiIRL(homePage);
		}
		myName = name;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Account-name: " + myName + " |Account-HomePage: " + this.myHomePage +"\n";
	}
	
	
	/**
	 * @return the myHomePage
	 */
	public XapiIRL getHomePage() {
		return myHomePage;
	}
	
	
	/**
	 * @return the myName
	 */
	public String getName() {
		return myName;
	}
	
	
	
	public boolean hasHomePage(){
		return myHomePage != null;
	}
	
	
	
	public boolean hasName(){
		return myName != null;
	}
	
	
	
	public boolean isValid(){
		return (hasHomePage() && hasName());
	}
	
	
	
	public boolean isEmpty(){
		return !hasHomePage() && !hasName();
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int prime = 3323;
		return myName.hashCode() % prime + myHomePage.toString().hashCode();
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof XapiAccount){
			XapiAccount theAccount = (XapiAccount) obj;
			return myHomePage.equals(theAccount.getHomePage()) &&
			(myName.equals(theAccount.getName()));
			}
		return false;
	}
	

	
	public static void main(String[] args) {
		XapiAccount ac1 = new XapiAccount("a", "b");
		XapiAccount ac2 = new XapiAccount("a", "b");
		
		System.out.println(ac1);
		System.out.println(ac1 == ac2);
		System.out.println(ac1.equals(ac2));
		
		HashMap<XapiAccount, String> map = new HashMap<XapiAccount, String>();
		map.put(ac2, "c");
		map.put(ac2, "d");
		map.put(ac2, "e");
		System.out.println(map.get(ac1));
		
		for(XapiAccount acct : map.keySet()){
			System.out.println("key : " + acct + " , value: " + map.get(acct));
		}
		
		String[] z = new String[]{"a", "b", "c"};
		String z3 = z[3];
	}
}
