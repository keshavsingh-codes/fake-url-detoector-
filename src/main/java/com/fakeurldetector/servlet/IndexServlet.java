package com.fakeurldetector.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * IndexServlet.java
 * 
 * This is a CONTROLLER in MVC architecture.
 * Handles navigation back to the home page (index.jsp).
 * 
 * Why do we need this?
 * --------------------
 * MVC STRICT: All navigation must go through Servlets!
 * We cannot have direct links like <a href="index.jsp">
 * 
 * This servlet simply forwards to index.jsp when user wants to go back home.
 * 
 * Request Flow:
 * --------------
 * 1. User clicks "Check Another URL" or "Back to Home" button
 * 2. Form submits to /index URL (mapped in web.xml)
 * 3. This servlet's doGet() method is called
 * 4. Servlet forwards to index.jsp
 */

public class IndexServlet extends HttpServlet {
    
    /**
     * Handles HTTP GET requests for navigating to home page.
     * 
     * @param request  HttpServletRequest containing the request
     * @param response HttpServletResponse for sending response
     * @throws ServletException if servlet error occurs
     * @throws IOException      if I/O error occurs
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Simply forward to index.jsp
        // No business logic needed here - just navigation
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}