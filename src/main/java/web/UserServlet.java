package web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import Entities.User;
import Services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet ("/api/v1/users/all")
public class UserServlet extends HttpServlet {
    @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
     String userJson;
     ObjectMapper objMapper = new ObjectMapper(); 
     SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept("created_at", "updated_at"); // which fields will not serialize
     //SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAll(); // all fields will serialize
     FilterProvider filters = new SimpleFilterProvider().addFilter("filterMark", filter);       
     
     if (req.getParameter("id") == null & req.getParameter("field") == null & req.getParameter("order") == null) {
          ArrayList<User> users = UserService.getAll(); //create list of object  
          userJson = objMapper.writer(filters).writeValueAsString(users); // serialize objects        
     } else if (req.getParameter("id") == null) {
               ArrayList<User> users = UserService.getAll(req.getParameter("field"), req.getParameter("order")); //create list of object  
               userJson = objMapper.writer(filters).writeValueAsString(users); // serialize objects       
     } else {           
          try {
               int id = Integer.parseInt(req.getParameter("id"));
               User user = UserService.getOneFull(id); //get user by id                
               userJson = objMapper.writer(filters).writeValueAsString(user); // serialize objects 
          } catch(NumberFormatException e) {
               userJson = "{ id not integer }";
          }               
     }
     
     resp.setContentType("text/json");
     PrintWriter printWriter = resp.getWriter();
     printWriter.write(userJson);
     printWriter.close();
   }
}
