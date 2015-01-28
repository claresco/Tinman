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

import org.apache.abdera.i18n.iri.*;

/**
 * XapiIRL.java
 *
 *
 *
 *
 *
 * @author rheza
 * on Jan 15, 2014
 * 
 */

public class XapiIRL {
	
	// why IRI not XapiIRI? who knows
	private IRI myIRI;
	
	
	
	public XapiIRL(String string){
		myIRI = new IRI(string);
	}
	
	
	
	protected IRI getIRL(){
		return myIRI;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return myIRI.toString();
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof XapiIRL){
			if(((XapiIRL) obj).getIRL().equals(myIRI)){
				return true;
			}
		}
		return false;
	}
}
