package spiel;
import java.util.Random;

/**
 * In dieser Klasse wird die Spiellogik des Verschiebe-Spiels implementiert
 * Sie realisiert die Schnittstelle zwischen GUI und Spiel.
 * 
 * Die Ziffern des Spiels werden von links nach rechts und oben nach unten in einer Liste gespeichert.
 * Das leere Feld wird durch eine leere Position in der Liste realisiert.
 * Enthält die Liste von links nach rechts gelesen die Ziffernreihenfolge 1,2,3,4,5,6,7,8,null so ist das Spiel gewonnen
 * Das Spiel beginnt mit einer unsortierten Liste von Werten  - mit dem leeren Feld am Ende. 
 * Die Position einer Ziffer wird durch einen Zufallssgenerator ermittelt.
 * In einem Zähler ( statische Eigenschaft ) werden die Anzahl der Verschiebungen während dem Spiel gespeichert
 * 
 *
 * @author Prof. S. Keller
 */

public class Logik 
{  Integer grid[] = new Integer[3*3];
   Random  zufall = new Random();
   static  int zaehler = 0;
   
   /**
	 * Konstruktor der Klasse 
	 * Initialisiert ein array von Integer-Objekten.
	 * 
	 * Die Ziffern 1 - 8 werden an zufällig errechneten Positionen gespeichert,
	 * so dass man eine unsortierte Liste erhält.
	 * Der letzte Wert im array ist zu Beginn null 
	 * 
	 * Der Zähler wird auf 0 gesetzt
	 */   
@SuppressWarnings("deprecation")
public Logik()
   { 
	for ( int i = 0; i<3*3; i++) grid[i]= new Integer(i+1);
	 grid[3*3-1]=null;
     this.mische();
     zaehler = 0;
   }
	
  private void mische()
  {  int zufallsposition;
   
	  for ( int i = 0; i < 3*3-1; i++)
	  {   // zufällige Position zum tauschen errechnen
		  zufallsposition=zufall.nextInt()%8;
		  if ( zufallsposition < 0 ) zufallsposition = - zufallsposition;  

		  
		  //tausche position 0 mit zufallsposition 
		  Integer temp = grid[zufallsposition];
		  grid[zufallsposition]=grid[0];
		  grid[0] = temp;
	  }
  }
  
  // Leere Position suchen
  private int getLeer()
  {   
	  int i=0, stelle = -1;
  
	  while(  grid[i] != null) i++;
	  stelle = i;
	  
	  return stelle;
  }

  /**
	 * Die Methode move() verschiebt das Feld mit der 'ziffer', 
	 * falls das Nachbarfeld leer ist 

	 * @param  ziffer zu verschiebende Ziffer
	 * @return true, wenn eine Verschiebung durchgeführt wurde
	 *         false, wenn keine Verschiebung stattgefunden hat
	 */
//public boolean move(String ziffer)
public boolean move(Integer ziffer)
{  
	int von=-1,nach=-1;
    Integer i=null;
	boolean move=false;
   
	for ( int y = 0; y<3*3; y++)
	{       
            i=grid[y];
 		    if ( i != null &&  i.equals(ziffer)/*i.toString().equals(ziffer)*/ )  von = y; 
	}
	if ( von != -1 ) 
	{ 
	  nach = this.getLeer(); 

	  switch ( von)
	  {
	  case 0: if ( nach==1 || nach == 3) move=true; break;
	  case 1: if ( nach == 0 || nach == 2 || nach == 4) move=true; break;
	  case 2: if ( nach==1 || nach == 5) move=true; break;
	  case 3: if ( nach == 0 || nach == 6 || nach == 4) move=true; break;
	  case 4: if ( nach == 1 || nach == 3 || nach == 5 || nach==7 ) move=true; break;
	  case 5: if ( nach == 8 || nach == 2 || nach == 4) move=true; break;
	  case 6: if ( nach==7 || nach == 3) move=true; break;
	  case 7: if ( nach == 6 || nach == 8 || nach == 4) move=true; break;
	  case 8: if ( nach==5 || nach == 7) move=true; break;
	  default: move=false;
	  }
	  
	  if ( move)
	  {	  grid[nach]=grid[von];
	      grid[von]=null;
	      zaehler = zaehler+1;
	  }
	}

	return move; 
	}

/**
 * Die Methode getZaehler() liefert den aktuellen Zählerstand 
 * @return aktuellen Zählerstand
 */
    public int getZaehler() 
    {
    	return zaehler; 
    }
    
    // Nummern von links nach rechts und oben nach unten abfragen
    /**
	 * Die Methode toString() liefert die Ziffern des arrays von links nach rechts
	 * Das Leere Feld wird durch das Zeichen '-' codiert
	 *   
	 * @return  String, in dem die Ziffern der Liste von links nach rechts stehen. 
	 */
	
    public String toString()
    {
    	String nummernstring = new String("");
        Integer i;
    
        for ( int y = 0; y<3*3; y++)
	    {		i = grid[y];
			    if (i != null ) nummernstring = nummernstring+i.toString();
			    else nummernstring=nummernstring+"-";
	     }
	     return nummernstring;

    	};
    
    	
        /**
    	 * Die Methode getNummern() liefert das Integer-Array, in dem die Ziffern des Spiels gespeichert sind
    	 *   
    	 * @return  Integer[], in dem die Ziffern von links nach recht / oben nach unten gespeichert sind 
    	 * Das leere Feld des Spiels wird durch durch den Wert null repräsentiert
    	 */
    public Integer[] getNummern()
    {  
	    return grid;
    }
    

	/**
	 * Die Methode richtigeReihenfolge()prüft, ob die Ziffern in der richtigen 
	 * Reihenfolge 1,2,3,4,5,6,7,8,null im in der Liste stehen
	 * 
	 *@return true , falls die richtige Reihenfolge erkannt wurde, sonst false
	 */
    public boolean richtigeReihenfolge()
    {   boolean erg=false;
    	for ( int i = 0; i<3*3-1; i++)
    	{		Integer wert = grid[i];
    			if (wert != null && wert.intValue()== i+1) erg = true;
    			else return false;
    	}
    	return erg;
    }
    
    @SuppressWarnings("deprecation")
	public void TestSpielEnde()
    {
    	for ( int i = 0; i<3*3; i++)
    	{		grid[i]=new Integer(i+1);
    			if (i == 8) 
    				grid[i]=null;
    	}
    }
    
    // array in JSON-String wandlen ( wird f�r AJAX ben�tigt
    public String toJSON()
    {
        String jsonString="[";
        
        for ( int i=0; i<8; i++) jsonString=jsonString+ this.grid[i].toString()+",";
        jsonString = jsonString + this.grid[8].toString()+"]";
        System.out.println(jsonString);
        return jsonString;
    
    }
   public Integer getGridElement(int x,int y)
   {   
       int position=3*x+y;
   	   return this.grid[position];
   }
}
