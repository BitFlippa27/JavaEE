package jsp;
import java.io.IOException;
import java.io.PrintWriter;
import jsp.HelloWorld;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Hello")
/**
 * Ein einfaches  Servlet, das eine Webseite an den Client sendet
 */

public final class Hello extends HttpServlet {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Respond to a GET request for the content produced by
     * this servlet.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are producing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     * 
     */
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
      throws IOException, ServletException {

        response.setContentType("text/html");
        
        // Umleitung auf eine jsp-Seite
         RequestDispatcher dispatcher = request.getRequestDispatcher("hello.jsp");
         dispatcher.forward(request, response);
        
        
        
        // Wie hei�e ich?
        String ServletName = this.getServletName();
        //String reverse = new StringBuffer(ServletName).reverse().toString();
        //System.out.println(reverse);
        System.out.println(ServletName);
        // Welche Remote-Adresse?
        String remote = request.getRemoteAddr();
      
        // Writerobjet f�r die Erzeugung einer Ausgabe (response) besorgen
        PrintWriter writer = response.getWriter();  
        
        // HTML-Tags ausgeben
        writer.println("<html>");
        
        // head
        writer.println("<head>");
        writer.println("<title>Erstes Java Servlet</title>");
        writer.println("</head>");
        
        // body
        writer.println("<body>");
        // �berschrift
        writer.println("<h1>Beispiel f�r ein Servlet</h1>");
        // Ein Paragraf zur Ausgabe des Servlet-Namen
        writer.println("<p>Hier ist die Ausgabe des ersten Servlets mit Name: "+ServletName);
        // remote-Adresse ausgeben
        writer.println("<br/>Remote Adress="+remote);
        writer.println("</p>");
        
        // Einbinden der Ausgabe eines weiteren Servlets �ber dispatcher.include
        //RequestDispatcher dispatcher = request.getRequestDispatcher("HelloWorld");
        //dispatcher.include(request, response); // Hier sind wir schon im anderen Servlet
       
        // hier weitere Ausgaben
        writer.println("<h3> Diese Ausgabe erfolgt im Servlet: "+ServletName+"</h3>");
        
         writer.println("<p> Es werden zwei Parameter vom Client an den Server gesendet:  Vorname und Nachname </p>");
        //Parameter auswerten
        String vorname=request.getParameter("vorname");
        if (vorname == null) 
        {
            // Der parameter wurde nicht gesendet
            writer.println("Fehler: Kein Vorname erhalten");
        } else if ("".equals(vorname)) 
               {
               // Der Parameter wurde zwar gesendet hat aber keinen Wert
               writer.println("Fehler: Vorname ist leer");
               }
               else writer.println("vorname: "+vorname);
        
        String nachname=request.getParameter("nachname");
        if (nachname == null) 
        {
            // Der parameter wurde nicht gesendet
            writer.println("Fehler: Kein Nachname erhalten");
        } 
        else if ("".equals(nachname)) 
               {
                // Der Parameter wurde zwar gesendet hat aber keinen Wert
                writer.println("Fehler: Nachname ist leer");
               }
        		else  
        			writer.println("<br/>nachname: "+nachname);
        
        
        writer.println("</body>");
        writer.println("</html>");
        
        
    }
} 
