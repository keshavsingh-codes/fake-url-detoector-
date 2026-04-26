package com.fakeurldetector.servlet;

import com.fakeurldetector.dao.URLDao;
import com.fakeurldetector.model.URLLog;  // Standalone Model class (moved from URLDao inner class)

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * HistoryServlet.java
 *
 * This is a CONTROLLER in MVC architecture.
 * Handles requests to view scan history from the database.
 *
 * Request Flow:
 * --------------
 * 1. User clicks "View History" button on index.jsp or result.jsp
 * 2. Form submits to /history URL (mapped in web.xml)
 * 3. This servlet's doGet() method is called
 * 4. Servlet calls URLDao.getAllLogs() to fetch data from database
 * 5. Servlet sets the data as request attribute
 * 6. Servlet forwards to history.jsp for display
 *
 * MVC Responsibilities:
 * - Controller: Handles HTTP request/response
 * - NO business logic here (delegated to Model)
 * - NO direct database access here (delegated to DAO)
 * - Forwards to View (JSP) for display
 */

public class HistoryServlet extends HttpServlet {

    /**
     * Handles HTTP GET requests for viewing scan history.
     *
     * @param request  HttpServletRequest containing the request
     * @param response HttpServletResponse for sending response
     * @throws ServletException if servlet error occurs
     * @throws IOException      if I/O error occurs
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // ============================================================
            // STEP 1: Call DAO to fetch all scan history from database
            // ============================================================
            // DAO handles all database operations
            // Returns List<URLLog> ordered by timestamp (newest first)
            List<URLLog> logs = URLDao.getAllLogs();

            // ============================================================
            // STEP 2: Set data as request attribute for JSP access
            // ============================================================
            // This makes the data available in JSP using EL: ${logs}
            request.setAttribute("logs", logs);

        } catch (Exception e) {
            // If error occurs, log it and set empty list
            e.printStackTrace();
            request.setAttribute("logs", null);
        }

        // ============================================================
        // STEP 3: Forward to history.jsp (View layer)
        // ============================================================
        // RequestDispatcher forwards the request to JSP
        // URL remains unchanged in browser
        RequestDispatcher rd = request.getRequestDispatcher("history.jsp");
        rd.forward(request, response);
    }
}

