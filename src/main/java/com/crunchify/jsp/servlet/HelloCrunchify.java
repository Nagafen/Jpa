package com.crunchify.jsp.servlet;
 
import edu.co.sergio.mundo.dao.ObraDAO;
import edu.co.sergio.mundo.vo.Artista;
import edu.co.sergio.mundo.vo.Obra;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
 
/**
 * @author Crunchify.com
 */
 
public class HelloCrunchify extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // reading the user input
        String autor = request.getParameter("autor");
        String nombre = request.getParameter("nombre");
        String descripcion= request.getParameter("descripcion");
        String estilo = request.getParameter("estilo");
        String valor = request.getParameter("valor");
        //Se debe incluir validaciones - Lo recuerda: Gestion de Excepciones.
        ObraDAO dao = new ObraDAO();
        
        Obra obra = new Obra();
        Artista artist=new Artista();
        artist.setNombreautor(autor);
        obra.setNombreautor(artist);
        obra.setNombreobra(nombre);
        obra.setDescripcion(descripcion);
        obra.setEstilo(estilo);
        obra.setValor(Float.valueOf(valor));
        try {
            dao.create(obra);
        } catch (Exception ex) {
            Logger.getLogger(HelloCrunchify.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Listando la informacion  
        List<Obra> obras =  dao.findObraEntities();
        request.setAttribute("obras", obras);
       
       
        //Redireccionando la informacion
        RequestDispatcher redireccion = request.getRequestDispatcher("index.jsp");
        redireccion.forward(request, response);
        
        
        }
}
