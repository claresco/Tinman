$(document).ready(function() {
	var url = document.URL.split('?')[1];
	var params = url.split('&');
	$("#errorCode").html("Error code: " + params[0].split("=")[1]);
	$("#message").html(decodeURIComponent(params[1].split("=")[1]));
});