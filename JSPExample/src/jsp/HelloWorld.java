package jsp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/HelloWorld") 

public class HelloWorld  extends HttpServlet
{
     // Dieses Servlet erzeugt kein korrektes html-Dokument. es fehlen html-tag und body-Tag
	// Das Servlet wird in die Ausgabe eines anderen Servlets eingebaut

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request,HttpServletResponse response)
              throws IOException, ServletException 
     {
		
		System.out.println("Servlet angesprungen");
		response.setContentType("text/html");     
		
		String ServletName = this.getServletName();
	
		 		
		PrintWriter writer = response.getWriter();  
		


		// Mit writer-Objekt htmls-Ausgabe erzeugen
		writer.println("<hr/>");
		writer.println("<h3>Dieser Text aus dem Servlet "+ServletName+" wird eingef�gt</h3>");
		writer.println("<p>");


		//empfangene Parameter auswerten
		
		// Parameter Vorname
		String vorname=request.getParameter("vorname");
		if (vorname == null)   // der Parameter wurde nicht gesendet
		{
			writer.println("Fehler: Kein Vornamne erhalten");
		} else if ("".equals(vorname))   // Parameter erhalten aber leerer String
      			{
					writer.println("Fehler: Vorname ist leer");
      			}
      			else writer.println("vorname: "+vorname);  // Parameter korrekt erhalten und in Webseite einbinden

		// analog mit Nachname verfahren
		String nachname=request.getParameter("nachname");
		if (nachname == null) 
		{
			writer.println("Fehler: Kein Nachname erhalten");
		} else if ("".equals(nachname)) 
			{
				writer.println("Fehler: Nachname ist leer");
			} else   writer.println("<br/>nachname: "+nachname+"<br/>");
		writer.println("</p>");
		writer.println("<hr/>");
}
	
}
