
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

import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.hp.hpl.jena.sdb.sql.MySQLEngineType;

/**
 * XAPIInverseFunctionIdentifier.java
 *
 * Identifier for either agent or group
 *	
 * Status:
 * 	GREEN
 * 
 *
 * @author rheza
 * on Jan 13, 2014
 * 
 */

public class XapiInverseFunctionalIdentifier {

	/*
	 * Local Variables Descriptions:
	 * 	- myMbox: email address that is unique to this actor/group
	 *  - myMboxSha1sum: SHA1 hash of a myMbox
	 *  - myOpenID : Open id that uniquely identifies an agent
	 *	- myAccount : user account object
	 */

	private XapiIRI myMbox;
	private String myMboxSha1sum;
	private URI myOpenID;
	private XapiAccount myAccount;
	private int myIdentifierSum;	// how many identifiers that it has
	private int myIdentifierIndex;	// which identifier it has



	/**
	 * 
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiInverseFunctionalIdentifier(String emailAddress, String theMboxSha1Sum, String openID, XapiAccount account){
		this.myAccount = account;
		if(account != null && account.isEmpty()){
			this.myAccount = null;
		}

		if(openID != null){
			this.myOpenID = URI.create(openID);
		}else{
			this.myOpenID = null;
		}

		this.myMboxSha1sum = theMboxSha1Sum;

		if (emailAddress != null){
			this.myMbox = new XapiIRI(emailAddress);
		}
		else{
			this.myMbox = null;
		}

		myIdentifierSum = 0;
		if(myAccount != null){
			myIdentifierSum++;
			myIdentifierIndex = 4;
		}
		if (myMbox != null) {
			myIdentifierSum++;
			myIdentifierIndex = 1;
		}
		if(myMboxSha1sum != null){
			myIdentifierSum++;
			myIdentifierIndex = 2;
		}
		if(myOpenID != null){
			myIdentifierSum++;
			myIdentifierIndex = 3;
		}
	}



	/**
	 * Description:
	 *
	 * Params:
	 *
	 */
	public boolean isEmpty() {
		return !hasAccount() && !hasMbox() && !hasMboxSha1Sum() && !hasOpenId();
	}



	/**
	 * Description:
	 * this function will be called when we need to produce the sha1sum
	 * the question is when is the good time to do so
	 *
	 * Params:
	 *
	 */
	private void calculateSha1Sum(){
		if (this.myMbox != null && this.myMboxSha1sum == null){
			try {
				MessageDigest sha1 = MessageDigest.getInstance("SHA1");
				byte[] byteSha1 = sha1.digest(myMbox.toString().getBytes());
				this.myMboxSha1sum = byteSha1.toString();
			}
			catch(NoSuchAlgorithmException e) {
			}
		}
	}



	/**
	 * @return the myAccount
	 */
	public XapiAccount getAccount() {
		return myAccount;
	}



	public boolean hasAccount(){
		return myAccount != null;
	}



	public XapiIRI getMbox(){
		return this.myMbox;
	}



	public boolean hasMbox(){
		return this.myMbox != null;
	}



	public String getMboxSha1Sum(){
		return this.myMboxSha1sum;
	}



	public boolean hasMboxSha1Sum(){
		return this.myMboxSha1sum != null;
	}



	public URI getOpenId(){
		return this.myOpenID;
	}



	public boolean hasOpenId(){
		return this.myOpenID != null;
	}



	public boolean isValid(){
		return myIdentifierSum == 1;
	}



	/**
	 * 
	 * Definition:
	 *	Return the identifier index which points to which value
	 *	of the identifier is not null
	 *
	 * Params:
	 *
	 *
	 */
	public int getIdentifierIndex(){
		return myIdentifierIndex;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof XapiInverseFunctionalIdentifier){
			XapiInverseFunctionalIdentifier theInvID = (XapiInverseFunctionalIdentifier) obj;
			if(myIdentifierIndex == theInvID.myIdentifierIndex && 
					myIdentifierSum == theInvID.myIdentifierSum){
				if(hasMbox() && !myMbox.equals(theInvID.myMbox)){
					return false;
				}
				if(hasAccount() && !myAccount.equals(theInvID.myAccount)){
					return false;
				}
				if(hasMboxSha1Sum() && !myMboxSha1sum.equals(theInvID.myMboxSha1sum)){
					return false;
				}
				if(hasOpenId() && !myOpenID.equals(theInvID.myOpenID)){
					return false;
				}
				return true;
			}
		}
		return false;
	}




	public String toString(){
		return this.myMbox + " " + this.myMboxSha1sum + this.myAccount + " " + this.myOpenID;
	}


}
