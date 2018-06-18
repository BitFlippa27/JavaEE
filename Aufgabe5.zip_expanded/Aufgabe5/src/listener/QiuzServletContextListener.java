package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import application.Quiz;
import loader.FilesystemLoader;
 
@WebListener
public class QiuzServletContextListener implements ServletContextListener {
 
    public void contextInitialized(ServletContextEvent servletContextEvent) {

    	FilesystemLoader xmlLoader = new FilesystemLoader("catalog");
        Quiz quiz = Quiz.getInstance();
		quiz.initCatalogLoader(xmlLoader);
    }
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
     
}