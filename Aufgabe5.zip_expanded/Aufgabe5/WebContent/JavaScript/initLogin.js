var socket;
var clientid = -1;
var numberOfPlayers = 0;
var gameStarted = false;
var curSelection = -2;
var queCell1;
var queCell2;
var queCell3;
var queCell4;
var firstRound = true;
var playerTable;
var questiondiv;
var scoreID = [];

 
 function createWebsocket()
 {  // Listener registrieren fÃ¼r Buttons
	var login= window.document.getElementById("Login_Button");   
	login.addEventListener("click",sendLogin,false); 

	//var url = "ws://" + document.location.host  + "/Echo";
	var url = 'ws://localhost:8080/Aufgabe5/Echo';
	
	socket=new WebSocket(url);
	

	socket.onopen=sendenMoeglich;
	socket.onclose=Closing;
	socket.onerror=ErrorHandler;
	socket.onmessage=empfange;
 }

 
// Funktion die beim klicken auf den Login Button ausgeführt wird
 function sendLogin(event)
 {  
 	sendLoginRequest(socket); 	
	
 } 
 
 function sendenMoeglich()
 { 
	 console.log("senden Moeglich");
 }
 function ErrorHandler(event)
 { 
	 alert("Fehler bei den Websockets "+event.data);
 }
 
 function Closing(event)
 { 
	 if(event.code != 1000)
	{
		 alert("Websockets closing "+event.code);
	}
	 else
	{
		 console.log("Websocket wird geschlossen");
	}
	 
 }
 
 function empfange(message)
 { 
	var msgServer = JSON.parse(message.data);
	
		//LoginResponse
		if(parseInt(msgServer.Type) == 2){

				clientid = msgServer.clientid;
				console.log("ID: " + clientid);
				if(clientid == 0)
				{
					window.document.getElementById("Start_Game").disabled = false;
					document.getElementById("Start_Game").addEventListener("click", startGame,false);
					var tmp = document.getElementById("Login_Button");
					tmp.parentNode.removeChild(tmp);
					tmp = document.getElementById("uname")
					tmp.parentNode.removeChild(tmp);
					tmp = document.getElementById("Username");
					tmp.parentNode.removeChild(tmp);
				}
				if(clientid != 0)
				{
					var logDiv = document.getElementById("LoginFeld");
					logDiv.parentNode.removeChild(logDiv);
					
					var hauptDiv = document.getElementById("Hauptansicht");
					var newDiv = document.createElement("warteScreen");
					newDiv.id = "waitScreen";
					
					var txt = document.createTextNode("Warten auf Spielstart.");
					
					newDiv.appendChild(txt);
					hauptDiv.appendChild(newDiv);
				
				}	
		}
		//CatalogChanged
		if(parseInt(msgServer.Type) == 5){

				var filename = msgServer.Message;
				for(var i = 0; i < document.getElementsByClassName("cat").length; i++)
				{
					var catalog = document.getElementsByClassName("cat")[i].textContent;
					console.log("catalogname: "+ catalog+ "filename: "+ filename);
					if(catalog==filename)
					{
						console.log("Gleicher Filename: "+filename);
						document.getElementsByClassName("cat")[i].style.backgroundColor = "#225579";
						
					}
					else
					{
						document.getElementsByClassName("cat")[i].style.backgroundColor = "#102839";
					}
				}
		}
		//PlayerList
		if(parseInt(msgServer.Type)== 6){ 
				
				numberOfPlayers = msgServer.Lenght / 37;
				console.log("Anzahl Spieler: "+numberOfPlayers);
				
				pTable = document.getElementById("Player_Score_Table");
				pTable.parentElement.removeChild(pTable);
			
				pTable = document.createElement("table");
				pTable.id = "Player_Score_Table";
				
				scoreID = [];
				
				var player_Score = document.getElementById("Player_Score");
				player_Score.appendChild(pTable);	
				for(var i = 0; i < numberOfPlayers; i++) {			
					// Create an empty <tr> element and add it to the 2nd position of the table:
					var row = pTable.insertRow(0);
	
					// Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
					var cell1 = row.insertCell(0);
					var cell2 = row.insertCell(1);
					
					// Add text to the new cells:
					cell1.innerHTML = msgServer.Players[i].Spielername;
					cell2.innerHTML = msgServer.Players[i].Punktestand;
					
					scoreID.push(msgServer.Players[i].clientid);
					
					if(msgServer.Players[i].clientid == clientid)
					{
						cell1.style.backgroundColor = "#225579";
						cell2.style.backgroundColor = "#225579";
					}
					console.log("Erzeuge Spieler: "+msgServer.Players[i].Spielername);
				}
		}
		//StartGame
		if(parseInt(msgServer.Type) == 7){
				sendQuestionRequest();
		}
				
		//Question
		if(parseInt(msgServer.Type) == 9){
			
			if(msgServer.Length == 769)
			{
				if(firstRound == true) {
					if(clientid == 0) {
						var login = document.getElementById("LoginFeld");
						login.parentElement.removeChild(login);
					}
					else {
						var warteScreen = document.getElementById("waitScreen");
						warteScreen.parentElement.removeChild(warteScreen);
					}
				}
				firstRound = false;
				//Neuen div für die Fragen anlegen und eine ID vergeben
				var questiondiv = document.createElement("questions");
				
				questiondiv.id = "questionwindow";
				
				if(document.getElementById("questionTable")!=null) {
					var table = document.getElementById("questionTable");
					table.parentElement.removeChild(table);
				}
				
				//Neue Tabelle anlegen und mit Reihen, Zellen und Werten füllen
				var queTable = document.createElement("table");
				queTable.id = "questionTable";
				var queRow0 = queTable.insertRow(0);
				var queRow1 = queTable.insertRow(1);
				var queRow2 = queTable.insertRow(2);
				
				var queCell0 = queRow0.insertCell(0);
				queCell0.setAttribute("colspan",2);

				queCell1 = queRow1.insertCell(0);
				queCell2 = queRow1.insertCell(1);
				queCell3 = queRow2.insertCell(0);
				queCell4 = queRow2.insertCell(1);
				
				queCell0.innerHTML = msgServer.Frage;
				
				queCell1.innerHTML = "A: "+msgServer.Answer[0];
				queCell1.id = "0";
				queCell1.addEventListener("mouseover", mouseOverListener);
				queCell1.addEventListener("mouseout", mouseOutListener);
				queCell1.addEventListener("click", mouseClickListener);
				
				queCell2.innerHTML = "B: "+msgServer.Answer[1];
				queCell2.id = "1";
				queCell2.addEventListener("mouseover", mouseOverListener);
				queCell2.addEventListener("mouseout", mouseOutListener);
				queCell2.addEventListener("click", mouseClickListener);
				
				queCell3.innerHTML = "C: "+msgServer.Answer[2];
				queCell3.id = "2";
				queCell3.addEventListener("mouseover", mouseOverListener);
				queCell3.addEventListener("mouseout", mouseOutListener);
				queCell3.addEventListener("click", mouseClickListener);
				
				queCell4.innerHTML = "D: "+msgServer.Answer[3];
				queCell4.id = "3";
				queCell4.addEventListener("mouseover", mouseOverListener);
				queCell4.addEventListener("mouseout", mouseOutListener);
				queCell4.addEventListener("click", mouseClickListener);
				
				questiondiv.appendChild(queTable);
				
				var position = document.getElementById("Hauptansicht");
				position.appendChild(questiondiv);
			}
			// Keine Frage mehr vorhanden!
			else {
				var endDiv = document.createElement("endDiv");
				var mainDiv = document.getElementById("Login");
				endDiv.id = "endDiv";
				var queTable = document.getElementById("questionTable");
				queTable.parentElement.removeChild(queTable);
				
				var t = document.createTextNode("Alle Fragen beantwortet!");

				endDiv.appendChild(t);
				mainDiv.appendChild(endDiv);
			}
		}
		//QuestionResult
		if(parseInt(msgServer.Type) == 11){
			var correctAnswer = msgServer.Correct;
			if(correctAnswer != -1) {
				var correctCell = document.getElementById(correctAnswer);
				correctCell.style.background = "green";
				if(curSelection != correctAnswer) {
					var falseCell = document.getElementById(curSelection);
					correctCell.style.background = "red";
				}
				
				// Entfernen der Listener
				queCell1.removeEventListener("mouseover", mouseOverListener);
				queCell1.removeEventListener("mouseout", mouseOutListener);
				queCell1.removeEventListener("click", mouseClickListener);
				
				queCell2.removeEventListener("mouseover", mouseOverListener);
				queCell2.removeEventListener("mouseout", mouseOutListener);
				queCell2.removeEventListener("click", mouseClickListener);
				
				queCell3.removeEventListener("mouseover", mouseOverListener);
				queCell3.removeEventListener("mouseout", mouseOutListener);
				queCell3.removeEventListener("click", mouseClickListener);
				
				queCell4.removeEventListener("mouseover", mouseOverListener);
				queCell4.removeEventListener("mouseout", mouseOutListener);
				queCell4.removeEventListener("click", mouseClickListener);
			}
			else {
				alert("Zeit abgelaufen!");
				var questionRequest = {
						"Type": "8",
						"Length" : "0"
					};
				socket.send(JSON.stringify(questionRequest));
			}
			
			// 3 Sekunden warten -> die Hintergrundfarbe auf Standard
			setTimeout(function() {
				for(var i = 0; i < 4; i++) {
					var answers = document.getElementById(i);
					answers.style.backgroundColor = "white";
				}
				// eine neue Frage holen
				var questionRequest = {
						"Type": "8",
						"Length" : "0"
					};
				socket.send(JSON.stringify(questionRequest));
			}, 3000);
		}
		
		//GameOver
		if(parseInt(msgServer.Type) == 12){
			for(var i = 0; i < scoreID.length; i ++)
			{
				if(clientid == scoreID[i])
					{
						var tmp = -1;
						tmp = scoreID.length-i;
						alert("Sie sind " + tmp +". Platz geworden!");
						socket.close();
						location.reload();
						break;
					}
			}
			var main = document.getElementById("Login");
			main.parentElement.removeChild(main);
		}
		//ErrorWarning
		if(parseInt(msgServer.Type) == 255){
			
				console.log("Message: "+ msgServer.Message);
				var errorText = msgServer.Message;
				alert(errorText);
				socket.close();
				location.reload();
				
		}
	
} 
 
 function startGame() 
 {
	if(numberOfPlayers <= 1 && clientid == 0) {
		alert("Warten auf andere Spieler");
	}
	else
	{
		if(curCat != null) {
			console.log("spiel wird gestartet");
			var startGame = { 
					"Type" : "7",
					"Length" : curCat.textContent.length,
					"Message" : curCat.textContent
			}
			socket.send(JSON.stringify(startGame));
			gameStarted = true;
		}
		else 
		{
			alert("Kein Katalog ausgewählt!");
		}
	}
}
 
//Antwortenlistener
function mouseClickListener(event) {
 	event.target.style.background = "#225579";
	var questionAnswered = {
			"Type": "10",
			"Length" : "1",
			"Selection" : event.target.id
	};
	curSelection = event.target.id;

	console.log("Sende QuestionAnswered");
 	socket.send(JSON.stringify(questionAnswered));
}
 
function mouseOverListener(event) {
		event.target.style.background = "#225579";
}

function mouseOutListener(event) {
	event.target.style.background = "#102839";
}

function sendCurCatalog() {
	console.log("Length: "+curCat.textContent);
	var changedCatalog = { 
		"Type" : "5",
		"Length" : curCat.textContent.length,
		"Message" : curCat.textContent
	}
	socket.send(JSON.stringify(changedCatalog));
	console.log("aktueller Katalognachricht versendet!");
}