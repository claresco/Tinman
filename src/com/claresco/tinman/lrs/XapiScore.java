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
 * XapiScore.java
 *
 * Score, part of statement's result
 *
 *
 *
 * @author rheza
 * on Mar 5, 2014
 * 
 */

public class XapiScore {
	
	private Double myScaled;
	private Double myRaw;
	private Double myMin;
	private Double myMax;
	
	/**
	 * Description:
	 *
	 * Params:
	 *
	 */
	public XapiScore(Double theScaled, Double theRaw, Double theMin, Double theMax) {
		this.myScaled = theScaled;
		this.myRaw = theRaw;
		this.myMin = theMin;
		this.myMax = theMax;
	}
	
	
	
	/**
	 * Description:
	 *
	 * Params:
	 *
	 */
	public XapiScore(double theScaled, double theRaw, double theMin, double theMax) {
		this.myScaled = theScaled;
		this.myRaw = theRaw;
		this.myMin = theMin;
		this.myMax = theMax;
	}

	
	
	public Double getScaledScore(){
		return this.myScaled;
	}
	
	
	
	public Double getRawScore(){
		return this.myRaw;
	}
	
	
	
	public Double getMinScore(){
		return this.myMin;
	}
	
	
	
	public Double getMaxScore(){
		return this.myMax;
	}
	
	
	
	public boolean hasScaledScore(){
		return this.myScaled != null;
	}
	
	
	
	public boolean hasRawScore(){
		return this.myRaw != null;
	}
	
	
	
	public boolean hasMinScore(){
		return this.myMin != null;
	}
	
	
	
	public boolean hasMaxScore(){
		return this.myMax != null;
	}
	
	
	
	private boolean isScaledValid(){
		if(hasScaledScore()){
			return myScaled >= -1 && myScaled <= 1;
		}
		return true;		
	}
	
	
	
	private boolean isRawValid(){
		if(hasRawScore()){
			if(hasMaxScore()){
				return myMax >= myRaw;
			}
			if(hasMinScore()){
				return myMin <= myRaw;
			}
		}
		return true;
	}
	
	
	
	private boolean isMaxValid(){
		if(hasMaxScore() && hasMinScore()){
			return myMax > myMin;
		}
		return true;
	}
	
	
	
	private boolean isMinValid(){
		if(hasMinScore() && hasMaxScore()){
			return myMin < myMax;
		}
		return true;
	}
	
	
	
	public boolean isValid(){
		return isScaledValid() && isRawValid() && isMinValid() && isMaxValid();
	}
	
	
	
	public boolean isEmpty(){
		return !hasScaledScore() && !hasRawScore() && !hasMaxScore() && !hasMinScore();
	}
	

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "theScaled: " + myScaled + ", theRaw: " + myRaw + ", theMin: " + myMin + ", theMax:" + myMax;
	}
	
}
