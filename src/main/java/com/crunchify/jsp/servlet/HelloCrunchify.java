package com.crunchify.jsp.servlet;
 
import edu.co.sergio.mundo.dao.UsersJpaController;
import edu.co.sergio.mundo.vo.Users;
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
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String lastname= request.getParameter("lastname");
        //Se debe incluir validaciones - Lo recuerda: Gestion de Excepciones.
        UsersJpaController dao = new UsersJpaController();
        
       
        Users users=new Users();
        users.setId(id);
        users.setName(name);
        users.setLastname(lastname);
        try {
            dao.create(users);
        } catch (Exception ex) {
            Logger.getLogger(HelloCrunchify.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Listando la informacion  
        List<Users> userss =  dao.findUsersEntities();
        request.setAttribute("users", userss);
       
       
        //Redireccionando la informacion
        RequestDispatcher redireccion = request.getRequestDispatcher("index.jsp");
        redireccion.forward(request, response);
        
        
        }
}
