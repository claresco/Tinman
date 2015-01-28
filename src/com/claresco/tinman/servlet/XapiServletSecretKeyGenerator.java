/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiServletSecretKeyGenerator.java	May 13, 2014
 *
 * Copyright 2014 Claresco Corporation, Berkeley, CA 94704. All Rights Reserved.
 *
 * This software is the proprietary information of Claresco Corporation.
 * Use is subject to license terms.
 *
 * Author : Rheza
 *
 */
package com.claresco.tinman.servlet;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

/**
 * XapiServletSecretKeyGenerator
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiServletSecretKeyGenerator {
	
	private Random myNumberGenerator = new Random();
	private MessageDigest myMD = MessageDigest.getInstance("MD5");
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiServletSecretKeyGenerator() throws NoSuchAlgorithmException{
		myNumberGenerator = new Random();
		myMD = MessageDigest.getInstance("MD5");
	}
	
	
	
	private String generateKey(String theRequest){
		Date theNow = new Date();
		
		String keyString = theRequest + theNow.toString() + myNumberGenerator.nextInt();
		
		return getMD5HashString(keyString);
	}
	
	
	
	private String generateSecret(String theKey){
		String theSecretString = myNumberGenerator.nextInt() + theKey;
		
		return getMD5HashString(theSecretString);
	}
	
	
	
	private String getMD5HashString(String theString){
		myMD.update(theString.getBytes(), 0, theString.length());
		
		byte[] theBytes = myMD.digest();
		
		StringBuffer hexString = new StringBuffer();
    	for (int i=0;i < theBytes.length;i++) {
    		String hex=Integer.toHexString(0xff & theBytes[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	
    	return hexString.toString();
	}
	
	
	
	protected XapiKeySecret getLoginInformation(String theRequest) throws IOException{
	        String theKey = generateKey(theRequest);
	        String theSecret = generateSecret(theKey);
	        return new XapiKeySecret(theKey, theSecret);
	}
}
