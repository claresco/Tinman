package com.claresco.tinman.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.abdera.i18n.iri.IRISyntaxException;
import org.apache.abdera.i18n.text.InvalidCharacterException;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.claresco.tinman.json.XapiJsonControl;
import com.claresco.tinman.json.XapiParseException;
import com.claresco.tinman.lrs.XapiActivity;
import com.claresco.tinman.lrs.XapiActor;
import com.claresco.tinman.lrs.XapiClarescoException;
import com.claresco.tinman.lrs.XapiContext;
import com.claresco.tinman.lrs.XapiContextActivities;
import com.claresco.tinman.lrs.XapiIRI;
import com.claresco.tinman.lrs.XapiState;
import com.claresco.tinman.lrs.XapiStatement;
import com.claresco.tinman.lrs.XapiStatementBatch;
import com.claresco.tinman.lrs.XapiStatementResult;
import com.claresco.tinman.sql.XapiDataIntegrityException;
import com.claresco.tinman.sql.XapiDataNotFoundException;
import com.claresco.tinman.sql.XapiSQLControl;
import com.google.gson.JsonSyntaxException;

/**
 * @author Rheza
 * 
 * Description:
 * 	Just a trial
 * 
 * Status:
 * 	RED 
 *
 */
public class XapiServlet extends HttpServlet{

	static final long serialVersionUID = 1L;
	private static final String POST = "POST";
	private static final String PUT = "PUT";
	private static final String GET = "GET";
	private static final String OPTIONS = "OPTIONS";

	private static final String CONTENTTYPE = "Content-Type";
	private static final String PLAINTEXT = "text/plain";
	private static final String HTMLTEXT = "text/html";
	private static final String JSON = "application/json";
	
	private static final String UTF8 = "UTF-8";

	private ServletContext myServletContext;

	// The serializer and deserializer
	private XapiJsonControl myJson;
	// My connection pool
	private ConnectionPooling myConnectionPooling;
	// Generate login and password combination for user
	private XapiServletSecretKeyGenerator mySecretKeyGenerator;
	// Logger
	private XapiServletSimpleErrorLogger myLogger;
	// Another logger
	private XapiLogger myOtherLogger;

	// List of permissions
	private XapiAccessManager myAccessManager;

	// List of accepted params for different kind of requests
	private ArrayList<String> myCredentialsRequestParams;
	private ArrayList<String> myStatementGetParams;
	private ArrayList<String> myStateSingleParams;
	private ArrayList<String> myStateSingleGetParams;
	private ArrayList<String> myStateMultipleGetParams;
	private ArrayList<String> myStateMultipleDeleteParams;
	private ArrayList<String> myActivityProfileSingleParams;
	private ArrayList<String> myActivityProfileSingleGetParams;
	private ArrayList<String> myActivityProfileMultipleGetPrams;
	private ArrayList<String> myAgentProfileSingleParams;
	private ArrayList<String> myAgentProfileMultipleParams;

	private ArrayList<String> myAllowedDomainList;
	private ArrayList<String> myAllowedHeaderList;
	
	// Debug mode value is defined in the web.xml
	private boolean isDebugMode = false;



	@Override
	public void init() throws ServletException {
		super.init();
		myJson = new XapiJsonControl();
		
		// Get Servlet Context
		myServletContext = getServletContext();

		// Retrieving information for database connection and initializing connection
		String myDriverName = myServletContext.getInitParameter("DriverName");
		String myURL = myServletContext.getInitParameter("DatabaseServer");
		String myUser = myServletContext.getInitParameter("DatabaseLogin");
		String myPassword = myServletContext.getInitParameter("DatabasePassword");
		String myAcceptedDomain = myServletContext.getInitParameter("AcceptedDomains");
		
		String[] myAcceptedDomainList = myAcceptedDomain.split(",");
		populateDomainList(myAcceptedDomainList);
		
		String theCredentialsListFileName = myServletContext.getInitParameter("CredentialsListFileName");
		String theCredentialsListPath = myServletContext.getInitParameter("CredentialsListPath");
		
		XapiCredentialsList theCredentialsList = null;
		try{
			BufferedReader theReader = new BufferedReader(new FileReader(new File(theCredentialsListPath + 
					theCredentialsListFileName)));
			theCredentialsList = myJson.deserialiCredentialsList(theReader);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		
		if(theCredentialsList == null){
			myAccessManager = new XapiAccessManager(theCredentialsListPath + theCredentialsListFileName);
		}else{
			myAccessManager = new XapiAccessManager(theCredentialsList, theCredentialsListPath +
					theCredentialsListFileName);
		}
		
		myServletContext.setAttribute("Access Manager", myAccessManager);
		

		// check if it is the debug mode from web.xml;
		if(myServletContext.getInitParameter("debug").equals("yes")){
			isDebugMode = true;
			myAccessManager.setDebugMode(isDebugMode);
		}

		
		myOtherLogger = new XapiLogger();
		
		// Getting the LMS key secret from web.xml
		getServerKeySecret();

		// This will populate the list of accepted params for every request
		populateCredentialRequestParams();
		populateStatementGetParams();
		populateStateSingleParams();
		populateStateSingleGetParams();
		populateStateMultipleDeleteParams();
		populateStateMultipleGetParams();
		populateActivityProfileSingleParams();
		populateActivityProfileSingleGetParams();
		populateActivityProfileMultipleGetParams();
		populateAgentProfileSingleParams();
		populateAgentProfileMultipleParams();

		populateHeaderList();

		// This is a timer which will maintain the credentials/permissions list from getting
		// too big
		maintainCredentialsList();
		
		try{
			myConnectionPooling = new ConnectionPooling(myURL, myUser, myPassword, myDriverName);

			mySecretKeyGenerator = new XapiServletSecretKeyGenerator();

			String theLogPath = myServletContext.getInitParameter("LogPath");
			String theFileName = myServletContext.getInitParameter("LogFileName");
			myLogger = new XapiServletSimpleErrorLogger(theLogPath, theFileName);

		}catch(SQLException e){
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		myOtherLogger.keepTrack("Servlet initialized");
	}



	@Override
	public void destroy() {
		String theList = myJson.serializeCredentialsList(myAccessManager.getCredentialsList());
		
		myAccessManager.saveCredentialsList(theList);
		
		myOtherLogger.keepTrack("Servlet destroyed");
		super.destroy();
	}



	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException{
		// Can't have super here
		String methodName = POST;
		boolean alternateSyntax = false;

		// For IE8 and IE9
		if(req.getParameter("method") != null){
			methodName = req.getParameter("method");
			alternateSyntax = true;
		}

		handleRequest(req, resp, methodName, alternateSyntax);
	}



	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException{
		// Can't have super here
		String methodName = GET;
		
		/**
		boolean alternateSyntax = false;
		
		// For IE8 and IE9
		if(req.getParameter("method") != null){
			methodName = req.getParameter("method");
			alternateSyntax = true;
		}
		**/
		
		handleRequest(req, resp, methodName, false);
	}



	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException{
		String methodName = PUT;
		handleRequest(req, resp, methodName, false);
	}



	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doOptions(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleCORS(req, resp);
	}



	/**
	private void debugFreakingIE8(HttpServletRequest req){
		// For IE8 debugging purposes
		StringBuffer theStringBuffer = new StringBuffer();
		theStringBuffer.append("Request URI : " + req.getRequestURI() +"\n");
		theStringBuffer.append("Query String : " + req.getQueryString() +"\n");

		Enumeration<String> e = req.getParameterNames();


		theStringBuffer.append("the param names :\n");
		while(e.hasMoreElements()){
			String paramName = e.nextElement();
			theStringBuffer.append(paramName + " : " + req.getParameter(paramName) + "\n");
		}

		theStringBuffer.append("\nthe header names :\n");
		e = req.getHeaderNames();
		while(e.hasMoreElements()){
			String headerName = e.nextElement();
			theStringBuffer.append(headerName + " : " + req.getHeader(headerName) + "\n");
		}

		try{
			theStringBuffer.append("\nThe reader:\n");
			theStringBuffer.append(XapiServletUtility.getStringFromReader(req.getReader()) + "\n\n\n");
		}catch(IOException e1){
			theStringBuffer.append("Having trouble reading from the reader \n");
		}catch(XapiServletOperationProblemException e2) {
			theStringBuffer.append("Having trouble reading from the reader \n");
		}



		myOtherLogger.keepTrack(theStringBuffer.toString());
	}
	**/
	
	
	
	/**
	 * 
	 * Definition:
	 *	Handling request, decide which handler functions are called
	 *
	 * Params:
	 *
	 *
	 */
	private void handleRequest(HttpServletRequest req, HttpServletResponse resp, String methodName,
			boolean alternateSyntax){
		Connection conn = null;
		XapiSQLControl mySQLControl = null;
		long theLogID = -1;
		
		/**
		// Logging purposes
		StringBuffer theStringBuffer = new StringBuffer();
		theStringBuffer.append("request.getRequstURI() gives :");
		theStringBuffer.append(req.getRequestURI() + "\n");
		theStringBuffer.append("request.getQueryString() gives :");
		theStringBuffer.append(req.getQueryString() + "\n");
		theStringBuffer.append("method is :");
		theStringBuffer.append(req.getMethod() + "\n");
		theStringBuffer.append("Path info : " + req.getPathInfo() + "\n");
		myOtherLogger.keepTrack(theStringBuffer.toString());
		**/
		
		try {
			// Getting connection and calling SQL control
			conn = myConnectionPooling.getConnection();
			//conn.setAutoCommit(false);
			//myOtherLogger.keepTrack("connection gotten succesfully");

			// Initialize SQL Control
			mySQLControl = new XapiSQLControl(conn);

			// Checking validity of URL
			checkURL(req, resp);

			// Getting bit and pieces from the URL
			String theAPI = getAPI(req);
			String theAction = getAction(req);

			// Add some headers to allow cross site requests
			respondToCORS(req, resp);

			// create header and parameter map
			HashMap<String, String> theHeaderAndParameterMap = getHeaderAndParameterMap(req, alternateSyntax);

			// Set response encoding universally to UTF-8
			resp.setCharacterEncoding(UTF8);
			
			// Calling appropriate handler function
			// Basic API
			if(theAPI.equalsIgnoreCase("basic")){
				handleBasicAPI(req, resp, methodName, mySQLControl, theHeaderAndParameterMap);

			// STATE and ACTIVITIES API
			}else if(theAPI.equalsIgnoreCase("activities")){
				if(XapiServletUtility.checkAction(theAction, "state")){
					handleStateAPI(req, resp, methodName, mySQLControl, theHeaderAndParameterMap);
				}else if(theAction == null){
					// Asking to read the activity
					handleActivityProfileAPI(req, resp, methodName, mySQLControl, theHeaderAndParameterMap);
				}else if(theAction.equalsIgnoreCase("profile")){
					handleActivityProfileAPI(req, resp, methodName, mySQLControl, theHeaderAndParameterMap);
				}
				else{
					throw new XapiBadURLException("Can't find what you are looking for.");
				}

				// STATEMENT API
			}else if(theAPI.equalsIgnoreCase("statements")){
				handleStatementAPI(req, resp, methodName, mySQLControl, theHeaderAndParameterMap);

				// AGENT PROFILE API
			}else if(theAPI.equalsIgnoreCase("agents")){
				handleAgentProfileAPI(req, resp, methodName, mySQLControl, theHeaderAndParameterMap);

			}else{
				throw new XapiBadURLException("Can't find what you are looking for");
			}
		} catch(IRISyntaxException e){
			rollback(conn, resp);
			theLogID = myLogger.log(e, 400, "IRI Not Valid", req);
			handleError(resp, 400, "IRI Not Valid", e, theLogID);
		} catch(InvalidCharacterException e){
			rollback(conn, resp);
			theLogID = myLogger.log(e, 400, "IRI Not Valid", req);
			handleError(resp, 400, "IRI Not Valid", e, theLogID);
		} catch(XapiParseException e){ 
			//e.printStackTrace();
			rollback(conn, resp);
			theLogID = myLogger.log(e, e.getErrorCode(), e.getMessage(), req);
			handleError(resp, e.getErrorCode(), e.getMessage(), e, theLogID);
		} catch (XapiServletException e){
			//e.printStackTrace();
			rollback(conn, resp);
			theLogID = myLogger.log(e, req);
			handleError(resp, e.getErrorNumber(), e.getMessage(), e, theLogID);
		} catch (JsonSyntaxException e){
			//e.printStackTrace();
			rollback(conn, resp);
			theLogID = myLogger.log(e, 400, "Having trouble parsing", req);
			handleError(resp, 400, e.getMessage(), e, theLogID);
		} catch (XapiDataIntegrityException e) {
			//e.printStackTrace();
			rollback(conn, resp);
			theLogID = myLogger.log(e, req);
			handleError(resp, e.getErrorCode(), e.getMessage(), e, theLogID);
		} catch (XapiDataNotFoundException e) {
			//e.printStackTrace();
			rollback(conn, resp);
			theLogID = myLogger.log(e, req);
			handleError(resp, e.getErrorCode(), e.getMessage(), e, theLogID);
		} catch(XapiClarescoException e){
			//e.printStackTrace();
			rollback(conn, resp);
			theLogID = myLogger.log(e, req);
			handleError(resp, e.getErrorCode(), e.getMessage(), e, theLogID);
		} catch (SQLException e) {
			//e.printStackTrace();
			rollback(conn, resp);
			theLogID = defaultLog(e, req);
			handleError(resp, 500, e.getMessage(), e, theLogID);
			myOtherLogger.logBug("SQL went wrong", e);
		} catch (Exception e) {
			//e.printStackTrace();
			rollback(conn, resp);
			theLogID = defaultLog(e, req);
			handleError(resp, 500, e.getMessage(), e, theLogID);
			
			//Log as much as possible
			StringBuffer theBuffer = new StringBuffer();
			theBuffer.append("Something went wrong\n");
			theBuffer.append("request.getRequstURI() gives :");
			if(req.getRequestURI() != null){
				theBuffer.append(req.getRequestURI() + "\n");
			}
			theBuffer.append("request.getQueryString() gives :");
			if(req.getQueryString() != null){
				theBuffer.append(req.getQueryString() + "\n");
			}
			theBuffer.append("method is :");
			if(req.getMethod() != null){
				theBuffer.append(req.getMethod() + "\n");
			}
			theBuffer.append("Path info : " + req.getPathInfo() + "\n");
			
			theBuffer.append("Reading from the request's reader gives :\n");
			try{
				theBuffer.append(XapiServletUtility.getStringFromReader(
						XapiServletUtility.getReader(req)));
			}catch(XapiServletOperationProblemException excpt){
				theBuffer.append("UNABLE to read from the reader");
			}
			
			theBuffer.append("iterating through parameter names gives :\n");
			Enumeration<String> paramNames = req.getParameterNames();
			while(paramNames.hasMoreElements()){
				String paramName = paramNames.nextElement();
				theBuffer.append(paramName + " : " + req.getParameter(paramName) + "\n");
			}
			
			
			theBuffer.append("\nthe header names :\n");
			Enumeration<String> headerNames = req.getHeaderNames();
			while(headerNames.hasMoreElements()){
				String headerName = headerNames.nextElement();
				theBuffer.append(headerName + " : " + req.getHeader(headerName) + "\n");
			}
			
			myOtherLogger.logBug(theBuffer.toString(), e);
			
		} finally{
			// Always close/release the connection
			//try{
			//	conn.commit();
			//}catch(SQLException excpt){
			//}
			XapiServletUtility.closeConnection(conn);
			XapiServletUtility.closeSQLControl(mySQLControl);

		}
	}
	
	
	
	/**
	 * 
	 * Definition:
	 *	
	 *
	 * Params:
	 *
	 *
	 */
	private HashMap<String, String> getHeaderAndParameterMap(HttpServletRequest req, 
			boolean alternateSyntax) throws XapiServletOperationProblemException, XapiBadParamException{
		HashMap<String, String> theHeaderAndParameterMap = new HashMap<String, String>();
		
		if(!alternateSyntax){
			StringBuffer theBuffer = new StringBuffer();
			Enumeration<String> e = req.getHeaderNames();
			while(e.hasMoreElements()){
				String headerName = e.nextElement();
				theBuffer.append(headerName + " : ");
				theHeaderAndParameterMap.put(headerName, req.getHeader(headerName));
				theBuffer.append(req.getHeader(headerName) + "\n");
			}
			
			BufferedReader theReader = XapiServletUtility.getReader(req);
			String theContent = XapiServletUtility.getStringFromReader(theReader);
			theHeaderAndParameterMap.put("content", theContent);
			theBuffer.append("content : " + theContent + "\n");
			
			e = req.getParameterNames();
			while(e.hasMoreElements()){
				String paramName = e.nextElement();
				theBuffer.append(paramName + " : ");
				theHeaderAndParameterMap.put(paramName, req.getParameter(paramName));
				theBuffer.append(req.getParameter(paramName) + "\n");
			}
			//myOtherLogger.keepTrack(theBuffer.toString());

		}else{
			// Uses alternate syntax, this is to support IE8 and IE9
			String theRequestBody = XapiServletUtility.getStringFromReader(XapiServletUtility.getReader(req));
			if(theRequestBody != null){
				//myOtherLogger.keepTrack("IE8 body : " + theRequestBody);
				if(!theRequestBody.isEmpty()){
					String[] theHeadersAndParams = theRequestBody.split("&");
					for(String theEntry : theHeadersAndParams){
						String[] theKeyAndValue = theEntry.split("=");
						if(theKeyAndValue.length == 2){
							try{
								String theKey = URLDecoder.decode(theKeyAndValue[0], "UTF-8");
								String theValue = URLDecoder.decode(theKeyAndValue[1], "UTF-8");
								theHeaderAndParameterMap.put(theKey, theValue);
							}catch(UnsupportedEncodingException e){
								throw new XapiServletOperationProblemException("Having trouble reading the request");
							}
						}else{
							throw new XapiBadParamException("Something wrong with the body of the message");
						}
					}
				}
			}else{
				//The request body is null
				throw new XapiBadParamException("Request body not supposed to be null");
			}
			
		}
		
		return theHeaderAndParameterMap;
	}



	/**
	 * 
	 * Definition:
	 *	Handler for Statement API
	 *
	 * Params:
	 *
	 *
	 */
	private void handleStatementAPI(HttpServletRequest req, HttpServletResponse resp, String theMethod, 
			XapiSQLControl theSQLControl, HashMap<String, String> theHeaderAndParameterMap) 
			throws  XapiServletException, SQLException, XapiClarescoException{
		// Verify that client has basic key secret
		verifyClientCredentials(theHeaderAndParameterMap);

		PrintWriter out = null;
		String theAction = getAction(req);

		// URL is bad
		if(theAction != null){
			throw new XapiBadURLException("Cannot find what you are looking for");
		}

		// Get client credentials/permissions
		XapiCredentials theClientCredentials = getClientCredentials(theHeaderAndParameterMap);

		XapiActor theActor = null;
		// timestamp's default is now
		DateTime theTimestamp = DateTime.now();
		ArrayList<XapiIRI> theActivityID = new ArrayList<XapiIRI>();
		UUID theRegistration = null;

		boolean isDefiningAllowed = false;

		// POST method
		if(theMethod.equals(POST)){
			// If it is a batch of statement, if it is not, it will return an array
			// that contains one statement only
			XapiStatementBatch theStatementBatch = myJson.deserializeStatementBatch(
					theHeaderAndParameterMap.get("content"));

			ArrayList<String> theIDArray = new ArrayList<String>();

			for(XapiStatement s : theStatementBatch){
				// This part needs to be changed if the scope of the project is expanded
				theActor = s.getActor();

				theActivityID = fillUpActivityIDs(s, theActivityID);

				// This is to help check permission
				XapiServletActionRequested theActionRequested = new XapiServletActionRequested(
						XapiServletActionType.STATEMENTWRITE,
						theActor, theTimestamp, theActivityID, theRegistration);

				// Check if this is permissible
				verifyPermission(theClientCredentials, theActionRequested);

				if(!myAccessManager.isDebugMode()){
					isDefiningAllowed = theClientCredentials.isDefiningAllowed();
				}else{
					isDefiningAllowed = true;
				}

				theSQLControl.insertNewStatement(s, isDefiningAllowed, true);

				theIDArray.add(s.getId());
			}

			sendOK(resp);

			resp.setHeader(CONTENTTYPE, JSON);
			out = XapiServletUtility.getWriter(resp);
			out.print(XapiServletUtility.createJsonArray(theIDArray));

		}else if(theMethod.equals(GET)){
			// GET Method
			verifyParams(theHeaderAndParameterMap, myStatementGetParams, "retrieving statements");

			// Get the actor
			String theAgent = theHeaderAndParameterMap.get("agent");
			if(theAgent != null){
				theActor = myJson.deserializeActor(theHeaderAndParameterMap.get("agent"));
			}


			// Get the activityId
			String theActvString = theHeaderAndParameterMap.get("activity");
			if(theActvString != null){
				theActivityID.add(new XapiIRI(theActvString));
			}

			// Get registration
			String theRegistrationString = theHeaderAndParameterMap.get("registration");
			if(theRegistrationString != null){
				theRegistration = XapiServletUtility.validateUUID(theRegistrationString);
			}

			// How do I know when they want to read any or just mine?
			XapiServletActionRequested theActionRequested = new XapiServletActionRequested(
					XapiServletActionType.STATEMENTREADMINE, theActor, theTimestamp, theActivityID,
					theRegistration);

			verifyPermission(theClientCredentials, theActionRequested);

			// This is the map of all the parameters
			HashMap<String, String> myParamMap = new HashMap<String, String>();
			// List of all the accepted param names
			String[] paramNames = new String[]{"statementId", "voidedStatementId", "agent", "verb",
					"activity", "since", "until"};
			for(String s : paramNames){
				if (theHeaderAndParameterMap.get(s) != null) {
					if(s.equals("agent")){
						int theActorID = theSQLControl.retrieveActorID(theActor);
						myParamMap.put(s, String.valueOf(theActorID));
					}else{
						myParamMap.put(s, theHeaderAndParameterMap.get(s));
					}
				}
			}

			// The function will take care of retrieving the statements from the database
			HashMap<Integer, XapiStatement> theResultMap = theSQLControl.retrieveStatements(myParamMap);

			if(theResultMap != null && !theResultMap.isEmpty()){
				XapiStatementBatch theStatementBatch = createStatementBatch(theResultMap);
				XapiStatementResult theStatementResult = new XapiStatementResult(theStatementBatch);

				sendOK(resp);
				resp.setHeader(CONTENTTYPE, JSON);
				out = XapiServletUtility.getWriter(resp);
				out.print(myJson.serializeStatementResult(theStatementResult));
			}else{
				handleError(resp, 404, "Statements not found");
			}

			// PUT Method
		}else if(theMethod.equals(PUT)){
			XapiStatement theStatement = myJson.deserializeStatement(theHeaderAndParameterMap.get("content"));

			// This verifies all the parameters
			/**
			for(Enumeration<String> paramNames = req.getParameterNames(); paramNames.hasMoreElements();){
				String paramName = paramNames.nextElement();
				if(!paramName.equals("statementId")){
					throw new XapiBadParamException(paramName + " not accepted");
				}
			}
			**/

			// If the statementId is specified, set the statement to that id
			String theStatementID = theHeaderAndParameterMap.get("statementId");
			if(theStatementID != null){
				theStatement.setID(theStatementID);
			}else{
				throw new XapiRequiredParameterNotFoundException("need statementId parameter");
			}

			theActor = theStatement.getActor();

			theActivityID = fillUpActivityIDs(theStatement, theActivityID);

			XapiServletActionRequested theActionRequested = new XapiServletActionRequested(
					XapiServletActionType.STATEMENTWRITE,
					theActor, theTimestamp, theActivityID, theRegistration);

			// Check if this is permissible
			verifyPermission(theClientCredentials, theActionRequested);

			if(!myAccessManager.isDebugMode()){
				isDefiningAllowed = theClientCredentials.isDefiningAllowed();
			}else{
				isDefiningAllowed = true;
			}

			theSQLControl.insertNewStatement(theStatement, isDefiningAllowed, false);

			sendNoContent(resp);
		}else{
			throw new XapiAPIMethodNotSupportedException("Basic API does not support this method : " + theMethod);
		}
	}



	/**
	 * 
	 * Definition:
	 *	Handler for Basic API which is mainly for requesting client's permission
	 *
	 * Params:
	 *
	 *
	 */
	private void handleBasicAPI(HttpServletRequest req, HttpServletResponse resp, String theMethod, 
			XapiSQLControl theSQLControl, HashMap<String, String> theHeaderAndParameterMap) throws XapiServletException{
		PrintWriter out = null;
		String theAction = getAction(req);
		if(XapiServletUtility.checkAction(theAction, "request")){
			// If LMS is authorized
			verifyLMSCredentials(theHeaderAndParameterMap);

			if(theMethod.equals(PUT) || theMethod.equals(POST)){
				// Check for invalid params
				verifyParams(theHeaderAndParameterMap, myCredentialsRequestParams);

				XapiKeySecret theKeySecret = generateClientCredentials(theHeaderAndParameterMap.get("content"));

				out = XapiServletUtility.getWriter(resp);
				out.print(myJson.createKeySecretJson(theKeySecret));
				resp.setContentType(JSON);
				sendOK(resp);

				//myOtherLogger.keepTrack("Credentials sent");
			}else{
				throw new XapiAPIMethodNotSupportedException("Basic API does not support this method");
			}
		/**
		}else if(XapiServletUtility.checkAction(theAction, "debug")){
			if(theMethod.equals(POST)){
				verifyLMSCredentials(theHeaderAndParameterMap);

				String theValue = theHeaderAndParameterMap.get("debugMode");
				if(theValue.equalsIgnoreCase("on")){
					isDebugMode = true;
					isEpicDebugMode = true;
				}
				else if(theValue.equalsIgnoreCase("off")){
					isDebugMode = false;
					isEpicDebugMode = false;
				}else{
					throw new XapiBadParamException("value of param debugMode is not accepted");
				}

				sendNoContent(resp);
			}else{
				throw new XapiAPIMethodNotSupportedException("Basic API does not support this method");
			}
		**/
		}else{
			throw new XapiBadURLException("Cannot find what you are looking for");
		}
	}



	/**
	 * 
	 * Definition:
	 *	Handler for the state API
	 *
	 * Params:
	 *
	 *
	 */
	private void handleStateAPI(HttpServletRequest req, HttpServletResponse resp, String theMethod, XapiSQLControl
			theSQLControl, HashMap<String, String> theHeaderAndParameterMap) throws XapiServletException,
			SQLException, XapiClarescoException{
		PrintWriter out = null;
		String theAction = getAction(req);

		verifyClientCredentials(theHeaderAndParameterMap);

		// Retrieve client credentials
		XapiCredentials theClientCredentials = getClientCredentials(theHeaderAndParameterMap);

		// Default values
		XapiActor theActor = null;
		DateTime theTimestamp = DateTime.now();
		XapiIRI theActivityID = null;
		UUID theRegistration = null;
		XapiServletActionRequested theActionRequested = null;

		// PUT and POST method
		if(theMethod.equals(PUT) || theMethod.equals(POST)){
			if(XapiServletUtility.checkAction(theAction, "state")){

				// If this is not called before verifyParams, its gonna get messy
				String theResult = theHeaderAndParameterMap.get("content");

				// Check for invalid params
				verifyParams(theHeaderAndParameterMap, myStateSingleParams);

				// Getting all the parameters
				String theStateID = theHeaderAndParameterMap.get("stateId");
				String theActvID = theHeaderAndParameterMap.get("activityId");

				String theActorString = theHeaderAndParameterMap.get("agent");
				if(theActorString != null){
					theActor = myJson.deserializeActor(theActorString);
				}

				String theStateRegistration = theHeaderAndParameterMap.get("registration");

				// Check if any of the required parameters is missing
				if(theStateID == null || theActvID == null || theActor == null){
					throw new XapiRequiredParameterNotFoundException("Missing the required params");
				}

				theActivityID = new XapiIRI(theActvID);

				if(theStateRegistration != null){
					theRegistration = XapiServletUtility.validateUUID(theStateRegistration);
				}

				XapiState theState = new XapiState(theStateID, theActvID, theActor, theStateRegistration, theResult);

				// To help with checking permission
				theActionRequested = new XapiServletActionRequested(XapiServletActionType.STATEWRITE, theActor,
						theTimestamp, theActivityID, theRegistration);

				// Check if this is permissible
				verifyPermission(theClientCredentials, theActionRequested);

				theSQLControl.insertState(theState);

				sendNoContent(resp);

			}else{
				throw new XapiBadURLException("Cannot find what you are looking for");
			}
		}else if(theMethod.equals(GET)){
			try{
				verifyParams(theHeaderAndParameterMap, myStateSingleGetParams, "single GET request");

				// Getting all the params
				String theStateID = theHeaderAndParameterMap.get("stateId");
				String theActvID = theHeaderAndParameterMap.get("activityId");
				String theAgentParameter = theHeaderAndParameterMap.get("agent");

				// Check if required parameter exists
				if(theStateID == null || theActvID == null || theAgentParameter == null){
					throw new XapiRequiredParameterNotFoundException("One of the required params not found");
				}

				theActor = myJson.deserializeActor(theAgentParameter);

				theActivityID = new XapiIRI(theActvID);

				String theStateRegistration = theHeaderAndParameterMap.get("registration");
				if(theStateRegistration != null){
					theRegistration = XapiServletUtility.validateUUID(theStateRegistration);
				}

				theActionRequested = new XapiServletActionRequested(XapiServletActionType.STATEREAD, theActor,
						theTimestamp, theActivityID, theRegistration);

				// Check permission
				verifyPermission(theClientCredentials, theActionRequested);

				String theDocument;

				if(theRegistration != null){
					theDocument = theSQLControl.retrieveState(theActvID, theActor, theStateID, theStateRegistration); 
				}else{
					theDocument = theSQLControl.retrieveState(theActvID, theActor, theStateID); 
				}
				// Check if there is such document
				if(theDocument != null){
					out = XapiServletUtility.getWriter(resp);
					out.print(theDocument);
					resp.setContentType(JSON);
					sendOK(resp);
				}else{
					// or should we throw an exception instead?
					handleError(resp, 404, "Document not found");
					//sendNoContent(resp);
				}
			}catch(XapiBadParamException e){
				out = XapiServletUtility.getWriter(resp);
				handleMultipleGetState(req, resp, theClientCredentials, theSQLControl, out,
						theHeaderAndParameterMap);			
			}

		}else{
			throw new XapiAPIMethodNotSupportedException("State API does not have this method");
		}
	}



	/**
	 * 
	 * Definition:
	 *	Helper method to handle Multiple GET request for State API
	 *
	 * Params:
	 *
	 *
	 */
	private void handleMultipleGetState(HttpServletRequest req, HttpServletResponse resp, XapiCredentials 
			theClientCredentials, XapiSQLControl theSQLControl, PrintWriter out,
			HashMap<String, String> theHeaderAndParameterMap)
			throws XapiServletException, SQLException, XapiClarescoException{
		verifyParams(theHeaderAndParameterMap, myStateMultipleGetParams, "multiple get request");

		// Get the params
		String theActvID = theHeaderAndParameterMap.get("activityId");
		String theAgentParam = theHeaderAndParameterMap.get("agent");
		XapiActor theActor = null;
		if(theAgentParam != null){
			theActor = myJson.deserializeActor(theAgentParam);
		}
		String theStateRegistration = theHeaderAndParameterMap.get("registration");
		String theStringTS = theHeaderAndParameterMap.get("since");

		// Check if any of the required params is missing
		if(theActvID == null || theActor == null){
			throw new XapiRequiredParameterNotFoundException("either the activityId or the agent is missing");
		}

		XapiIRI theActivityID = new XapiIRI(theActvID);

		UUID theRegistration = null;
		if(theStateRegistration != null){
			theRegistration = XapiServletUtility.validateUUID(theStateRegistration);
		}

		DateTime theSince = null;
		if(theStringTS != null){
			DateTimeFormatter theFormatter = ISODateTimeFormat.dateTimeParser();
			try{
				theSince = theFormatter.parseDateTime(theStringTS);
			}catch(IllegalArgumentException exc){
				throw new XapiServletOperationProblemException("Having trouble parsing the timestamp");
			}

		}

		HashMap<String, String> result;

		DateTime theTimestamp = DateTime.now();

		XapiServletActionRequested theActionRequested = new XapiServletActionRequested(XapiServletActionType.STATEREAD,
				theActor, theTimestamp, theActivityID, theRegistration);

		verifyPermission(theClientCredentials, theActionRequested);

		// Case 1 : requests include registration and since
		if(theStateRegistration != null && theStringTS != null){
			result = theSQLControl.retrieveMultipleState(theActvID, theActor, theStateRegistration, theSince);
		}else if(theStateRegistration == null && theStringTS == null){
			// Case 2 : requests do not include registration and since	
			result = theSQLControl.retrieveMultipleState(theActvID, theActor);
		}else if(theStringTS == null){
			// Case 3 : requests only include registration
			result = theSQLControl.retrieveMultipleState(theActvID, theActor, theStateRegistration);
		}else{
			// Case 4 : requests only include since
			result = theSQLControl.retrieveMultipleState(theActvID, theActor, theSince);
		}

		// Throw an exception instead
		if(result == null){
			handleError(resp, 404, "Document not found");
		}else{
			resp.setContentType(JSON);
			out.print(XapiServletUtility.createJsonArray(result.keySet()));
		}
	}



	/**
	 * 
	 * Definition:
	 *	Handling request as defined under Activity Profile API
	 *
	 * Params:
	 *
	 *
	 */
	private void handleActivityProfileAPI(HttpServletRequest req, HttpServletResponse resp, String theMethod,
			XapiSQLControl theSQLControl, HashMap<String, String> theHeaderAndParameterMap)
			throws XapiServletException, SQLException, XapiClarescoException{
		PrintWriter out = null;
		String theAction = getAction(req);


		// Verify Client Login and Password
		verifyClientCredentials(theHeaderAndParameterMap);

		XapiCredentials theClientCredentials = getClientCredentials(theHeaderAndParameterMap);

		XapiActor theActor = null;
		DateTime theTimestamp = DateTime.now();
		XapiIRI theActivityID = null;
		UUID theRegistration = null;

		XapiServletActionRequested theActionRequested= null;

		if(theAction == null){
			// GET
			if(theMethod.equals(GET)){
				ArrayList<String> acceptableParams = new ArrayList<String>();
				acceptableParams.add("activityId");

				// Check if all params are accepted
				verifyParams(theHeaderAndParameterMap, acceptableParams);

				String theActvID = theHeaderAndParameterMap.get("activityId");

				if(!myAccessManager.isDebugMode()){
					theActor = theClientCredentials.getActor();
				}

				// To help checking permission
				theActionRequested = new XapiServletActionRequested(XapiServletActionType.PROFILEREAD,
						theActor, theTimestamp, new XapiIRI(theActvID), theRegistration);

				// Check permission
				verifyPermission(theClientCredentials, theActionRequested);

				// Retrieve activity
				XapiActivity theActv = theSQLControl.retrieveActivity(theActvID);

				// Throw an error instead???
				if(theActv == null){
					handleError(resp, 404, "Activity not found");
				}else{
					// Serialize the activity
					String myActivityJson = myJson.serializeActivity(theActv);

					sendOK(resp);

					resp.setHeader(CONTENTTYPE, JSON);
					out = XapiServletUtility.getWriter(resp);
					out.println(myActivityJson);
				}

			}else{
				throw new XapiBadURLException("Can't find what you are looking for");
			}
		}else if(theAction.equals("profile")){
			// PUT and POST
			if(theMethod.equals(PUT) || theMethod.equals(POST)){

				// If this is not called before verifyParams, it will not behave as we want it.
				String theDocument = theHeaderAndParameterMap.get("content");

				verifyParams(theHeaderAndParameterMap, myActivityProfileSingleParams);

				String theActvID = theHeaderAndParameterMap.get("activityId");

				if(!myAccessManager.isDebugMode()){
					theActor = theClientCredentials.getActor();
				}

				if(theActvID != null){
					theActivityID = new XapiIRI(theActvID);
				}

				theActionRequested = new XapiServletActionRequested(XapiServletActionType.PROFILEWRITE,
						theActor, theTimestamp, theActivityID, theRegistration);

				verifyPermission(theClientCredentials, theActionRequested);

				String theProfileID = theHeaderAndParameterMap.get("profileId");

				if(theActvID == null || theProfileID == null){
					throw new XapiRequiredParameterNotFoundException("Missing required params");
				}

				theSQLControl.insertActivityProfile(theActvID, theProfileID, theDocument);

				sendNoContent(resp);

			}
			else if(theMethod.equals(GET)){
				// GET
				try{
					verifyParams(theHeaderAndParameterMap, myActivityProfileSingleGetParams);

					String theActvID = theHeaderAndParameterMap.get("activityId");
					String theProfileID = theHeaderAndParameterMap.get("profileId");

					// If any of the required params is missing, the request could be a multiple get request
					if(theActvID == null || theProfileID == null){
						throw new XapiRequiredParameterNotFoundException("Missing required params");
					}

					if(!myAccessManager.isDebugMode()){
						theActor = theClientCredentials.getActor();
					}

					theActivityID = new XapiIRI(theActvID);

					theActionRequested = new XapiServletActionRequested(XapiServletActionType.PROFILEREAD,
							theActor, theTimestamp, theActivityID, theRegistration);

					verifyPermission(theClientCredentials, theActionRequested);

					String theDocument = theSQLControl.retrieveActivityProfile(theActvID, theProfileID);

					out = XapiServletUtility.getWriter(resp);

					if(theDocument != null){
						sendOK(resp);
						resp.setContentType(JSON);
						out.println(theDocument);
					}else{
						handleError(resp, 404, "Document not found");
					}
				}catch(XapiBadParamException e){
					// If exception is thrown, this request could be a multiple get request
					verifyParams(theHeaderAndParameterMap, myActivityProfileMultipleGetPrams, "multiple get request");

					// Get all params
					String theActvID = theHeaderAndParameterMap.get("activityId");
					String theStringTS = theHeaderAndParameterMap.get("since");

					// Make sure activity ID not null
					if(theActvID == null){
						throw new XapiRequiredParameterNotFoundException("Activity ID can't be null");
					}

					if(!myAccessManager.isDebugMode()){
						theActor = theClientCredentials.getActor();
					}

					theActivityID = new XapiIRI(theActvID);
					theActionRequested = new XapiServletActionRequested(XapiServletActionType.PROFILEREAD,
							theActor, theTimestamp, theActivityID, theRegistration);

					// Verifying that the request is permissible
					verifyPermission(theClientCredentials, theActionRequested);

					HashMap<String, String> result;

					// If there is no since param
					if(theStringTS == null){
						result = theSQLControl.retrieveMultipleActivityProfile(theActvID);
						// If there is
					}else{
						DateTimeFormatter theFormatter = ISODateTimeFormat.dateTimeParser();
						try{
							DateTime myTimeStamp = theFormatter.parseDateTime(theStringTS);	
							result = theSQLControl.retrieveMultipleActivityProfile(theActvID,
									myTimeStamp);
						}catch(IllegalArgumentException exception){
							//exception.printStackTrace();
							defaultLog(exception, req);
							throw new XapiServletOperationProblemException("Having trouble parsing the timestamp." + 
									"Perhaps you should check it again");
						}

					}

					if(result != null){
						out = XapiServletUtility.getWriter(resp);
						out.print(XapiServletUtility.createJsonArray(result.keySet()));
						resp.setContentType(JSON);
					}else{
						handleError(resp, 404, "Documents not found");
					}
				}

			}else{
				throw new XapiBadURLException("I do not know what you want");
			}
		}
	}



	/**
	 * 
	 * Definition:
	 *	Method to handle Agent Profile API
	 *
	 * Params:
	 *
	 *
	 */
	private void handleAgentProfileAPI(HttpServletRequest req, HttpServletResponse resp, String theMethod, 
			XapiSQLControl theSQLControl, HashMap<String, String> theHeaderAndParameterMap)
			throws XapiServletException, XapiClarescoException, SQLException{
		PrintWriter out = null;

		out = XapiServletUtility.getWriter(resp);

		String theAction = getAction(req);

		// Verify Client Login and Password
		verifyClientCredentials(theHeaderAndParameterMap);

		XapiCredentials theClientCredentials = getClientCredentials(theHeaderAndParameterMap);

		XapiActor theActor = null;
		DateTime theTimestamp = DateTime.now();
		XapiIRI theActivityID = null;
		UUID theRegistration = null;

		XapiServletActionRequested theActionRequested= null;

		if(theAction == null){

		}else if(theAction.equals("profile")){
			// PUT and POST
			if(theMethod.equals(POST) || theMethod.equals(PUT)){
				String theDocument = theHeaderAndParameterMap.get("content");

				verifyParams(theHeaderAndParameterMap, myAgentProfileSingleParams);

				String theAgentParam = theHeaderAndParameterMap.get("agent");
				if(theAgentParam != null){
					theActor = myJson.deserializeActor(theAgentParam);
				}

				String theProfileID = theHeaderAndParameterMap.get("profileId");

				if(theAgentParam == null || theProfileID == null){
					throw new XapiRequiredParameterNotFoundException("agent and profileid can't be null");
				}

				theActionRequested = new XapiServletActionRequested(XapiServletActionType.PROFILEWRITE, theActor,
						theTimestamp, theActivityID, theRegistration);

				verifyPermission(theClientCredentials, theActionRequested);

				theSQLControl.insertNewAgentProfile(theActor, theProfileID, theDocument);

				sendNoContent(resp);
				// GET Method
			}else if(theMethod.equals(GET)){
				try{
					verifyParams(theHeaderAndParameterMap, myAgentProfileSingleParams);

					String theActorString = theHeaderAndParameterMap.get("agent");
					if(theActorString != null){
						theActor = myJson.deserializeActor(theActorString);
					}

					String theProfileID = theHeaderAndParameterMap.get("profileId");

					if(theActor == null || theProfileID == null){
						throw new XapiRequiredParameterNotFoundException("either agent or profileId is missing");
					}

					theActionRequested = new XapiServletActionRequested(XapiServletActionType.PROFILEREAD, theActor,
							theTimestamp, theActivityID, theRegistration);

					verifyPermission(theClientCredentials, theActionRequested);

					String theResult = theSQLControl.retrieveSingleAgentProfile(theActor, theProfileID);

					if(theResult == null){
						handleError(resp, 404, "Document not found");
					}else {
						out = XapiServletUtility.getWriter(resp);
						out.println(theResult);
						resp.setContentType(JSON);
					}
				}catch(XapiBadParamException e){
					verifyParams(theHeaderAndParameterMap, myAgentProfileMultipleParams);

					String theAgentString = theHeaderAndParameterMap.get("agent");
					String theStringTS = theHeaderAndParameterMap.get("since");

					if(theAgentString == null){
						throw new XapiRequiredParameterNotFoundException("can't find the required parameter : agent");
					}

					theActor = myJson.deserializeActor(theAgentString);

					theActionRequested = new XapiServletActionRequested(XapiServletActionType.PROFILEREAD, theActor,
							theTimestamp, theActivityID, theRegistration);

					verifyPermission(theClientCredentials, theActionRequested);

					HashMap<String, String> theResult;

					// If there is no since param
					if(theStringTS == null){
						theResult = theSQLControl.retrieveMultipleAgentProfile(theActor);
						// If there is
					}else{
						DateTimeFormatter theFormatter = ISODateTimeFormat.dateTimeParser();

						try{
							DateTime myTimeStamp = theFormatter.parseDateTime(theStringTS);
							theResult = theSQLControl.retrieveMultipleAgentProfile(theActor,
									myTimeStamp);
						}catch(IllegalArgumentException exc){
							//exc.printStackTrace();
							defaultLog(exc, req);
							throw new XapiServletOperationProblemException("Having trouble parsing your timestamp");
						}
					}

					if(theResult != null){
						out = XapiServletUtility.getWriter(resp);
						out.print(XapiServletUtility.createJsonArray(theResult.keySet()));
						resp.setContentType(JSON);
					}else{
						handleError(resp, 404, "Documents not found");
					}
				}

			}
		}
	}



	/**
	 * 
	 * Definition:
	 * 	This is to verify if the client has permission to proceed with request
	 *
	 * Params:
	 *
	 *
	 */
	private void verifyPermission(XapiCredentials theCredentials , XapiServletActionRequested theAction)
			throws XapiForbiddenException{
		// If it is not any debug mode
		if(!myAccessManager.isDebugMode()){
			if(!(theCredentials instanceof XapiLMSCredentials) && !theCredentials.isActionRequestedAllow(theAction)){
				myOtherLogger.logThreats("User try something fancy");
				throw new XapiForbiddenException("Nice try, punk! You are not allowed to do so");
			}
		}
	}



	/**
	 * 
	 * Definition:
	 *	Handle cross domain request
	 *
	 * Params:
	 *
	 *
	 */
	private void handleCORS(HttpServletRequest req, HttpServletResponse resp){
		String theOriginURL = XapiServletUtility.getOriginURL(req);
		String theMethodName = req.getHeader("Access-Control-Request-Method");
		String theHeaderRequested = req.getHeader("Access-Control-Request-Headers");

		String headersToBeAllowed = XapiServletUtility.createHeaderString(myAllowedHeaderList);
		if(!myAllowedHeaderList.contains(theHeaderRequested)){
			headersToBeAllowed += "," + theHeaderRequested;
		}

		String methodToBeAllowed = POST + "," + PUT + "," + GET;


		if(myAllowedDomainList.contains(theOriginURL)){
			resp.setHeader("Access-Control-Allow-Origin", theOriginURL);
			resp.setHeader("Access-Control-Allow-Methods", theMethodName);
			resp.setHeader("Access-Control-Allow-Headers", headersToBeAllowed);
		}

		resp.setHeader("Vary", "Origin");
		resp.setIntHeader("Access-Control-Max-Age", 17280);
	}



	/**
	 * 
	 * Definition:
	 *	Helper method to respond to CORS
	 *
	 * Params:
	 *
	 *
	 */
	private boolean checkCORSPermission(HttpServletRequest req, HttpServletResponse resp){
		String theOriginURL = XapiServletUtility.getOriginURL(req);

		return myAllowedDomainList.contains(theOriginURL);
	}



	/**
	 * 
	 * Definition:
	 *	Helper method to respond to CORS
	 *
	 * Params:
	 *
	 *
	 */
	private void addCORSResponse(HttpServletRequest req, HttpServletResponse resp){
		String theOriginURL = XapiServletUtility.getOriginURL(req);

		resp.setHeader("Access-Control-Allow-Origin", theOriginURL);
	}



	/**
	 * 
	 * Definition:
	 *	Helper method to respond to CORS
	 *
	 * Params:
	 *
	 *
	 */
	private void respondToCORS(HttpServletRequest req, HttpServletResponse resp){
		if(checkCORSPermission(req, resp)){
			addCORSResponse(req, resp);
		}
	}



	private void handleError(HttpServletResponse resp, int errorNumber, String theMessage, Exception exc, long longID){
		try{
			if(myAccessManager.isDebugMode()){
				//resp.sendError(errorNumber, theMessage);
				resp.setContentType("text/plain");
				resp.setStatus(errorNumber);
				PrintWriter out = resp.getWriter();
				out.print("Log ID : " + longID + "\n");
				exc.printStackTrace(out);
			}else{
				handleError(resp, errorNumber, theMessage);
			}
		}catch(Exception e){

		}
	}



	/**
	 * 
	 * Definition:
	 *	Handling error, if an exception is thrown
	 *
	 * Params:
	 *
	 *
	 */
	private void handleError(HttpServletResponse resp, int errorNumber, String theMessage){
		try {
			resp.setContentType(PLAINTEXT);
			resp.setStatus(errorNumber);
			PrintWriter out = XapiServletUtility.getWriter(resp);
			out.print(theMessage);
		} catch (Exception e) {
		}		
	}



	private void handleError(HttpServletResponse resp, int errorNumber){
		try {
			resp.setContentType(PLAINTEXT);
			resp.setStatus(errorNumber);
			PrintWriter out = XapiServletUtility.getWriter(resp);
			out.print(errorNumber);
		} catch (Exception e) {
		}		
	}



	/**
	 * 
	 * Definition:
	 *  Sending an OK (200 status)
	 *
	 * Params:
	 *
	 *
	 */
	private void sendOK(HttpServletResponse resp){
		resp.setStatus(200);
	}



	/**
	 * 
	 * Definition:
	 *	Sending a 204
	 *
	 * Params:
	 *
	 *
	 */
	private void sendNoContent(HttpServletResponse resp){
		resp.setStatus(204);
	}



	/**
	 * 
	 * Definition:
	 *	This function will check if the LMS is authorized to do so
	 *	If not throw and exception
	 *
	 * Params:
	 *
	 *
	 */
	private void verifyLMSCredentials(HashMap<String, String> theHeaderAndParameterMap) throws XapiNotAuthorizedException,
	XapiServletOperationProblemException{
		if(!doesLMSCredentialsExists(theHeaderAndParameterMap)){
			throw new XapiNotAuthorizedException("LMS is not authorized to do so");
		}
	}



	/**
	 * 
	 * Definition:
	 *	This function will check if the client is authorized
	 *	If not throw and exception
	 *
	 * Params:
	 *
	 *
	 */
	private void verifyClientCredentials(HashMap<String, String> theHeaderAndParameterMap) throws
			XapiNotAuthorizedException, XapiServletOperationProblemException{
		if(!myAccessManager.isDebugMode()){
			if(!doesClientCredentialExists(theHeaderAndParameterMap) && 
					!doesLMSCredentialsExists(theHeaderAndParameterMap)){
				myOtherLogger.logThreats("User try something fancy");
				
				throw new XapiNotAuthorizedException("Client not authorized to do so");
			}
		}
	}



	/**
	 * 
	 * Definition:
	 *	Check if the LMS is using the correct key-secret combination
	 *
	 * Params:
	 *
	 *
	 */
	private boolean doesLMSCredentialsExists(HashMap<String, String> theHeaderAndParameterMap) throws XapiServletOperationProblemException{
		// In the header, the format has to be 'login:password'
		String[] theLoginInfo = handleAuthorizationHeader(theHeaderAndParameterMap);

		return doesLMSCredentialsExists(theLoginInfo);
	}


	private boolean doesLMSCredentialsExists(String[] theLoginInfo){
		if(!myAccessManager.isDebugMode()){
			if(theLoginInfo == null){
				return false;
			}

			// Length has to be 2
			if(theLoginInfo.length != 2){
				return false;
			}

			return myAccessManager.containServerKeySecret(new XapiKeySecret(theLoginInfo[0],
					theLoginInfo[1]));
		}else{
			return true;
		}
	}



	/**
	 * 
	 * Definition:
	 *	Check if the client is using an existing key-secret combination
	 *
	 * Params:
	 *
	 *
	 */
	private boolean doesClientCredentialExists(HashMap<String, String> theHeaderAndParameterMap)
			throws XapiServletOperationProblemException{
		if(!myAccessManager.isDebugMode()){
			String[] theLoginInfo = handleAuthorizationHeader(theHeaderAndParameterMap);

			// There has to be authorization header
			if(theLoginInfo == null){
				return false;
			}

			// Length has to be 2
			if(theLoginInfo.length != 2){
				return false;
			}

			XapiKeySecret theKeySecret = new XapiKeySecret(theLoginInfo[0], theLoginInfo[1]);
			
			if(myAccessManager.containsClientCredentials(theKeySecret)){
				myAccessManager.addLastAccessedTime(theKeySecret, DateTime.now());
				return true;
			}
			
			return false;
		}else{
			return true;
		}
	}



	/**
	 * 
	 * Definition:
	 *	Getting client credentials from the HTTPServletRequest
	 *
	 * Params:
	 *
	 *
	 */
	private XapiCredentials getClientCredentials(HashMap<String, String> theHeaderAndParameterMap)
			throws XapiServletSecurityRiskException, XapiServletOperationProblemException{
		if(myAccessManager.isDebugMode()){
			return new XapiCredentials(myAccessManager.isDebugMode());
		}
		String[] theLoginInfo = handleAuthorizationHeader(theHeaderAndParameterMap);

		if(theLoginInfo == null){
			throw new XapiServletSecurityRiskException("No authorization header");
		}

		if(doesLMSCredentialsExists(theLoginInfo)){
			return new XapiLMSCredentials();
		}

		return myAccessManager.getCredential(new XapiKeySecret(theLoginInfo[0], theLoginInfo[1]));
	}



	/**
	 * 
	 * Definition:
	 *	Helper method to get key secret passed on as Authorization header
	 *
	 * Params:
	 *
	 *
	 */
	private String[] handleAuthorizationHeader(HashMap<String, String> theHeaderAndParameterMap) throws 
	XapiServletOperationProblemException{
		String theAuthorizationHeader = theHeaderAndParameterMap.get("authorization");

		if(theAuthorizationHeader == null){
			theAuthorizationHeader = theHeaderAndParameterMap.get("Authorization");
		}
		
		if(theAuthorizationHeader == null){
			return null;
		}
		
		theAuthorizationHeader = theAuthorizationHeader.trim();
		if(!theAuthorizationHeader.startsWith("Basic")){
			return null;
		}

		String temp = theAuthorizationHeader.substring(5);
		temp = temp.trim();
		try{
			temp = XapiServletUtility.decodeBase64(temp);
		}catch(UnsupportedEncodingException e){
			throw new XapiServletOperationProblemException("Can't decode the message");
		}

		String[] theLoginInfo = temp.split(":");

		return theLoginInfo;
	}



	/**
	 * 
	 * Definition:
	 *	Assuming that the second string in the array is "xapi"
	 *
	 * Params:
	 *
	 *
	 */
	private boolean isURLSupported(String[] urlArray){
		// Too short
		if(urlArray.length < 2){
			return false;
		}

		// Too long
		if(urlArray.length > 3){
			return false;
		}

		// does not have keyword 'xapi'
		if(!urlArray[0].equalsIgnoreCase("xapi")){
			return false;
		}

		return true;
	}



	/**
	 * 
	 * Definition:
	 *	Checking if URL is valid
	 *
	 * Params:
	 *
	 *
	 */
	private void checkURL(HttpServletRequest req, HttpServletResponse resp) throws XapiBadURLException{
		String[] urlArray = XapiServletUtility.getRequestURLArray(req);
		if (!isURLSupported(urlArray)){
			throw new XapiBadURLException("We do not support that URL");
		}
	}



	/**
	 * 
	 * Definition:
	 *	Get the API from the URL
	 *
	 * Params:
	 *
	 *
	 */
	private String getAPI(HttpServletRequest req){
		return XapiServletUtility.getRequestURLArray(req)[1];
	}



	/**
	 * 
	 * Definition:
	 *	Get the string after the API, we call it action here
	 *
	 * Params:
	 *
	 *
	 */
	private String getAction(HttpServletRequest req){
		String[] urlArray = XapiServletUtility.getRequestURLArray(req);
		if(urlArray.length == 2){
			return null;
		}
		return urlArray[2];
	}



	/**
	 * 
	 * Definition:
	 *	Getting Accepted KeySecret for LMS
	 *
	 * Params:
	 *
	 *
	 */
	private void getServerKeySecret(){
		String theKey = myServletContext.getInitParameter("LMSLogin");
		String theSecret = myServletContext.getInitParameter("LMSPassword");

		myAccessManager.addServerKeySecret(new XapiKeySecret(theKey, theSecret));
	}



	/**
	 * 
	 * Definition:
	 *	Generate client key secret
	 *
	 * Params:
	 *
	 *
	 */
	private XapiKeySecret generateClientCredentials(BufferedReader theReader) throws XapiParseException, 
	XapiServletOperationProblemException{
		StringBuilder theBuilder = new StringBuilder();
		String line;

		try{
			// Need this to turn the request into json
			while ((line = theReader.readLine()) != null) {
				theBuilder.append(line).append('\n');
			}

			return generateClientCredentials(theBuilder.toString());
		}catch(IOException e){
			myOtherLogger.logBug("Credentials problem", e);
			throw new XapiServletOperationProblemException("Having problem reading the request");
		}

	}
	
	
	
	/**
	 * 
	 * Definition:
	 *	Generate client key secret
	 *
	 * Params:
	 *
	 *
	 */
	private XapiKeySecret generateClientCredentials(String theContent) throws XapiParseException, 
	XapiServletOperationProblemException{
		try{
			// What are they sending
			myOtherLogger.keepTrack(theContent);

			XapiCredentials theCredentials = myJson.deserializeCredentials(theContent);
			XapiKeySecret theKeySecret = mySecretKeyGenerator.getLoginInformation(theContent);
			myAccessManager.addCrendential(theKeySecret, theCredentials);

			myOtherLogger.keepTrack(theKeySecret.getKey() + ":" + theKeySecret.getSecret());
			return theKeySecret;
		}catch(IOException e){
			myOtherLogger.logBug("Credentials problem", e);
			throw new XapiServletOperationProblemException("Having problem reading the request");
		}
	}



	/**
	 * 
	 * Definition:
	 *	Checking if the params of the request are all part of the allowed params
	 *	Does not have to be on
	 *
	 * Params:
	 *
	 *
	 */
	private void verifyParams(HashMap<String, String> theHeaderAndParameterMap, ArrayList<String> acceptedParamList) throws 
	XapiBadParamException{
		/**
		for(Enumeration<String> paramNames = req.getParameterNames(); paramNames.hasMoreElements();){
			String paramName = paramNames.nextElement();

			if(!acceptedParamList.contains(paramName)){
				throw new XapiBadParamException("Param name : " + paramName + " not accepted");
			}
		}
		 **/
	}



	/**
	 * 
	 * Definition:
	 *	Checking if the params of the request are all part of the allowed params
	 *	Does not have to be on	
	 *
	 * Params:
	 *
	 *
	 */
	private void verifyParams(HashMap<String, String> theHeaderAndParameterMap, ArrayList<String> acceptedParamList, 
			String typeOfRequest) throws XapiBadParamException{
		/**
		for(Enumeration<String> paramNames = req.getParameterNames(); paramNames.hasMoreElements();){
			String paramName = paramNames.nextElement();

			if(!acceptedParamList.contains(paramName)){
				throw new XapiBadParamException("Param name : " + paramName + " not accepted for "
						+ typeOfRequest);
			}
		}
		 **/
	}



	/**
	 * 
	 * Definition:
	 *	Helper function -- create StatementBatch
	 *
	 * Params:
	 *
	 *
	 */
	private XapiStatementBatch createStatementBatch(HashMap<Integer, XapiStatement> theStatements){
		XapiStatementBatch theStatementBatch = new XapiStatementBatch();

		for(Integer i : theStatements.keySet()){
			theStatementBatch.addStatementToBatch(theStatements.get(i));
		}

		return theStatementBatch;
	}



	private ArrayList<XapiIRI> fillUpActivityIDs(XapiStatement theStatement, ArrayList<XapiIRI> theList){
		if(theStatement.getObject() instanceof XapiActivity){
			theList.add(((XapiActivity) theStatement.getObject()).getId());
		}

		// Add other possible activity IDs
		if(theStatement.hasContext()){
			XapiContext theContext = theStatement.getContext();
			if(theContext.hasContextActivities()){
				XapiContextActivities theContextActivities = theContext.getContextActivities();

				if(theContextActivities.hasParent()){
					theList = addActivityIDs(theList, theContextActivities.getParent());
				}
				if(theContextActivities.hasGrouping()){
					theList = addActivityIDs(theList, theContextActivities.getGrouping());
				}
				if(theContextActivities.hasCategory()){
					theList = addActivityIDs(theList, theContextActivities.getCategory());
				}
				if(theContextActivities.hasOther()){
					theList = addActivityIDs(theList, theContextActivities.getOther());
				}
			}
		}

		return theList;
	}


	private ArrayList<XapiIRI> addActivityIDs(ArrayList<XapiIRI> theList, ArrayList<XapiActivity> theActivities){
		for(XapiActivity theActivity : theActivities){
			theList.add(theActivity.getId());
		}

		return theList;
	}



	/**
	 * 
	 * Definition:
	 *	All the next methods down below are to populate the list of allowed params
	 *
	 * Params:
	 *
	 *
	 */
	private void populateCredentialRequestParams(){
		myCredentialsRequestParams = new ArrayList<String>();
		myCredentialsRequestParams.add("scope");
		myCredentialsRequestParams.add("expiry");
		myCredentialsRequestParams.add("historical");
		myCredentialsRequestParams.add("actors");
		myCredentialsRequestParams.add("activity");
		myCredentialsRequestParams.add("registration");
	}



	private void populateStatementGetParams(){
		myStatementGetParams = new ArrayList<String>();
		myStatementGetParams.add("statementId");
		myStatementGetParams.add("agent");
		myStatementGetParams.add("verb");
		myStatementGetParams.add("activity");
		myStatementGetParams.add("registration");
		myStatementGetParams.add("since");
		myStatementGetParams.add("until");
		myStatementGetParams.add("limit");
		myStatementGetParams.add("related_activities");
		myStatementGetParams.add("related_agents");
	}



	private void populateStateSingleParams(){
		myStateSingleParams = new ArrayList<String>();
		myStateSingleParams.add("activityId");
		myStateSingleParams.add("agent");
		myStateSingleParams.add("registration");
		myStateSingleParams.add("stateId");
	}

	private void populateStateSingleGetParams(){
		myStateSingleGetParams = new ArrayList<String>();
		myStateSingleGetParams.add("activityId");
		myStateSingleGetParams.add("agent");
		myStateSingleGetParams.add("registration");
		myStateSingleGetParams.add("stateId");
	}



	private void populateStateMultipleGetParams(){
		myStateMultipleGetParams = new ArrayList<String>();
		myStateMultipleGetParams.add("activityId");
		myStateMultipleGetParams.add("agent");
		myStateMultipleGetParams.add("registration");
		myStateMultipleGetParams.add("since");
	}



	private void populateStateMultipleDeleteParams(){
		myStateMultipleDeleteParams = new ArrayList<String>();
		myStateMultipleDeleteParams.add("activityId");
		myStateMultipleDeleteParams.add("agent");
		myStateMultipleDeleteParams.add("registration");
	}



	private void populateActivityProfileSingleParams(){
		myActivityProfileSingleParams = new ArrayList<String>();
		myActivityProfileSingleParams.add("activityId");
		myActivityProfileSingleParams.add("profileId");
	}



	private void populateActivityProfileSingleGetParams(){
		myActivityProfileSingleGetParams = new ArrayList<String>();
		myActivityProfileSingleGetParams.add("activityId");
		myActivityProfileSingleGetParams.add("profileId");
	}



	private void populateActivityProfileMultipleGetParams(){
		myActivityProfileMultipleGetPrams = new ArrayList<String>();
		myActivityProfileMultipleGetPrams.add("activityId");
		myActivityProfileMultipleGetPrams.add("since");
	}



	private void populateAgentProfileSingleParams(){
		myAgentProfileSingleParams = new ArrayList<String>();
		myAgentProfileSingleParams.add("agent");
		myAgentProfileSingleParams.add("profileId");
	}



	private void populateAgentProfileMultipleParams(){
		myAgentProfileMultipleParams = new ArrayList<String>();
		myAgentProfileMultipleParams.add("agent");
		myAgentProfileMultipleParams.add("since");
	}



	/**
	 * 
	 * Definition:
	 *	Populate the list of the accepted domains
	 *	for CORS
	 *
	 * Params:
	 *
	 *
	 */
	private void populateDomainList(String[] theList){
		myAllowedDomainList = new ArrayList<String>();
		for(String s : theList){
			myAllowedDomainList.add(s.trim());
		}
	}



	/**
	 * 
	 * Definition:
	 *	Fill the list of the header allowed
	 *
	 * Params:
	 *
	 *
	 */
	private void populateHeaderList(){
		myAllowedHeaderList = new ArrayList<String>();
		myAllowedHeaderList.add("Authorization");
		myAllowedHeaderList.add("content");
		myAllowedHeaderList.add("X-Experience-API-Version");
	}



	/**
	 * 
	 * Definition:
	 *	Function to maintaing the permission list from getting too big
	 *
	 * Params:
	 *
	 *
	 */
	private void maintainCredentialsList(){
		// Interval equals to ten minutes
		int theInterval = 3 * 60 * 1000;

		Timer theTimer = new Timer();

		theTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				DateTime theNow = DateTime.now();

				// Iterating through credentials maps
				Iterator<XapiKeySecret> theIterator = myAccessManager.getCredentialsKeySet().iterator();
				/**
				for(XapiKeySecret ks : myAccessManager.getCredentialsKeySet()){
					XapiCredentials cred = myAccessManager.getCredential(ks);

					if(cred.getExpiry().isBefore(theNow)){
						myAccessManager.removePermission(ks);
					}
				}
				**/
				while(theIterator.hasNext()){
					XapiKeySecret ks = theIterator.next();
					XapiCredentials cred = myAccessManager.getCredential(ks);

					if(cred.getExpiry().isBefore(theNow)){
						theIterator.remove();
						myAccessManager.removePermission(ks);
					}
				}
			}
		}, theInterval, theInterval);
	}



	private long defaultLog(Exception theException, HttpServletRequest req){
		return myLogger.log(theException, 500, theException.getMessage(), req);
	}



	/**
	 * 
	 * Definition:
	 *	Rollback the transaction
	 *
	 * Params:
	 *
	 *
	 */
	private void rollback(Connection conn, HttpServletResponse resp){
		//try{
		//	conn.rollback();
		//}catch(SQLException e){
		//	defaultLog(e);
		//	handleError(resp, 500, "Having trouble writing to the database");
		//}
	}
}