$(document).ready(function() {
	$("button").click(function(){
		var text = $("#statement").val(); 
		$.post('http://localhost:8080/XapiServlet/Trial', text, function( xml ){
			var errorCode = $(xml).find("Errorcode").text();
			var message = $(xml).find("Message").text();
			var url = "http://localhost:8080/XapiServlet/index.html?errorCode=" + errorCode +"&message=" + message;
			window.location.href = url;
			},
			//$.html().replacewith(data);}, 
			"xml");
	});
});