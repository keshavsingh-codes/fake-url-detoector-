package com.fakeurldetector.servlet;

/**
 * CheckURLServlet.java
 *
 * This is the CONTROLLER component in MVC architecture.
 * Servlets act as controllers in Java web applications.
 *
 * What does this Servlet do?
 * ----------------------------
 * 1. Receives HTTP POST requests from the user form
 * 2. Extracts the URL parameter from the request
 * 3. Validates the input (not null, not empty)
 * 4. Passes URL to Model (URLDetector) for analysis
 * 5. Creates a URLResult model object to hold the result
 * 6. Stores results in database using DAO (URLDao)
 * 7. Forwards results to View (result.jsp) for display
 *
 * Servlet Lifecycle:
 * 1. init()       - Called once when servlet is first loaded
 * 2. doGet()      - Handles HTTP GET requests
 * 3. doPost()     - Handles HTTP POST requests (we use this)
 * 4. destroy()    - Called once when servlet is being removed
 */

import com.fakeurldetector.model.URLDetector;  // Model: URL analysis logic
import com.fakeurldetector.model.URLResult;    // Model: Result data object
import com.fakeurldetector.dao.URLDao;          // DAO: Database operations

import javax.servlet.*;           // Core servlet interfaces
import javax.servlet.http.*;      // HTTP-specific servlet classes
import java.io.IOException;       // For handling I/O errors

public class CheckURLServlet extends HttpServlet {

    /**
     * Handles HTTP POST requests sent from the URL check form.
     *
     * @param request  HttpServletRequest object containing form data
     * @param response HttpServletResponse object for sending response
     * @throws ServletException if servlet encounters error
     * @throws IOException      if I/O error occurs
     *
     * Request-Response Flow:
     * ----------------------
     * 1. User submits form from index.jsp
     * 2. Form action="check" maps to this servlet (configured in web.xml)
     * 3. This method processes the request
     * 4. Results are forwarded to result.jsp for display
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ============================================================
        // STEP 1: Retrieve user input from the form
        // ============================================================
        // request.getParameter("url") gets the value from the form field
        // where name="url" in index.jsp's input tag
        String url = request.getParameter("url");

        // ============================================================
        // STEP 2: Validate user input
        // ============================================================
        // We must validate to prevent empty or null submissions
        // If invalid, we forward back to index.jsp with an error message
        if (url == null || url.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please enter a valid URL.");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;  // Stop further processing
        }

        // Trim whitespace for consistent processing
        url = url.trim();

        // ============================================================
        // STEP 3: Analyze the URL using our Model (URLDetector)
        // ============================================================
        // We delegate business logic to the Model class
        // Controller should not contain business logic - just coordinates
        int score = URLDetector.calculateScore(url);
        String result = URLDetector.classify(score);

        // ============================================================
        // STEP 4: Create URLResult model object
        // ============================================================
        // Instead of passing loose attributes, we use a proper Model object
        // This is cleaner and follows MVC pattern strictly
        URLResult urlResult = new URLResult(url, score, result);

        // ============================================================
        // STEP 5: Store results in database using DAO
        // ============================================================
        // We delegate database operations to the DAO class
        // This follows separation of concerns principle
        URLDao.insertURL(url, score, result);

        // ============================================================
        // STEP 6: Prepare data for the View (JSP)
        // ============================================================
        // setAttribute() stores data in request scope
        // This data will be available in result.jsp using Expression Language
        // Example in JSP: ${urlResult.url}, ${urlResult.score}, ${urlResult.result}
        request.setAttribute("urlResult", urlResult);

        // ============================================================
        // STEP 7: Forward request to result.jsp for display
        // ============================================================
        // RequestDispatcher forwards the same request to another resource
        // This is server-side forwarding (internal to the application)
        // URL remains unchanged in browser - user sees result.jsp content
        RequestDispatcher rd = request.getRequestDispatcher("result.jsp");
        rd.forward(request, response);
    }
}

