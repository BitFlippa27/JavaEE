package temp;

import java.io.IOException;
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
 * Servlet implementation class BeispielTemplate
 */
@WebServlet("/BeispielTemplate")
public class BeispielTemplate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public void init(ServletConfig config)throws ServletException
    {
    	super.init(config);
        System.out.println("Servlet: Aufruf von init");
    }


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher dispatcher;
		dispatcher = request.getRequestDispatcher("INDEX.jsp");
		dispatcher.forward(request, response);
		
		// TODO Auto-generated method stub
		System.out.println("sdkjlfhsdjklfh");
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(5000);
		
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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
