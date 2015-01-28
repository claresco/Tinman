/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * XapiServletXMLBuilder.java	Jun 12, 2014
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

import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.claresco.tinman.json.XapiJsonControl;
import com.claresco.tinman.lrs.XapiStatement;

/**
 * XapiServletXMLBuilder
 * @author Rheza
 *
 * Description: 
 *
 *
 * Status: 
 *
 *
 */
public class XapiServletXMLBuilder {
	
	private DocumentBuilderFactory myBuilderFactory;
	private DocumentBuilder myBuilder;
	private TransformerFactory myTransformerFactory;
	private Transformer myTransformer;
	private XapiJsonControl myJson;
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public XapiServletXMLBuilder() throws ParserConfigurationException, TransformerConfigurationException{
		myBuilderFactory = DocumentBuilderFactory.newInstance();
		myBuilder = myBuilderFactory.newDocumentBuilder();
		myTransformerFactory = TransformerFactory.newInstance();
		myTransformer = myTransformerFactory.newTransformer();
		myJson = new XapiJsonControl();
	}
	
	
	
	public String buildXMLResponseFromStatements(HashMap<Integer, XapiStatement> theData, String theTitle, String key,
			String value, String description) throws XapiServletException{
		HashMap<String, String> theStringData = new HashMap<String, String>();
		
		for(Integer i : theData.keySet()){
			String theStatementString = myJson.serializeStatement(theData.get(i));
			theStringData.put(Integer.toString(i), theStatementString);
		}
		
		return buildXMLResponse(theStringData, theTitle, key, value, description);
	}
	
	
	
	public String buildXMLResponse(HashMap<String, String> theData, String theTitle, String key,
			String value, String description) throws XapiServletException{
		Document theDoc = myBuilder.newDocument();
		
		Element theRootE = theDoc.createElement(theTitle);
		theDoc.appendChild(theRootE);
		
		for(String k : theData.keySet()){
			Element theDesc = theDoc.createElement(description);
			
			Element theKey = theDoc.createElement(key);
			theKey.appendChild(theDoc.createTextNode(k));
			theDesc.appendChild(theKey);
			
			Element theValue = theDoc.createElement(value);
			theValue.appendChild(theDoc.createTextNode(theData.get(k)));
			theDesc.appendChild(theValue);
			
			theRootE.appendChild(theDesc);
		}
		
		
		myTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		
		StringWriter writer = new StringWriter();
		
		try{
			myTransformer.transform(new DOMSource(theDoc), new StreamResult(writer));
			String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
			
			return output;
		}catch(TransformerException e){
			throw new XapiServletException(500, "Can't produce the XML");
		}
	}
	
	
	
	public static void main(String[] args) {
		try{
			HashMap<String, String> theHM = new HashMap<String, String>();
			theHM.put("A", "B");
			
			XapiServletXMLBuilder theB = new XapiServletXMLBuilder();
			
			System.out.println(theB.buildXMLResponse(theHM, "something", "key", "value", "node"));
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
