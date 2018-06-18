<% 
// Parameter auswerten
String login=request.getParameter("login");
String start=request.getParameter("start");

// 1. Request - Login-Formular anzeigen
if (login == null && start == null)
{
	out.println("<form>");
	out.println("<input name=\"login\" type=\"text\"/>");
	out.println("<input type=\"submit\" value=\"absenden\" />");
	out.println("</form>");

}


// Spieler meldet sich an
if (login != null && start== null)
	{    RequestDispatcher dispatcher= request.getRequestDispatcher("/Templates");
	     dispatcher.include(request,response);
	     System.out.println("login erkannt. Servlet gestartet");
		{ out.println("<form>");
		  if ( session.getAttribute("error") != null )  // Falls Fehler, dann in login-Feld anzeigen
			  {  String error=(String)session.getAttribute("error"); 
			     out.println("<input type=\"text\" value= \""+error+"\"/>");
			  }
		  else out.println("<input name=\"login\" type=\"text\" />");
		  	  
		  // mehr als 2 Spieler sind angemeldet. Jetzt Spiel starten möglich
		  if ( session.getAttribute("Spieler2") != null )  		   
	                  out.println("<input type=\"submit\" name=\"start\" value=\"start\" />");  
		  
		  out.println("<input type=\"submit\" value=\"absenden\" />");
		  out.println("</form>");
		}
	} 
//Start-Button wurde gedrückt --> Frage anzeigen
if (start!= null)
	{ 	out.println("<h3>Frage</h3>");
	    out.println("<ul>Antwort 1</ul>");
	    out.println("<ul>Antwort 2</ul>");
	    out.println("<ul>Antwort 3</ul>");
	    out.println("<ul>Antwort 4</ul>");
	}

%>