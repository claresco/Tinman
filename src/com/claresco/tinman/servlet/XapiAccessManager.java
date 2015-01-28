/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiAccessManager.java	Sep 9, 2014
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

/**
 * XapiAccessManager
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiAccessManager {
	
	private XapiCredentialsList myCredentialsList;
	
	private HashMap<XapiKeySecret, DateTime> myKeySecretTimeMap;
	
	private ArrayList<XapiKeySecret> myLMSKeySecret;
	
	private boolean isDebugMode = false;
	
	private String myCredentialsListFileName;
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiAccessManager(XapiCredentialsList theCredentialsList, 
			HashMap<XapiKeySecret, DateTime> theKeySecretTimeMap) {
		myCredentialsList = theCredentialsList;
		
		myKeySecretTimeMap = theKeySecretTimeMap;
	}
	
	
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiAccessManager(String theFileName) {
		this(new XapiCredentialsList(new HashMap<XapiKeySecret, XapiCredentials>()), theFileName);
	}
	
	
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiAccessManager(XapiCredentialsList theCredentialsList, String theFileName) {
		myCredentialsList = theCredentialsList;
		myKeySecretTimeMap = new HashMap<XapiKeySecret, DateTime>();
		myLMSKeySecret = new ArrayList<XapiKeySecret>();
		myCredentialsListFileName = theFileName;
	}
	
	
	
	protected void addCrendential(XapiKeySecret theKeySecret, XapiCredentials theCredential){
		addLastAccessedTime(theKeySecret, DateTime.now());
		myCredentialsList.put(theKeySecret, theCredential);
	}
	
	
	
	protected void addLastAccessedTime(XapiKeySecret theKeySecret, DateTime theTime){
		myKeySecretTimeMap.put(theKeySecret, theTime);
	}
	
	
	
	protected XapiCredentials getCredential(XapiKeySecret theKeySecret){
		return myCredentialsList.get(theKeySecret);
	}
	
	
	
	protected TreeMap<XapiKeySecret, DateTime> getSortedMap(){
		AccessedTimeComparator theComparator = new AccessedTimeComparator(myKeySecretTimeMap);
		TreeMap<XapiKeySecret, DateTime> theSortedMap = new TreeMap<XapiKeySecret, DateTime>(theComparator);
		
		theSortedMap.putAll(myKeySecretTimeMap);
		
		return theSortedMap;
	}
	
	
	
	protected Set<Map.Entry<XapiKeySecret, DateTime>> getSortedSet(){
		return getSortedMap().entrySet();
	}
	
	
	
	protected boolean containsClientCredentials(XapiKeySecret theCredentials){
		return myCredentialsList.containsKey(theCredentials);
	}
	
	
	
	protected Set<XapiKeySecret> getCredentialsKeySet(){
		return myCredentialsList.keySet();
	}
	
	
	
	protected void removePermission(XapiKeySecret theKeySecret){
		myKeySecretTimeMap.remove(theKeySecret);
	}
	
	
	
	protected void addServerKeySecret(XapiKeySecret theKeySecret){
		myLMSKeySecret.add(theKeySecret);
	}
	
	
	
	protected boolean containServerKeySecret(XapiKeySecret theKeySecret){
		return myLMSKeySecret.contains(theKeySecret);
	}
	
	
	
	protected void setDebugMode(boolean theValue){
		isDebugMode = theValue;
	}
	
	
	
	protected boolean isDebugMode(){
		return isDebugMode;
	}
	
	
	
	protected XapiCredentialsList getCredentialsList(){
		return myCredentialsList;
	}
	
	
	
	protected void saveCredentialsList(String theList){
		DateTime theDate = DateTime.now();
		
		File theFile = new File(myCredentialsListFileName);
		
		try{
			if(!theFile.exists()){
				theFile.createNewFile();
			}
			
			PrintWriter theWriter = new PrintWriter(new BufferedWriter(new FileWriter
					(myCredentialsListFileName, false)));

			theWriter.print(theList);
			
			theWriter.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		XapiAccessManager theM = new XapiAccessManager("abc");
		
		XapiKeySecret theKS1 = new XapiKeySecret("a", "b");
		XapiKeySecret theKS2 = new XapiKeySecret("c", "d");
		
		theM.addLastAccessedTime(theKS2, new DateTime());
		theM.addLastAccessedTime(theKS1, new DateTime());
		
		for(Map.Entry<XapiKeySecret, DateTime> theEntry : theM.getSortedSet()){
			System.out.println(theEntry.getKey() + " : " + theEntry.getValue());
		}
	}	
}
