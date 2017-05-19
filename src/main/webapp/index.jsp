<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.co.sergio.mundo.vo.*"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<style type="text/css">
body {
    background-image:
        url('http://cdn.crunchify.com/wp-content/uploads/2013/03/Crunchify.bg_.300.png');
}
</style>
 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Crunchify JSP Servlet Example</title>
</head>
<body>
    <div align="center" style="margin-top: 50px;">
        <form action="CrunchifyServlet">
           Autor:  <input type="text" name="autor" size="20px"> <br>
           Nombre:  <input type="text" name="nombre" size="20px"> <br>
           Decripcion:  <input type="text" name="descripcion" size="20px"> <br>
           Estilo:  <input type="text" name="estilo" size="20px"> <br>
           Valor:  <input type="text" name="valor" size="20px"> <br>
        <input type="submit" value="submit">
        </form>
     </div>
 
    <%
       if( request.getAttribute("obras")!=null){
          List<Obra> obras  = (List<Obra>)request.getAttribute("obras");
           for (Obra obra : obras) {
         %>      
         <h1> <%=obra.getNombreautor().getNombreautor()%> </h1><br/> 
         <%      
          }
       }
      
    
    %>
    <img src="ChartServlet" />
</body>
</html>
