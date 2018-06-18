package templates;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet(name = "Templates", urlPatterns = {"/Templates"})
public class Templates extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public void init(ServletConfig config)throws ServletException
    {
    	super.init(config);
        System.out.println("Servlet: Aufruf von init");
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		System.out.println("sdkjlfhsdjklfh");
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(5000);
		RequestDispatcher dispatcher = request.getRequestDispatcher("indexo.jsp");
		dispatcher.forward(request, response);
		
		String login=request.getParameter("login");
		String start=request.getParameter("start");
		
		// Spieler hat sich mit Name angemeldet
		if (login != null && !login.equals("") &&  start == null)
		{   //  1. Spieler ? 
			if ( session.getAttribute("Spieler1") == null ) 
				session.setAttribute("Spieler1", login);
			// weitere Spieler in Session eintragen
			else
			{   Enumeration<String> spielerliste= session.getAttributeNames();
				
				int zaehler; 
				// Anzahl der Spieler zaehlen
				for (zaehler=0; spielerliste.hasMoreElements(); zaehler++)
				{	
					spielerliste.nextElement();
				}
                System.out.println("Zähler: "+zaehler);
				if (zaehler<6) 
					session.setAttribute("Spieler"+(zaehler+1), login);
				else 
					session.setAttribute("error", "Spielerliste_ist_voll");
			}	
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
