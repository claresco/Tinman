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

package com.claresco.tinman.sql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;

import com.claresco.tinman.json.JsonUtility;
import com.claresco.tinman.lrs.XapiAccount;
import com.claresco.tinman.lrs.XapiActivity;
import com.claresco.tinman.lrs.XapiActivityDefinition;
import com.claresco.tinman.lrs.XapiActor;
import com.claresco.tinman.lrs.XapiAgent;
import com.claresco.tinman.lrs.XapiClarescoException;
import com.claresco.tinman.lrs.XapiContext;
import com.claresco.tinman.lrs.XapiIRI;
import com.claresco.tinman.lrs.XapiInteraction;
import com.claresco.tinman.lrs.XapiInteractionComponent;
import com.claresco.tinman.lrs.XapiInverseFunctionalIdentifier;
import com.claresco.tinman.lrs.XapiLanguageMap;
import com.claresco.tinman.lrs.XapiObject;
import com.claresco.tinman.lrs.XapiState;
import com.claresco.tinman.lrs.XapiStatement;
import com.claresco.tinman.lrs.XapiStatementRef;
import com.claresco.tinman.lrs.XapiVerb;
import com.claresco.tinman.servlet.XapiNotAuthorizedException;
import com.google.gson.Gson;

/**
 * XapiSQLControl.java
 *
 * Keep track of all the writers and readers of the database
 *
 *
 *
 * @author rheza
 * on Feb 27, 2014
 * 
 */

public class XapiSQLControl {
	
	private Connection myConn;
	
	// All the writers needed
	private XapiAccountSQLWriter myAccountWriter;
	private XapiActivitySQLWriter myActivityWriter;
	private XapiActorSQLWriter myActorWriter;
	private XapiAgentSQLWriter myAgentWriter;
	private XapiContextSQLWriter myContextWriter;
	private XapiExtensionSQLWriter myExtensionWriter;
	private XapiGroupSQLWriter myGroupWriter;
	private XapiLanguageMapSQLWriter myLanguageMapWriter;
	private XapiObjectSQLWriter myObjectWriter;
	private XapiResultSQLWriter myResultWriter;
	private XapiObjectSQLWriter mySubstatementObjectWriter;
	private XapiStatementSQLWriter myStatementWriter;
	private XapiSubStatementSQLWriter mySubStamentWriter;
	private XapiVerbSQLWriter myVerbWriter;
	private XapiContextActivitiesSQLWriter myContextActivitiesWriter;
	
	private XapiDocumentSQLWriter myDocumentWriter;
	private XapiStateSQLWriter myStateWriter;
	private XapiActivityProfileSQLWriter myActivityProfileWriter;
	private XapiAgentProfileSQLWriter myAgentProfileWriter;
	
	// All the readers needed
	private XapiAccountSQLReader myAccountReader;
	private XapiActivitySQLReader myActivityReader;
	private XapiActorSQLReader myActorReader;
	private XapiContextActivitiesSQLReader myContextActivitiesReader;
	private XapiContextSQLReader myContextReader;
	private XapiExtensionSQLReader myExtensionReader;
	private XapiLanguageMapSQLReader myLanguageMapReader;
	private XapiObjectSQLReader myObjectReader;
	private XapiResultSQLReader myResultReader;
	private XapiObjectSQLReader mySubStatementObjectReader;
	private XapiStatementSQLReader myStatementReader;
	private XapiSubStatementSQLReader mySubStatementReader;
	private XapiVerbSQLReader myVerbReader;
	
	private XapiDocumentSQLReader myDocumentReader;
	private XapiStateSQLReader myStateReader;
	private XapiActivityProfileSQLReader myActivityProfileReader;
	private XapiAgentProfileSQLReader myAgentProfileReader;
	
	/**
	 * Description:
	 *
	 * Params:
	 *
	 */
	public XapiSQLControl(Connection conn) throws SQLException{
		this.myConn = conn;
		
		// Initialize readers
		myAccountReader = new XapiAccountSQLReader(myConn);
		myExtensionReader = new XapiExtensionSQLReader(myConn);
		myLanguageMapReader = new XapiLanguageMapSQLReader(myConn);
		myResultReader = new XapiResultSQLReader(myConn);
		myActivityReader = new XapiActivitySQLReader(myConn, myLanguageMapReader, myExtensionReader);
		myContextActivitiesReader = new XapiContextActivitiesSQLReader(myConn, myActivityReader);
		myContextReader = new XapiContextSQLReader(myConn, myContextActivitiesReader);
		myActorReader = new XapiActorSQLReader(myConn, myAccountReader);
		myVerbReader = new XapiVerbSQLReader(myConn, myLanguageMapReader);	
		mySubStatementObjectReader = new XapiObjectSQLReader(myConn, myActivityReader, myActorReader, null);
		mySubStatementReader = new XapiSubStatementSQLReader(myConn, myActorReader, myVerbReader,
				mySubStatementObjectReader);		
		myObjectReader = new XapiObjectSQLReader(myConn, myActivityReader, myActorReader, mySubStatementReader);
		myStatementReader = new XapiStatementSQLReader(myConn, myActorReader, myVerbReader, myObjectReader, 
				myContextReader, myResultReader);
		
		myDocumentReader = new XapiDocumentSQLReader(myConn);
		myStateReader = new XapiStateSQLReader(myConn, myDocumentReader, myActorReader, myActivityReader);
		myActivityProfileReader = new XapiActivityProfileSQLReader(myConn, myDocumentReader, myActivityReader);
		myAgentProfileReader = new XapiAgentProfileSQLReader(myConn, myActorReader, myDocumentReader);
		
		// Initialize writers
		myResultWriter = new XapiResultSQLWriter(myConn);
		myAccountWriter = new XapiAccountSQLWriter(myConn, myAccountReader);
		myLanguageMapWriter = new XapiLanguageMapSQLWriter(myConn, myLanguageMapReader);
		myExtensionWriter = new XapiExtensionSQLWriter(myConn, myExtensionReader);
		myActivityWriter = new XapiActivitySQLWriter(myConn, myLanguageMapWriter, myExtensionWriter,
				myActivityReader);
		myAgentWriter = new XapiAgentSQLWriter(myConn, myAccountWriter);
		myGroupWriter = new XapiGroupSQLWriter(myConn, myAccountWriter, myAgentWriter);
		myActorWriter = new XapiActorSQLWriter(myConn, myAgentWriter, myGroupWriter, myActorReader);
		myContextActivitiesWriter = new XapiContextActivitiesSQLWriter(myConn, myActivityWriter);
		myContextWriter = new XapiContextSQLWriter(myConn, myActorWriter, myStatementReader,
				myContextActivitiesWriter);
		myVerbWriter = new XapiVerbSQLWriter(myConn, myLanguageMapWriter, myVerbReader);
		mySubstatementObjectWriter = new XapiObjectSQLWriter(myConn, myActorWriter, myActivityReader,
				null, myStatementReader, myActivityWriter);
		mySubStamentWriter = new XapiSubStatementSQLWriter(myConn, myActorWriter, myVerbWriter,
				mySubstatementObjectWriter);
		myObjectWriter = new XapiObjectSQLWriter(myConn, myActorWriter, myActivityReader,
				mySubStamentWriter, myStatementReader, myActivityWriter);
		myStatementWriter = new XapiStatementSQLWriter(myConn, myActorWriter, myVerbWriter, myObjectWriter, 
				myStatementReader, myContextWriter, myResultWriter);
		
		myDocumentWriter = new XapiDocumentSQLWriter(myConn);
		myStateWriter = new XapiStateSQLWriter(myConn, myActorWriter, myActivityReader, myDocumentWriter,
				myStateReader, myActivityWriter);
		myActivityProfileWriter = new XapiActivityProfileSQLWriter(myConn, myActivityWriter,
				myDocumentWriter, myActivityProfileReader);
		myAgentProfileWriter = new XapiAgentProfileSQLWriter(myConn, myActorWriter, myDocumentWriter,
				myAgentProfileReader);
	}
	
	
	
	public int insertNewStatement(XapiStatement theStatement, boolean newActivityAllowed, boolean generateRandomID)
			throws XapiSQLOperationProblemException, XapiDataIntegrityException{
		if (theStatement == null) {
			return -1;
		}
		try{
			int id = myStatementWriter.insertNewStatement(theStatement, newActivityAllowed, generateRandomID);
			return id;
		}catch(SQLException e){
			//e.printStackTrace();
			throw new XapiSQLOperationProblemException("Having trouble writing statements : " + e.getMessage());
		}
	}
	
	
	
	public XapiStatement retrieveStatementByID(int theID) throws SQLException, XapiDataIntegrityException{
		HashMap<Integer, XapiStatement> myStatementMap = myStatementReader.retrieveByID(theID);
		
		if (myStatementMap.size() > 1) {
			throw new XapiDuplicateStatementIDException("Oops, there are multiple statements " +
					"using this ID, damn it!");
		}
		
		// Maybe not appropriate for production
		for(Integer i : myStatementMap.keySet()){
			if(i.intValue() != theID){
				throw new XapiDataIntegrityException("Something super wrong with your reader/writer");
			}
			return myStatementMap.get(i);
		}
		
		return null;
	}
	
	
	
	public HashMap<Integer, XapiStatement> retrieveStatements(HashMap<String, String> paramMap) throws SQLException,
			XapiDataIntegrityException, XapiSQLOperationProblemException{
		return myStatementReader.handleConjuctionQuery(paramMap);
	}
	
	
	
	public int retrieveActorID(XapiActor theActor) throws SQLException, XapiDataIntegrityException{
		return myActorReader.retrieveActorID(theActor);
	}
	
	
	
	public HashMap<Integer, XapiStatement> getStatementsByActor(XapiActor theActor) throws SQLException,
			XapiDataIntegrityException
	{
		int theActorID = myActorReader.retrieveByInverseFuncID(theActor.getInverseFuncId());
		
		return myStatementReader.retrieveStatementByActor(theActorID);
	}
	
	
	
	public HashMap<Integer, XapiStatement> getStatementsByVerb(String theIRI) throws SQLException,
			XapiDataIntegrityException
	{
		return myStatementReader.retrieveStatementByVerb(theIRI);
	}
	
	
	
	public int insertState(XapiState theState) throws XapiDataIntegrityException, 
			XapiSQLOperationProblemException{
		if(theState == null){
			return -1;
		}
		try{
			return myStateWriter.insertState(theState);
		}catch(SQLException e){
			//e.printStackTrace();
			throw new XapiSQLOperationProblemException("Having trouble writing the state : " + e.getMessage());
		}
		
	}
	
	
	
	public String retrieveState(String theActivityIRI, XapiActor theActor, String theStateID, String theRegistration) throws
			SQLException, XapiSQLOperationProblemException{
		return myStateReader.retrieveState(theActivityIRI, theActor, theStateID, theRegistration);
	}
	
	
	
	public String retrieveState(String theActivityIRI, XapiActor theActor, String theStateID) throws
			SQLException, XapiSQLOperationProblemException{
		return myStateReader.retrieveState(theActivityIRI, theActor, theStateID);
	}
	
	
	
	public HashMap<String, String> retrieveMultipleState(String theActivityID, XapiActor theActor, String theRegistration,
			DateTime theTimestamp) throws SQLException, XapiSQLOperationProblemException{
		return myStateReader.retrieveMultipleState(theActivityID, theActor, theRegistration, theTimestamp);
	}
	
	
	
	public HashMap<String, String> retrieveMultipleState(String theActivityID, XapiActor theActor, String theRegistration)
			throws SQLException, XapiSQLOperationProblemException{
		return myStateReader.retrieveMultipleState(theActivityID, theActor, theRegistration);
	}
	
	
	
	public HashMap<String, String> retrieveMultipleState(String theActivityID, XapiActor theActor,
			DateTime theTimestamp) throws SQLException, XapiSQLOperationProblemException{
		return myStateReader.retrieveMultipleState(theActivityID, theActor, theTimestamp);
	}
	
	
	
	public HashMap<String, String> retrieveMultipleState(String theActivityID, XapiActor theActor)
			throws SQLException, XapiSQLOperationProblemException{
		return myStateReader.retrieveMultipleState(theActivityID, theActor);
	}
	
	
	
	public XapiActivity retrieveActivity(String theActivityID) throws SQLException {
		ArrayList<XapiActivity> theActivity = myActivityReader.retrieveActivityByValue(theActivityID);
		
		if(theActivity == null){
			return null;
		}
		
		if(theActivity.size() > 1){
			return null;
		}
		return theActivity.get(0);
	}
	
	
	
	public int insertActivityProfile(String theActivityID, String theProfileKey, String theDocument) throws 
			SQLException, XapiDataIntegrityException, XapiSQLOperationProblemException{
		return myActivityProfileWriter.insertActivityProfile(theActivityID,
				theProfileKey, theDocument);
	}
	
	
	
	public String retrieveActivityProfile(String theActivityID, String theProfileKey) throws 
			SQLException, XapiDataIntegrityException, XapiDataNotFoundException, XapiSQLOperationProblemException{
		return myActivityProfileReader.retrieveActivityProfile(theActivityID, theProfileKey);
	}
	
	
	
	public HashMap<String, String> retrieveMultipleActivityProfile(String theActivityID, DateTime
			theTimestamp) throws SQLException, XapiSQLOperationProblemException{
		return myActivityProfileReader.retrieveMultipleActivityProfile(theActivityID, theTimestamp);
	}
	
	
	
	public HashMap<String, String> retrieveMultipleActivityProfile(String theActivityID)
			throws SQLException, XapiSQLOperationProblemException{
		return myActivityProfileReader.retrieveMultipleActivityProfile(theActivityID);
	}	
	
	
	
	public int insertNewAgentProfile(XapiActor theActor, String theProfileKey, String theDocument)
			throws SQLException, XapiClarescoException{
		return myAgentProfileWriter.insertAgentProfile(theActor, theProfileKey, theDocument);
	}
	
	
	
	public String retrieveSingleAgentProfile(XapiActor theActor, String theProfileKey) throws
			SQLException, XapiClarescoException{
		return myAgentProfileReader.retrieveSingleAgentProfile(theActor, theProfileKey);
	}
	
	
	
	public HashMap<String, String> retrieveMultipleAgentProfile(XapiActor theActor) throws
			SQLException, XapiClarescoException{
		return myAgentProfileReader.retrieveMultipleAgentProfile(theActor);
	}
	
	
	
	public HashMap<String, String> retrieveMultipleAgentProfile(XapiActor theActor, DateTime 
			theSince) throws SQLException, XapiClarescoException{
		return myAgentProfileReader.retrieveMultipleAgentProfile(theActor, theSince);
	}
	
	
	
	/**
	 * Definition:
	 *
	 *
	 * Params:
	 *
	 * 
	 */
	public void close() throws SQLException{
		if(myConn != null){
			myConn.close();
		}
		
		myAccountReader.close();
		myExtensionReader.close();
		myLanguageMapReader.close();
		myActivityReader.close();
		myActorReader.close();
		myVerbReader.close();
		mySubStatementObjectReader.close();
		mySubStatementReader.close();
		myObjectReader.close();
		myStatementReader.close();
		myDocumentReader.close();
		myStateReader.close();
		myActivityProfileReader.close();
		myAgentProfileReader.close();
		myResultReader.close();
		myContextReader.close();
		myContextActivitiesReader.close();
		
		myResultWriter.close();
		myAccountWriter.close();
		myExtensionWriter.close();
		myLanguageMapWriter.close();
		myAgentWriter.close();
		myGroupWriter.close();
		myActorWriter.close();
		myContextActivitiesWriter.close();
		myContextWriter.close();
		myVerbWriter.close();
		mySubstatementObjectWriter.close();
		mySubStamentWriter.close();
		myObjectWriter.close();
		myStatementWriter.close();
		myDocumentWriter.close();
		myStateWriter.close();
		myActivityProfileWriter.close();
		myAgentProfileWriter.close();
	}
	
	
	
	
	public static void main(String[] args) {
		try{
			Connection conn = SQLUtility.establishDefaultConnection();
			
			/**
			// Test actor writer
			XapiAccount theAccount = new XapiAccount("http://example.com/homePage", "GroupAccount");
			XapiInverseFunctionalIdentifier theID = new XapiInverseFunctionalIdentifier(null, null, null, theAccount);
			XapiActor theActor = new XapiAgent("Example Group", theID);
			//System.out.println(theControl.insertNewActor(theActor));
			
			XapiVerb theVerb = new XapiVerb("http://www.adlnet.gov/XAPIprofile/ran(travelled_a_distance)");
			//System.out.println(theControl.insertNewVerb(theVerb));
			
			XapiObject theObject = new XapiAgent("ExampleGroup", theID);
			XapiActivity theActivity = new XapiActivity("http://example.com/website");
			XapiStatementRef theStatementRef = new XapiStatementRef("a69bb626-1195-4ad6-b347-232068cdad55");
			//System.out.println(theControl.insertNewObject(theStatementRef));
			
			theAccount = new XapiAccount("http://example.moodle.com", "123456");
			theID = new XapiInverseFunctionalIdentifier(null, null, null, theAccount);
			theActor = new XapiAgent("Andrew Downes", theID);
			
			SQLUtility.printHashMap(theControl.getStatementsByActor(theActor));
			**/
			
			// Testing XapiContextActivitySQLWriter
			Gson gson = JsonUtility.createGson();
			String path = "/Users/rheza/Documents/workspace/ClarescoExperienceAPI/src/com/claresco/tinman/json/Activity.json";
			 
			BufferedReader bf = new BufferedReader(new FileReader(path));
			XapiActivity s = gson.fromJson(bf, XapiActivity.class);
			
			System.out.println(s);
				 
			String json = gson.toJson(s);
			System.out.println(json);
			
			XapiLanguageMap theName = new XapiLanguageMap();
			XapiLanguageMap theDescription = new XapiLanguageMap();
			theName.registerLanguage("en-US", "slash");
			theDescription.registerLanguage("en-US", "slash slash");
			//XapiActivityDefinition theDef = new XapiActivityDefinition(theName, theDescription, "http://claresco.com/myActivityType",
			//		null, null, null);
			
			ArrayList<XapiInteractionComponent> theChoices = new ArrayList<XapiInteractionComponent>();
			XapiLanguageMap thelmap = new XapiLanguageMap();
			thelmap.registerLanguage("en-US", "value");
			XapiInteractionComponent theIcomp = new XapiInteractionComponent("id", thelmap);
			theChoices.add(theIcomp);
			XapiInteraction theInter = new XapiInteraction("choice", null, theChoices, null, null, null, theChoices);
			
			XapiActivityDefinition theDef = new XapiActivityDefinition(theName, theDescription, "http://adlnet.gov/expapi/activities/cmi.interaction",
					null, theInter, null);
			
			XapiActivity theActv = new XapiActivity("http://activity.com/slash/slash/slash", theDef);
			
			//theControl.myActivityWriter.upda(11656, theActv);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
