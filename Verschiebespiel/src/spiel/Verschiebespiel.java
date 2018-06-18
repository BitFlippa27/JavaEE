package spiel;
import spiel.Logik;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@WebServlet("/Verschiebespiel")
/**
 * Servlet implementation class verschiebespiel
 */
// @WebServlet("/verschiebespiel")  --> URL wird in web.xml definiert
public class Verschiebespiel extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
    
	// keine �ffentliche oder private Daten im Servlet unsynchronisiert verwenden. 
	//private logik spielelogik;
	
    /**
     * Default constructor. 
     */
    public Verschiebespiel() 
    { 
    	// Instanziieren der Logik wird im SessionListener erledigt
    	System.out.println("Servlet: aufruf des Konstruktors");
    }  

    // Einsprung ins Servlet durch den servlet-Container um Initialisierungen vorzunehmen
    public void init(ServletConfig config) throws ServletException 
    {   
    	super.init(config);
        System.out.println("Servlet: Aufruf von init");
   } 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ "deprecation" })
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		RequestDispatcher dispatcher;
		
		System.out.println("Servlet:  Start doGet()");
		System.out.println("Servlet:  Thread-ID= "+Thread.currentThread().getId());
		System.out.println("Servlet:  Thread-Name= "+Thread.currentThread().getName());
		
		
		 // Session erzeugen oder benutzen
	    HttpSession session = request.getSession(true);
	    session.setMaxInactiveInterval(1000);
	    
	    // Session gibt es schon 
	    if ( session != null) 
	    {   
	    	Logik spielelogik = (Logik) session.getAttribute("spielelogik");
	        // aber logik noch nicht in der Session gespeichert
	        if ( spielelogik == null) 
	        { 
	        System.out.println("Servlet: es wird eine neue Spielelogik erzeugt");
	    	spielelogik = new Logik();
			session.setAttribute("spielelogik", spielelogik);  //Der Session wird die logik über das objekt spielelogik hinzugefügt.
	        }
	    }
		
		
		// Wenn eine neue Session erzeugt wurde muss eine neue Spiellogik erzeugt werden 
	    // Dafür ist der SessionListener zuständig, daher sind die folgenden Anweisungen auskommentiert
		/* 
		if ( session.isNew() )
		{   //spielelogik = new logik();
			//if ( spielelogik != null ) session.setAttribute("spielelogik", spielelogik);

    	} */

		
		String ziffer = request.getParameter("ziffer");  //request Parameter vom Client(Ziffer auf die er geklickt hat)
		if ( ziffer != null && !ziffer.equals(""))
		{
			Logik spielelogik  = (Logik) session.getAttribute("spielelogik"); //
			spielelogik.move(new Integer(ziffer));
			boolean erg = spielelogik.richtigeReihenfolge();
			
			// Zum Testen Gewonnen erzwingen bei Auswahl der ziffer 3
		     if (ziffer.equals("3"))
		    	 erg=true;
			
			if (erg)
			{ 
				// Auswahl der Seite Spiel gewonnen
			  dispatcher = request.getRequestDispatcher("EndeSpiel.html");
			   // Session zerst�ren
			  session.invalidate();
			}
			
			else
			{   // Auswahl der Spielseite
				dispatcher = request.getRequestDispatcher("verschiebespiel.jsp");
			}
		}
		else
		{	// Auswahl der Spielseite	
			dispatcher = request.getRequestDispatcher("verschiebespiel.jsp");
		}
	    // Weiterleiten auf ausgewählte Webseite
		dispatcher.forward(request, response);
	    System.out.println("Servlet: Ende doGet()");
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException //Behandelt alle Anfragen als GET-Requests
	{
		doGet(request, response);
	}

}
