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

import java.util.Date;
import java.text.SimpleDateFormat;

import org.joda.time.Duration;
import org.joda.time.Period;


/**
 * XapiResult.java
 *
 * Statement's result
 *
 *
 *
 * @author rheza
 * on Mar 5, 2014
 * 
 */

public class XapiResult {
	
	private XapiScore myScore;
	private Boolean mySuccess;
	private Boolean myCompletion;
	private String myResponse;
	private Duration myDuration;
	private XapiExtension myExtension;
	
	public XapiResult(XapiScore theScore, Boolean theSuccess, Boolean theCompletion, String theResponse, Duration theDuration){
		this.myScore = theScore;
		this.mySuccess = theSuccess;
		this.myCompletion = theCompletion;
		this.myResponse = theResponse;
		this.myDuration = theDuration;
		this.myExtension = null;
	}
	
	
	
	public XapiResult(XapiScore theScore, Boolean theSuccess, Boolean theCompletion, String theResponse, 
			Duration theDuration, XapiExtension theExtension){
		this.myScore = theScore;
		this.mySuccess = theSuccess;
		this.myCompletion = theCompletion;
		this.myResponse = theResponse;
		this.myDuration = theDuration;
		this.myExtension = theExtension;
	}
	
	
	
	public XapiScore getScore() {
		return myScore;
	}
	
	
	
	public boolean getSuccess(){
		return mySuccess;
	}

	
	
	public int getSuccessAsInt(){
		return mySuccess ? 1 : 0;
	}
	
	
	
	public boolean getCompletion(){
		return this.myCompletion;
	}
	
	
	
	public int getCompletionAsInt(){
		return myCompletion ? 1 : 0;
	}
	
	
	
	public String getResponse(){
		return this.myResponse;
	}
	
	
	
	public Duration getDuration(){
		return this.myDuration;
	}
	
	
	
	public String getDurationAsString(){
		return this.myDuration.toString();
	}
	
	
	
	public XapiExtension getExtension(){
		return this.myExtension;
	}
	
	
	
	public boolean hasScore(){
		return myScore != null;
	}
	
	
	
	public boolean hasSuccess(){
		return mySuccess != null;
	}
	
	
	
	public boolean hasCompletion(){
		return myCompletion != null;
	}
	
	
	
	public boolean hasResponse(){
		return myResponse != null;
	}
	
	
	
	public boolean hasDuration(){
		return myDuration != null;
	}
	
	
	
	public boolean hasExtension(){
		return myExtension != null;
	}
	
	
	
	public boolean isEmpty(){
		return !(hasScore() || hasSuccess() || hasCompletion() || 
				hasResponse() || hasDuration() || hasExtension());
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "score: " + myScore + ", success: " + mySuccess + ", completion: " + myCompletion + ", Response: " + myResponse
				+ ", duration: " + myDuration.toString();
	}
	
	public static void main(String[] args) {
		// Trying ISO 8601
		SimpleDateFormat dateFormatter = new SimpleDateFormat("'PT'H'H'm'M'ss'.'SSS");
		
		String a = "PT10H25M32.643S";
		try{
			Date d = dateFormatter.parse(a);
			System.out.println(dateFormatter.format(d));
			System.out.println(d.getTime());
		} catch(Exception e){
			e.printStackTrace();
		}
		
		
		XapiScore theS = new XapiScore(5.0, 5.0, 0.0, 10.0);
		
		XapiResult theR = new XapiResult(theS, false, true, "", null);
		
		System.out.println(theR.getSuccessAsInt());
		
	}
	
}
