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

/**
 * XapiInteraction.java
 *
 * Interaction object, part of the statement's object
 *
 *
 *
 * @author rheza
 * on Mar 5, 2014
 * 
 */

public class XapiInteraction {
	
	// Choices, source, target, steps, or scale
	private String myType;			// Corresponds to the interaction type
	private ArrayList<String> myCorrectResponse;
	private ArrayList<XapiInteractionComponent> myChoices;
	private ArrayList<XapiInteractionComponent> myScale;
	private ArrayList<XapiInteractionComponent> myTarget;
	private ArrayList<XapiInteractionComponent> mySource;
	private ArrayList<XapiInteractionComponent> mySteps;
	
	

	/**
	 * 
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiInteraction(String theType, ArrayList<String> theCorrectResponse, 
			ArrayList<XapiInteractionComponent> theChoices, ArrayList<XapiInteractionComponent> theScale, 
			ArrayList<XapiInteractionComponent> theTarget, ArrayList<XapiInteractionComponent> theSource,
			ArrayList<XapiInteractionComponent> theSteps){
		this.myType = theType;
		this.myCorrectResponse = theCorrectResponse;
		this.myChoices = theChoices;
		this.myScale = theScale;
		this.myTarget = theTarget;
		this.mySource = theSource;
		this.mySteps = theSteps;
	}
	
	
	
	public String getType(){
		return this.myType;
	}
	
	
	
	public ArrayList<String> getCorrectResponse(){
		return this.myCorrectResponse;
	}
	
	
	
	public ArrayList<XapiInteractionComponent> getChoices(){
		return this.myChoices;
	}
	
	
	
	public ArrayList<XapiInteractionComponent> getScale(){
		return this.myScale;
	}
	
	
	
	public ArrayList<XapiInteractionComponent> getSource(){
		return this.mySource;
	}
	
	
	
	public ArrayList<XapiInteractionComponent> getTarget(){
		return this.myTarget;
	}
	
	
	
	public ArrayList<XapiInteractionComponent> getSteps(){
		return this.mySteps;
	}
	
	
	
	public boolean hasType(){
		return this.myType != null;
	}
	
	
	
	public boolean hasCorrectReponse(){
		return this.myCorrectResponse != null && !this.myCorrectResponse.isEmpty();
	}
	
	
	
	public boolean hasChoices(){
		return this.myChoices != null && !myChoices.isEmpty();
	}
	
	
	
	public boolean hasScale(){
		return this.myScale != null && !myScale.isEmpty();
	}
	
	
	
	public boolean hasTarget(){
		return this.myTarget != null && !myTarget.isEmpty();
	}
	
	
	
	public boolean hasSource(){
		return this.mySource != null && !mySource.isEmpty();
	}
	
	
	
	public boolean hasSteps(){
		return this.mySteps != null && !mySteps.isEmpty();
	}
	
	
	
	
	public boolean isTypeValid(){
		return myType.equalsIgnoreCase("choice") || myType.equalsIgnoreCase("sequencing") || 
				myType.equalsIgnoreCase("likert") || myType.equalsIgnoreCase("performance") ||
				myType.equalsIgnoreCase("true-false") || myType.equalsIgnoreCase("fill-in") ||
				myType.equalsIgnoreCase("numeric") || myType.equalsIgnoreCase("matching") ||
				myType.equalsIgnoreCase("other");
	}
	
	
	
	public boolean isEmpty(){
		return !hasType() && !hasCorrectReponse() && !hasChoices() && !hasScale() && !hasTarget()
				&& !hasSource() && !hasSteps();
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String theString = "type: " + myType + "\n";
		theString += "correctResponses: " + myCorrectResponse + "\n";
		theString += "choices: " + myChoices + "\n";
		theString += "scale: " + myScale + "\n";
		theString += "target: " + myTarget + "\n";
		theString += "source: " + mySource + "\n";
		theString += "steps: " + mySteps + "\n";
		return theString;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof XapiInteraction){
			XapiInteraction theInteraction = (XapiInteraction) obj;
			if(hasType() && !myType.equals(theInteraction.myType)){
				return false;
			}
			if(theInteraction.hasType() && !theInteraction.myType.equals(myType)){
				return false;
			}
			if(hasCorrectReponse() && !myCorrectResponse.equals(theInteraction.myCorrectResponse)){
				return false;
			}
			if(theInteraction.hasCorrectReponse() && !theInteraction.myCorrectResponse.equals(myCorrectResponse)){
				return false;
			}
			if(hasChoices() && !myChoices.equals(theInteraction.myChoices)){
				return false;
			}
			if(theInteraction.hasChoices() && !theInteraction.myChoices.equals(myChoices)){
				return false;
			}
			if(hasScale() && !myScale.equals(theInteraction.myScale)){
				return false;
			}
			if(theInteraction.hasScale() && !theInteraction.myScale.equals(myScale)){
				return false;
			}
			if(hasSource() && !mySource.equals(theInteraction.mySource)){
				return false;
			}
			if(theInteraction.hasSource() && !theInteraction.mySource.equals(mySource)){
				return false;
			}
			if(hasSteps() && !mySteps.equals(theInteraction.mySteps)){
				return false;
			}
			if(theInteraction.hasSteps() && !theInteraction.mySteps.equals(mySteps)){
				return false;
			}
			if(hasTarget() && !myTarget.equals(theInteraction.myTarget)){
				return false;
			}
			if(theInteraction.hasTarget() && !theInteraction.myTarget.equals(myTarget)){
				return false;
			}
			return true;
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		XapiLanguageMap lmap = new XapiLanguageMap();
		XapiLanguageMap lmap2 = new XapiLanguageMap();
		
		lmap.registerLanguage("a", "b");
		lmap2.registerLanguage("a", "b");
		
		XapiInteractionComponent ic1 = new XapiInteractionComponent("id", lmap);
		XapiInteractionComponent ic2 = new XapiInteractionComponent("id", lmap2);
		
		ArrayList<XapiInteractionComponent> theICArray1 = new ArrayList<XapiInteractionComponent>();
		ArrayList<XapiInteractionComponent> theICArray2 = new ArrayList<XapiInteractionComponent>();
		
		theICArray1.add(ic1);
		theICArray1.add(ic1);
		theICArray2.add(ic2);
		
		ArrayList<String> as1 = new ArrayList<String>();
		as1.add("1");
		XapiInteraction i1 = new XapiInteraction("a", null, null, null, null, null, theICArray1);
		
		ArrayList<String> as2 = new ArrayList<String>();
		as2.add("1");
		XapiInteraction i2 = new XapiInteraction("a", as2, null, null, null, null, null);
		
		System.out.println(i1);
		System.out.println(i2.equals(i1));
		System.out.println(i1.equals(i2));
	}
	
}
