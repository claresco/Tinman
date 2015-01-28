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

import java.util.ArrayList;

import org.apache.abdera.i18n.iri.IRI;
import org.apache.abdera.i18n.iri.IRISyntaxException;
import org.apache.abdera.i18n.text.InvalidCharacterException;

/**
 * XapiIRI.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Jan 15, 2014
 * 
 */

public class XapiIRI {
	
	/*
	 * Local variable(s) description:
	 * 	- myIRI : the IRI value, imported from apache.abdera
	 */
	private IRI myIRI;
	
	
	
	public XapiIRI(String myString){
		myIRI = new IRI(myString);
	}
	
	
	
	protected IRI getIRI(){
		return myIRI;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.myIRI.toString();
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof XapiIRI){
			if(((XapiIRI) obj).getIRI().equals(myIRI)){
				return true;
			}
		}
		return false;
	}
	
		
	
	
	public static void main(String[] args) {
		try{
			XapiIRI myIRI = new XapiIRI("http://www.example.com");
			XapiIRI myIRI2 = new XapiIRI("http://www.example.com342");
			
			
			ArrayList<XapiIRI> arr = new ArrayList<XapiIRI>();
			arr.add(myIRI);
			arr.add(myIRI2);
			
			System.out.println(arr.contains(new XapiIRI("http://www.example.com")));
			System.out.println(arr.contains(new XapiIRI("http://www.example.com342")));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
