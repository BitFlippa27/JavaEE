<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>Erste JSP Seite</title>
      <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
      <link rel="stylesheet"  type="text/css"  href="layout.css">
<%@ page import="java.util.*, java.text.*" %>
</head>
<body>
<h1>Erste JSP Seite</h1>
Die folgenden Ausgaben werden mit JSP erzeugt
</br>
<%  // Datum und Zeit ausgeben
    Date datum = new Date();
    DateFormat format = DateFormat.getDateTimeInstance();
    String heute = format.format(datum);
  	System.out.println("Hansi");
    out.print("Heute haben wir "+heute); 
    RequestDispatcher dispatcher = request.getRequestDispatcher("BeispielSchleife");
    dispatcher.include(request, response);%>
</br>
<%= "Vorname: "+request.getParameter("vorname") %>
</br>
<%= "Nachname: "+request.getParameter("nachname") %>
</br>
<%= "request.getContextPath():="+request.getContextPath() %>
</br>
<%= "request.getServletPath():="+request.getServletPath() %>
</body>
</html>
