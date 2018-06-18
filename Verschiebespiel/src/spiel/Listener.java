package spiel;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

public class Listener implements ServletRequestListener
{


  public Listener() 
  {}

public void requestDestroyed(ServletRequestEvent arg0)
{
System.out.println("RequestListener:  Request zerstört");
	
}

public void requestInitialized(ServletRequestEvent arg0) 
{
	ServletRequest request = arg0.getServletRequest();
	String ziffer = request.getParameter("ziffer");
	System.out.println("RequestListener: request initialisiert");
	if ( ziffer != null ) 
		System.out.println("RequestListener: ziffer "+ ziffer);
	else
		System.out.println("RequestListener:  ziffer undefiniert");
	
} 

 
}