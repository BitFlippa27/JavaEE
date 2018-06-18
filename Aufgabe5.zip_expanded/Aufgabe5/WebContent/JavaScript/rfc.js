var webSocket;

window.onload = function () {
	
	createWebsocket();
	getCatalog();
	
}

function sendLoginRequest(socket){
	webSocket = socket;
	var name =window.document.getElementById("uname").value;
	var LoginRequest= {
			"Type" : "1",
			"Length" : name.length,
			"Name" : name
	}
	webSocket.send(JSON.stringify(LoginRequest));
}

function sendQuestionRequest(){
	var QuestionRequest={
			"Type" : "8",
			"Length" : "0"
	}
	var questionRequest_ = JSON.stringify(QuestionRequest);
	webSocket.send(questionRequest_);
}










