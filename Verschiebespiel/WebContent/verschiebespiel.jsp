<!DOCTYPE html>
<html>

<head>
      <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
      <title>Verschiebespiel</title>
      <link rel="stylesheet"  type="text/css"  href="verschiebespiel.css">
      <%@ page import="java.util.*, java.text.*" %>
      <%@ page import="spiel.Logik" %>
</head>
<body>
   <%! // jsp Declaration
       private static int zaehler=0; // Anzahl der jsp-Aufrufe mitzaehlen 
   %>
   
   <%  // jsp Scriptlet
   
       // Bei aufruf der jsp-Seite wird normalerweise eine Session erzeugt. In jsp kann man mit dem session-Objekt arbeiten
       // Falls es noch keine Session gibt dann Servlet aufrufen, damit eine Session und Spielelogik erzeugt wird
       System.out.println("Start der jsp-Seite zaehler= "+zaehler);
       if ( session == null ) 
       {   System.out.println("jsp-Seite: Session oder zaehler is null. Servlet verschiebespiel aufrufen");
    	   request.getRequestDispatcher(response.encodeURL("verschiebespiel")).include(request,response); 
       }
       zaehler++;
   %>
   <h1>Willkommen beim Verschiebespiel</h1>
   <div class="spielfeld">
     <% Logik spielelogik = (Logik) session.getAttribute("spielelogik"); %>
     <table>
       <tr>
          <td><form action=<%= response.encodeURL("verschiebespiel") %>> <input class="is<%= spielelogik.getGridElement(0,0) %>" name="ziffer" type="submit" value=<%= spielelogik.getGridElement(0,0) %> ></form></td>
          <td><form action=<%= response.encodeURL("verschiebespiel") %>> <input class="is<%= spielelogik.getGridElement(0,1) %>" name="ziffer" type="submit" value=<%= spielelogik.getGridElement(0,1) %> ></form></td>
          <td><form action=<%= response.encodeURL("verschiebespiel") %>> <input class="is<%= spielelogik.getGridElement(0,2) %>" name ="ziffer" type="submit" value=<%= spielelogik.getGridElement(0,2) %> ></form></td>
       </tr>
       <tr>
         <td><form action=<%= response.encodeURL("verschiebespiel") %>><input class="is<%= spielelogik.getGridElement(1,0) %>" name ="ziffer" type="submit" value=<%= spielelogik.getGridElement(1,0) %> ></form></td>
         <td><form action=<%= response.encodeURL("verschiebespiel") %>><input class="is<%= spielelogik.getGridElement(1,1) %>" name ="ziffer" type="submit" value=<%= spielelogik.getGridElement(1,1) %> ></form></td>
         <td><form action=<%= response.encodeURL("verschiebespiel") %>><input class="is<%= spielelogik.getGridElement(1,2) %>" name ="ziffer" type="submit" value=<%= spielelogik.getGridElement(1,2) %> ></form></td>
       </tr>
       <tr>
         <td><form action=<%= response.encodeURL("verschiebespiel") %>><input class="is<%= spielelogik.getGridElement(2,0) %>" name ="ziffer" type="submit" value=<%= spielelogik.getGridElement(2,0) %> ></form></td>
         <td><form action=<%= response.encodeURL("verschiebespiel") %>><input class="is<%= spielelogik.getGridElement(2,1) %>"  name ="ziffer" type="submit" value=<%= spielelogik.getGridElement(2,1) %> ></form></td>
         <td><form action=<%= response.encodeURL("verschiebespiel") %>><input class="is<%= spielelogik.getGridElement(2,2) %>" name ="ziffer" type="submit" value=<%= spielelogik.getGridElement(2,2) %> ></form></td>
       </tr>
     </table>
   </div>
   
   <div class="SessionAnzeige">
     <% DateFormat format = DateFormat.getDateTimeInstance();
        String heute = format.format(session.getLastAccessedTime());
        String creation = format.format(session.getCreationTime());
     %>
    <p>Daten zu der Sitzung:</p>
    <p> Session-ID=<%= session.getId() %></p>
    <p> Creation Time: <%= creation %></p>
    <p> Last Access Time: <%= heute %></p> 
  </div>
   
</body>
</html>