package spiel;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
@WebListener
public class SessionListener implements HttpSessionListener 
{


  public SessionListener() 
  {} 

  public void sessionCreated(HttpSessionEvent session) 
  {
    HttpSession Session = session.getSession();
    System.out.println("SessionListener: Session erzeugt");  
    
    // Session nach 1 Minuten zerstören
    Session.setMaxInactiveInterval(1000);
    System.out.println("SessionListener:  Sessiondauer= "+ Session.getMaxInactiveInterval());
    
    Logik spielelogik = new Logik();
	if ( spielelogik != null )
            Session.setAttribute("spielelogik", spielelogik);

    
  }

  public void sessionDestroyed(HttpSessionEvent session) 
  {

    HttpSession Session = session.getSession();
   
    String id = Session.getId();

    System.out.println("SessionListener: Session "+id+" deletet");
  }
}