/**
\ * @author Rheza Hidajat
 */
$(document).ready(function() {
	var base = 'your-app-url-here';
	
	// authorization header, format : 'login:password' encoded with base64
	var auth = "Basic " + "your-server-login-and-password-here";
	
	var myLogin, myPassword;
	
	
	// This is for agent form
	var idType = $("#idType").val();
	$("#idType").change(function(){
		idType = $("#idType").val();
		if(idType == "account"){
			$("#label2").show();
			$("#id2").show();
			$("#label1").html("homepage: ");
		}
		if(idType == "mbox"){
			$("#label2").hide();
			$("#id2").hide();
			$("#label1").html("mbox: ");
		}
	});
	
	
	
	// Agent form, LMSStuff
	var idType = $("#idTypeCred").val();
	$("#idTypeCred").change(function(){
		idType = $("#idTypeCred").val();
		if(idType == "account"){
			$("#label2Cred").show();
			$("#id2Cred").show();
			$("#label1Cred").html("homePage: ");
		}
		if(idType == "mbox"){
			$("#label2Cred").hide();
			$("#id2Cred").hide();
			$("#label1Cred").html("mbox: ");
		}
	});
	
	
	
	// Agent Form for State
	$("#StateIdType").change(function(){
		var idType = $("#StateIdType").val();
		if(idType == "account"){
			$("#StateNameLabel").show();
			$("#StateAccountName").show();
			$("#StateMboxLabel").html("homePage: ");
		}
		if(idType == "mbox"){
			$("#StateNameLabel").hide();
			$("#StateAccountName").hide();
			$("#StateMboxLabel").html("mbox: ");
		}
	});
	
	
	
	// Agent Profile
	$("#AgentProfileIdType").change(function(){
		var idType = $("#AgentProfileIdType").val();
		if(idType == "account"){
			$("#APNameLabel").show();
			$("#APAccountName").show();
			$("#APMboxLabel").html("homePage: ");
		}
		if(idType == "mbox"){
			$("#APNameLabel").hide();
			$("#APAccountName").hide();
			$("#APMboxLabel").html("mbox: ");
		}
	});
	
	
	function handleError(jqXHR, textStatus, errorThrown){
		alert(jqXHR.status + "\n" + jqXHR.responseText);
	}
	
	function handleSuccess(data, textStatus, jqXHR){
		
		//console.log(jqXHR.status);
		
		var responseString = "Success!\n";
		//responseString += jqXHR.status + "\n";
		
		// console.log(textStatus);
		
		var obj = eval(data);
		console.log(obj);
		
		if(typeof data == 'object'){
			var response = eval(data);
			responseString += JSON.stringify(response);
			//alert(JSON.stringify(response));
		}else{
			responseString += data;
		}
		
		alert(responseString);
	}
	
	function createAuthorization(){
		var auth = myLogin + ":" + myPassword;
		var result = "Basic " + btoa(auth);
		console.log(result);
		return result;
	}
	
	
	// This is to request credentials for clients
	$("#LMSButton").click(function(event) {
		var scope = [];
		$("#Scope input:checkbox:checked").each(function() {
			scope.push($(this).val());
		});
	
		for(var i = 0; i < scope.length; i++){
			console.log(scope[i]);
		}
		
		var activity = [];
		
		var idType = $("#idTypeCred").val();
		var actor = new Object();
		actor.name = [$("#CredentialName").val()];
		if(idType == "mbox"){
			actor.mbox = [$("#id1Cred").val()];
		}else if(idType == "account"){
			var account = new Object();
			account.name = $("#id2Cred").val();
			account.homePage = $("#id1Cred").val();
			actor.account = [account];
		}
		
		var request = new Object;
		request.scope = scope;
		request.actors = actor;
		
		if($("#activityID").val().length > 0){
			activity.push($("#activityID").val());
			
		}
		if($("#activityID2").val().length > 0){
			activity.push($("#activityID2").val());
		}
		if($("#activityID3").val().length > 0){
			activity.push($("#activityID3").val());
		}
		
		if(activity.length > 0){
			request.activity = activity;
		}
		
		if($("#expiry").val().length > 0){
			request.expiry = $("#expiry").val();
		}
		
		if($("#registration").val().length > 0){
			request.registration = $("#registration").val();
		}
		
		console.log(JSON.stringify(request)); 
		
		$.ajax({
			type: "PUT",
			url: base + "/basic/request",
			headers: { 
				Accept: "application/json, */*; q=0.01",
				Authorization : auth
				},
			data: JSON.stringify(request),
			dataType : "json",
			success : function(data){
				myLogin = data.login;
				myPassword = data.password;
				handleSuccess(data);
			},
			error : handleError
		
		});
	}); // End of LMS Button click
	
	
	
	$('#DebugOnButton').click(function (event){
		
		$.ajax({
			type: "POST",
			url: base + "/basic/debug",
			headers: {
				Accept: "application/json, */*; q=0.01",
				Content: "application/json",
				Authorization : auth
			},
			data: {
				debugMode : "on"
			}
		});
	});
	
	
	
	$('#DebugOffButton').click(function (event){
		
		$.ajax({
			type: "POST",
			url: base + "/basic/debug",
			headers: {
				Accept: "application/json, */*; q=0.01",
				Content: "application/json",
				Authorization : auth
			},
			data: {
				debugMode : "off"
			}
		});
	});
	
	
	
	
	function getStateParams(){
		var request = new Object;
		
		
		if($("#StateActivityID").val().length > 0){
			request.activityId = $("#StateActivityID").val();
		}
		
		if($("#StateRegistration").val().length > 0){
			request.registration = $("#StateRegistration").val();
		}
		
		if($("#StateID").val().length > 0){
			request.stateId = $("#StateID").val();
		}
		
		var idType = $("#StateIdType").val();
		var actor = new Object();
		if($("#StateAgentName").val().length > 0){
			actor.name = $("#StateAgentName").val();
		}
		
		if(idType == "mbox"){
			if($("#StateMbox").val().length > 0){
				actor.mbox = $("#StateMbox").val();
			}
		}else if(idType == "account"){
			var account = new Object();
			if(checkValue("#StateMbox")){
				account.name = $("#StateMbox").val();
			}
			if(checkValue("$StateAccountName")){
				account.homePage = $("#StateAccountName").val();
			}
			if(!($.isEmptyObject(actor))){
				actor.account = account;
			}
		}
		
		if(!($.isEmptyObject(actor))){
			request.agent = JSON.stringify(actor);
		}
		
		request.result = $("#StateTextarea").val();

//		console.log(request);
		
		return request;
	}
	
	function getStateParamsString(){
		var paramsString = "?";
		
		if($("#StateActivityID").val().length > 0){
			paramsString += "activityId=" + $("#StateActivityID").val() + "&";
		}
		
		if($("#StateRegistration").val().length > 0){
			paramsString += "registration=" + $("#StateRegistration").val() + "&";
		}
		
		if($("#StateID").val().length > 0){
			paramsString += "stateId=" + $("#StateID").val() + "&";
		}
		
		var idType = $("#StateIdType").val();
		var actor = new Object();
		if($("#StateAgentName").val().length > 0){
			actor.name = $("#StateAgentName").val();
		}
		
		if(idType == "mbox"){
			if($("#StateMbox").val().length > 0){
				actor.mbox = $("#StateMbox").val();
			}
		}else if(idType == "account"){
			var account = new Object();
			if(checkValue("#StateMbox")){
				account.name = $("#StateMbox").val();
			}
			if(checkValue("$StateAccountName")){
				account.homePage = $("#StateAccountName").val();
			}
			if(!($.isEmptyObject(account))){
				actor.account = account;
			}
		}
		
		if(!($.isEmptyObject(actor))){
			paramsString += "agent=" + JSON.stringify(actor);
		}

		return paramsString;
	}
	
	function checkValue(identifier){
		return ($(identifier).val().length() > 0);
	}
	
	function getValue(identifier){
		return ($(identifier).val().length() > 0);
	}
	
	// To post a state
	$("#PostStateButton").click(function(event) {

		$.ajax({
			type: "POST",
			url: base + "/activities/state" + getStateParamsString() ,
			headers: { 
				Authorization: createAuthorization(),
				Accept: "application/json, */*; q=0.01",
				"X-Experience-API-Version": "1.0.1",
				"content": "application/json"
				},
			data: getStateParams().result,
			success : handleSuccess,
			error : handleError
		});
	}); // End of click of poststatebutton
	
	
	
	// To get a state
	$("#GetStateButton").click(function(event) {
		console.log(myLogin + ":" + myPassword)
		
		var requestData = getStateParams();
		delete requestData.re
		
		$.ajax({
			type: "GET",
			url: base + "/activities/state",
			headers: {
				Authorization: createAuthorization(),
			},
			data: getStateParams(),
			success: handleSuccess,
			error: handleError,
		});
	}); // End of click of GetStateButton
	
	
	
	function getActivityProfileParams(){
		var request = new Object;
		
		if($("#ActvProfTextarea").val().length > 0){
			request.document = $("#ActvProfTextarea").val();
		}
		if($("#ActvProfActivityID").val().length > 0){
			request.activityId = $("#ActvProfActivityID").val();
		}
		if($("#ProfileID").val().length > 0){
			request.profileId = $("#ProfileID").val();
		}
		if($("#ActvProfSince").val().length > 0){
			request.since = $("#ActvProfSince").val();
		}
		
		console.log(request);
		
		return request;
	}
	
	function getActivityProfileParamsString(){
		var paramString = "?";
		
		if($("#ActvProfActivityID").val().length > 0){
			paramString += "activityId=" +  $("#ActvProfActivityID").val() +"&";
		}
		if($("#ProfileID").val().length > 0){
			paramString += "profileId=" +  $("#ProfileID").val() +"&";
		}
		if($("#ActvProfSince").val().length > 0){
			paramString += "since=" + $("#ActvProfSince").val();
		}
		
		console.log(paramString);
		
		return paramString;
	}
	
	$("#PostActivityProfile").click(function(event) {
		
		var params = getActivityProfileParams();
		
		var paramsString = getActivityProfileParamsString();		
		
		$.ajax({
			type: "POST",
			url: base + "/activities/profile" + paramsString ,
			headers: { 
				Authorization: createAuthorization(),
				Accept: "application/json, */*; q=0.01",
				Content: "application/json"
				},
			data: params.document,
			success : handleSuccess,
			error: handleError
		});
	});
	
	$("#GetActivityProfile").click(function(event) {
		
		var params = getActivityProfileParams();
		
		var paramsString = getActivityProfileParamsString();
		
		$.ajax({
			type: "GET",
			url: base + "/activities/profile" + paramsString,
			headers: { 
				Authorization: createAuthorization(),
				Accept: "application/json, */*; q=0.01",
				Content: "application/json"
				},
			data: params.document,
			success : handleSuccess,
			error: handleError,
		});
	});
	
	$("#PutActivityProfile").click(function(event) {
		
		var params = getActivityProfileParams();
		
		var paramsString = getActivityProfileParamsString();		
		
		$.ajax({
			type: "PUT",
			url: base + "/activities/profile" + paramsString ,
			headers: { 
				Authorization: createAuthorization(),
				Accept: "application/json, */*; q=0.01",
				Content: "application/json"
				},
			data: params.document,
			success : handleSuccess,
			error: handleError
		});
	});
	

	// Activity Profile, getting a statemetn
	$("#GetActivity").click(function(event) {
		var activityID = $("#ActvProfActivityID").val();
		
		$.ajax({
			type: "GET",
			url: base + "/activities",
			headers: { 
				Authorization: createAuthorization(),
				Accept: "application/json, */*; q=0.01",
				Content: "application/json"
				},
			data: {
				activityId : activityID
			},
			error : handleError,
			success : handleSuccess
		});
	});
	
	
	
	function getAgentProfileParamsString(){
		var paramString = "?";
		
		if($("#APProfileId").val().length > 0){
			paramString += "profileId=" +  $("#APProfileId").val() +"&";
		}
		
		var idType = $("#AgentProfileIdType").val();
		var actor = new Object();
		if($("#APAgentName").val().length > 0){
			actor.name = $("#APAgentName").val();
		}
		
		if(idType == "mbox"){
			if($("#APMbox").val().length > 0){
				actor.mbox = $("#APMbox").val();
			}
		}else if(idType == "account"){
			var account = new Object();
			if($("#APMbox").val().length > 0){
				account.name = $("#APMbox").val();
			}
			if($("#APAccountName").val().length > 0){
				account.homePage = $("#APAccountName").val();
			}
			
			if(!($.isEmptyObject(account))){
				actor.account = account;
			}
		}
		
		if(!($.isEmptyObject(actor))){
			paramString += "agent=" + JSON.stringify(actor) + "&";
		}
		
		if($("#APSince").val().length > 0){
			paramString += "since=" + $("#APSince").val();
		}
		
		console.log(paramString);
		
		return paramString;
	}
	
	
	
	$("#PostAPButton").click(function(event) {
		var text = $("#APTextarea").val();
		
		$.ajax({
			type: "POST",
			url: base + "/agents/profile" +	getAgentProfileParamsString(),
			headers: { 
				Authorization: createAuthorization(),
				Accept: "application/json, */*; q=0.01",
				Content: "application/json"
				},
			data: text,
			success: handleSuccess,
			error: handleError
		});
	});
	
	
	
	$("#GetAPButton").click(function(event) {
		$.ajax({
			type: "GET",
			url: base + "/agents/profile" +	getAgentProfileParamsString(),
			headers: { 
				Authorization: createAuthorization(),
				Accept: "application/json, */*; q=0.01",
				Content: "application/json"
				},
			success: handleSuccess,
			error : handleError
		});
	});
	
	
	
	$("#PutAPButton").click(function(event) {
		var text = $("#APTextarea").val();
		
		$.ajax({
			type: "PUT",
			url: base + "/agents/profile" +	getAgentProfileParamsString(),
			headers: { 
				Authorization: createAuthorization(),
				Accept: "application/json, */*; q=0.01",
				Content: "application/json"
				},
			data: text,
			success: handleSuccess,
			error: handleError
		});
	});
	
	
	function log(data, textStatus, jqXHR){
		var t = new Date();
		console.log(t.toTimeString() + " : " + data);
	}
	
	
	// To post a statement
	$("#PostStatementButton").click(function(event) {
		var text = $("#StatementTextarea").val(); 
		
		//for(i = 0; i < 20; i++){
			$.ajax({
				type: "POST",
				url: base + "/statements",
				headers: {
					Authorization: createAuthorization(),
				},
				data: text,
				success: log,
				error : handleError
			});
		//}
	}); // End of click post statement button
	
	
	
	// To post a statement
	$("#PutStatementButton").click(function(event) {
		var text = $("#StatementTextarea").val(); 
		
		var theStatement = JSON.parse(text);
		
		console.log(theStatement.id);
		
		//for(i = 0; i < 20; i++){
			$.ajax({
				type: "PUT",
				url: base + "/statements" + "?statementId=" + theStatement.id,
				headers: {
					Authorization: createAuthorization(),
				},
				data: text,
				success: log,
				error : handleError
			});
		//}
	}); // End of click post statement button
	
	
	
	$("#TryIt").click(function(event) {
		$.ajax({
			type: "PUT",
			url: base + "/activities/state" + "?" + "activityId=youknow&stateId=youknow2",
			headers: {
				Authorization: myLogin + ":" + myPassword,
			},
			data: "blablablbla",
			success: function(data){
				console.log(data);
			}
		});
	});
	
	
	
	// To post a state
	$("#PutStateButton").click(function(event) {

		$.ajax({
			type: "PUT",
			url: base + "/activities/state" + getStateParamsString() ,
			headers: { 
				Authorization: createAuthorization(),
				Accept: "application/json, */*; q=0.01",
				Content: "application/json"
				},
			data: getStateParams().result,
			success : handleSuccess,
			error : handleError
		});
	}); // End of click of poststatebutton
	
	
	
	
	$("#GetStatementButton").click(function(event) {
		getStatementsParams();
		
		$.ajax({
			type: "GET",
			url: base + "/statements",
			headers: { 
				Authorization: createAuthorization(),
				Accept: "application/json, */*; q=0.01",
				Content: "application/json"
				},
			data: getStatementsParams(),
			success : handleSuccess,
			error : handleError
		});
	});
	
	
	
	function getStatementsParams(){
		var request = new Object;
		
		if($("#StatementId").val().length > 0){
			request.statementId = $("#StatementId").val();
		}
		
		if($("#VerbName").val().length > 0){
			request.verb = $("#VerbName").val();
		}
		
		if($("#VoidedStatementId").val().length > 0){
			request.voidedStatementId = $("#VoidedStatementId").val();
		}
		
		if($("#activityId").val().length > 0){
			request.activity = $("#activityId").val();
		}
		
		if($("#since").val().length > 0){
			request.since = $("#since").val();
		}
		
		if($("#until").val().length > 0){
			request.until = $("#until").val();
		}
		
		var idType = $("#idType").val();
		var actor = new Object();
		if($("#agentName").val().length > 0){
			actor.name = $("#agentName").val();
		} 
		
		if(idType == "mbox"){
			if($("#id1").val().length > 0){
				actor.mbox = $("#id1").val();
			}
		}else if(idType == "account"){
			var account = new Object();
			if($("#id1").val().length > 0){
				account.homePage = $("#id1").val();
			}
			if($("#id2").val().length > 0){
				account.name = $("#id2").val();
			}
			if(!$.isEmptyObject(account)){
				actor.account = account;
			}
		}
		actor.objectType="Agent";
		
		if(!$.isEmptyObject(actor)){
			request.agent = JSON.stringify(actor);
		}

		console.log(request);
		
		return request;
	}
	
	
	$("#IE8").click(function(event){
		var actor = new Object();
		actor.name = "rheza hidajat";
		
		var account = new Object();
		account.homePage = "https://excellence.marriott.com";
		account.name = "0fb9b11828318782";
		actor.account = account;
		
		var headers = new Object();
		//headers.suck = "sucker";
		headers.agent = JSON.stringify(actor); 
		headers.profileId = "CMI5LearnerPreferences";
		
		var data = new Object();
		//data.documen
		
		//var method = "POST";
		var method = "GET";
		//var method = "PUT";
		
		//var path = "/activities/state";
		//var path = "/statements";
		//var path = "/basic/request";
		//var path = "/activities/profile";
		var path = "/agents/profile";
		
		var result = getIEModeRequest(method, base + path, headers, JSON.stringify(data));

		console.log(JSON.stringify(result));
		console.log(JSON.stringify(result.data));
		//result.content = JSON.stringify(result.content);
		$.ajax(result);
		
	});
	
	
	function getIEModeRequest(method, url, headers, data){

	    var newUrl = url;

	    // Everything that was on query string goes into form vars
	    var formData = new Array();
	    var qsIndex = newUrl.indexOf('?');
	    if(qsIndex > 0){
	        formData.push(newUrl.substr(qsIndex+1));
	        newUrl = newUrl.substr(0, qsIndex);
	    }

	    // Method has to go on querystring, and nothing else
	    newUrl = newUrl + '?method=' + method;

	    // Headers
	    if(headers !== null){
	        for(var headerName in headers){
	            formData.push(
	                headerName + "=" +
	                    encodeURIComponent(
	                        headers[headerName]));
	        }
	    }

	    // The original data is repackaged as "content" form var
	    if(data !== null){
	        formData.push('content=' + encodeURIComponent(data));
	    }

	    return {
	        "method":"POST",
	        "url":newUrl,
	        "headers":{},
	        "data":formData.join("&")
	    };
	}
});
