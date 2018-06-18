package loader;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import application.Catalog;
import application.Question;

public class FilesystemLoader implements CatalogLoader {

    /**
    * RegEx to capture the question block.
    * <p>
    * Captures three groups:
    * <p>
    *  1. Group: Contains the question<br>
    *  2. Group (optional): Timeout<br>
    *  3. Group: Answer block (all possible answers)<br>
    */
    private static final String QUESTION_BLOCK_REGEX =
        "(.+)\n(?:TIMEOUT: ([0-9]+)\n)??((?:[+-] .+\n){4}?)";
    /**
     * RegEx captures the individual answers in the captured answer block
     * from the more general expression above.
     * <p>
     * There are two capture groups:
     * <p>
     *  1. Group: +/-, which states if the answer is true or false<br>
     *  2. Group: Contains the answer<br>
     */
    private static final String ANSWER_REGEX = "([+-]) (.+)\n";

    private final Pattern blockPattern = Pattern.compile(QUESTION_BLOCK_REGEX);
    private final Pattern questionPattern = Pattern.compile(ANSWER_REGEX);

    private File[] catalogDir;
    private final Map<String, Catalog> catalogs =
        new HashMap<String, Catalog>();
    private final String location;
    private String directory;
    private File dir;
    private int temp = 0;

    public FilesystemLoader(String location) {
        this.location = location;
        }

    @Override
    public Map<String, Catalog> getCatalogs() throws LoaderException {

    	if (!catalogs.isEmpty()) {
        	return catalogs;
        }
        

        // Construct URL for package location
        //URL url = this.getClass().getClassLoader().getResource(location);
        //URL url = this.getClass().getResource(location);
        dir = null;
        //dir = new File(getClass().getClassLoader().getResource(location).toURI());
		dir = new File(System.getProperty("user.dir") + "/src/catalogs");
        System.out.println(dir.getPath());

        // Add catalog files
        if (dir.exists() && dir.isDirectory()) {
            this.catalogDir = dir.listFiles(new CatalogFilter());
            for (File f : catalogDir) {
            	System.out.println(f.getName());
                catalogs.put(f.getName(),
                    new Catalog(f.getName(), new QuestionFileLoader(f)));
            }
        }
        
        return catalogs;
    }

    @Override
    public Catalog getCatalogByName(String name) throws LoaderException {
        if (catalogs.isEmpty()) {
            getCatalogs();
        }

        return this.catalogs.get(name);
    }

    /**
     * Filter class for selecting only files with a .cat extension.
     *
     * @author Simon Westphahl
     *
     */
    private class CatalogFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            if (pathname.isFile() && pathname.getName().endsWith(".xml"))
                return true;
            else
                return false;
        }
    }

    private class QuestionFileLoader implements QuestionLoader {

    	 private final File catalogFile;
         private List <Question> questions = new ArrayList<Question>();

         public QuestionFileLoader(File file) {
             catalogFile = file;
         }
         
 		@Override
         public List<Question> getQuestions(Catalog catalog)
             throws LoaderException {

             if (!questions.isEmpty()) {
                 return questions;
             }
             
             DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         	DocumentBuilder dBuilder = null;
 			try {
 				dBuilder = dbFactory.newDocumentBuilder();
 			} catch (ParserConfigurationException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
         	org.w3c.dom.Document doc = null;
 			try {
 				doc = dBuilder.parse(catalogFile);
 			} catch (SAXException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			} catch (IOException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
         			
         	//optional, but recommended
         	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
         	doc.getDocumentElement().normalize();       	
         	
         	NodeList qList = doc.getElementsByTagName("question");

         	Question question = null;
         	
         	
 			while (temp < qList.getLength()) {

         		Node nNode = qList.item(temp);
         				
         		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

         			Element eElement = (Element) nNode;
         			System.out.println("Question : " + eElement.getElementsByTagName("issue").item(0).getTextContent());
         			question = new Question(eElement.getElementsByTagName("issue").item(0).getTextContent());
         			for(int i = 0; i < 4; i++)
         			{
         				System.out.println(i+1 +" Answer : " + eElement.getElementsByTagName("answer").item(i).getTextContent());
         				if(i == 0)
         				{
         					question.addAnswer(eElement.getElementsByTagName("answer").item(i).getTextContent());
         				}
         				else
         				{
         					question.addBogusAnswer(eElement.getElementsByTagName("answer").item(i).getTextContent());
         				}
         			}
         			System.out.println("Timeout : " + eElement.getElementsByTagName("timeout").item(0).getTextContent());
         			question.setTimeout(Long.parseLong(eElement.getElementsByTagName("timeout").item(0).getTextContent()));
         			
         			question.shuffleAnswers();
                     questions.add(question);
                     temp++;
         		}
         	}
             return questions;
        }

    }
}