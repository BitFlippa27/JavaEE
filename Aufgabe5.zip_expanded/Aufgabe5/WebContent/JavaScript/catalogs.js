var request;
var READY = 4;
var curCat = null;
function getCatalog(){
	request = new XMLHttpRequest();
	request.onreadystatechange = function() {
	    if (request.readyState == READY && request.status == 200) {
	        catalogListener();
	    }
	};
	request.open("POST", "CatalogList", true);
	request.send();
	console.log("Request gesendet");
}

function catalogListener(){
	var xmlDoc = request.responseXML;
	var count = 0;
	for(var i = 0; i < (xmlDoc.childNodes.length +1); i++)
	{
		var catalog = xmlDoc.getElementsByTagName("name")[count];
		var catValue = catalog.firstChild.nodeValue;
		console.log(catValue);
	    var div = document.createElement("div");
	    div.innerHTML = '<label class="cat" onclick="changeCatalog(this)">'+catValue+'</label>';
	    document.getElementById("cat").appendChild(div.childNodes[0]);
	    div.innerHTML = '<br></br>';
	    document.getElementById("cat").appendChild(div.childNodes[0]);
	    
	    count++;
	}
}

function changeCatalog(selectedCat){
	console.log("changeCatalog");
	console.log("Cliend ID" + clientid);
	if(clientid == 0 && gameStarted == false)
	{
		console.log("changeColor");
		
		if(curCat!=null)
		{
			curCat.style.backgroundColor = "#225579";
		}
		curCat = selectedCat;
		sendCurCatalog();
	}
}