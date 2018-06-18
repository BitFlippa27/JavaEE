import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet(name = "SimpleServlet", urlPatterns = {"/SimpleServlet"})
public class simpleServlet extends HttpServlet
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public void service( HttpServletRequest request, HttpServletResponse response )
    throws ServletException, IOException
  {

	for(int count = 0 ;count < 10; count++)
	{
		System.out.println("Spieler"+count);
	}
    response.setContentType( "text/html" );
    PrintWriter out = response.getWriter();
    out.println( "<hr/><center>Copyright &copy 2008</center>" );
  }
}