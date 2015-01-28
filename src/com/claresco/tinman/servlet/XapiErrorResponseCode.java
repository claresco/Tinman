/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiErrorResponseCode.java	Mar 31, 2014
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

/**
 * @author rheza
 * 
 * Description:
 * 
 * Status
 * 
 */
public enum XapiErrorResponseCode {
	LOGIN("Login"),
	SYSTEMBUSY("Sytem Busy"),
	CONNECTIONINUSE("Connection in Use"),
	INSUFFICIENTPERMISSION("Insufficient Permision"),
	DEFAULTERROR("Default Error"),
	INACTIVEUSER("Inactive User"),
	UPDATEPASSWORD("UpdatePassword"),
	INFOREQUIRED("Info Required"),
	ALREADYREGISTERED("Already Registered");
	
	private final String code;
	
	private XapiErrorResponseCode(String theCode){
		code = theCode;
	}
	
	protected String getCode(){
		return code;
	}
}
