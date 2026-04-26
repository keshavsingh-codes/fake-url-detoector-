<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--
    index.jsp
    
    This is the main entry point (View) of the application.
    It displays a form where users can enter a URL to analyze.
    
    JSP (JavaServer Pages):
    - JSP allows embedding dynamic content in HTML
    - It compiles to a Servlet behind the scenes
    - Easier to write UI compared to pure Servlets
    
    MVC STRICT REQUIREMENTS:
    - This page ONLY contains the input form
    - NO business logic here
    - All form submissions go to Servlets (not directly to other JSPs)
    - Navigation buttons also submit to Servlets
    
    @page directive at the top configures JSP properties:
    - language="java": Scripting language is Java
    - contentType="text/html; charset=UTF-8": Response format
-->
<!DOCTYPE html>
<html>
<head>
    <title>Fake URL Detector</title>
    <style>
        /* ============================================
           CSS STYLES
           Simple, clean design without frameworks
           ============================================ */
        
        body { 
            font-family: Arial, sans-serif;  /* Clean, readable font */
            background: #f4f4f4;              /* Light gray background */
            margin: 0;                         /* Remove default margin */
            padding: 0;                        /* Remove default padding */
        }
        
        .container { 
            width: 400px;                      /* Fixed width for form */
            margin: 80px auto;                 /* Center horizontally with top margin */
            background: #fff;                  /* White background for card */
            padding: 30px;                     /* Internal spacing */
            border-radius: 8px;                /* Rounded corners */
            box-shadow: 0 0 10px #ccc;         /* Subtle shadow effect */
        }
        
        input[type=text] { 
            width: 100%;                       /* Full width of container */
            padding: 10px;                     /* Comfortable input size */
            margin: 10px 0;                    /* Vertical spacing */
            border: 1px solid #ddd;            /* Light border */
            border-radius: 4px;                /* Slightly rounded */
            box-sizing: border-box;            /* Include padding in width */
        }
        
        input[type=submit] { 
            padding: 10px 20px;                /* Button size */
            background: #007bff;               /* Blue color */
            color: #fff;                       /* White text */
            border: none;                      /* Remove default border */
            border-radius: 4px;                /* Rounded corners */
            cursor: pointer;                   /* Hand cursor on hover */
            font-size: 16px;                   /* Readable text */
        }
        
        input[type=submit]:hover {
            background: #0056b3;               /* Darker blue on hover */
        }
        
        a { 
            text-decoration: none;             /* Remove underline */
            color: #007bff;                    /* Blue link color */
        }
        
        a:hover {
            text-decoration: underline;        /* Underline on hover */
        }
        
        .error {
            color: red;
            font-size: 14px;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Application Title -->
        <h2>Fake URL Detector</h2>
        
        <!-- Display error message if URL validation fails in CheckURLServlet -->
        <c:if test="${not empty errorMessage}">
            <p class="error">${errorMessage}</p>
        </c:if>
        
        <!-- 
            URL Input Form
            
            action="check"  -> Submits to URL mapped to CheckURLServlet
                              (configured in web.xml as /check)
            
            method="post"   -> Uses HTTP POST (more secure than GET for forms)
                              Data sent in request body, not visible in URL
            
            required        -> HTML5 validation - prevents empty submission
            placeholder     -> Hint text shown when field is empty
        -->
        <form action="check" method="post">
            <label>Enter URL:</label>
            <input type="text" name="url" required placeholder="https://example.com">
            <input type="submit" value="Check URL">
        </form>
        
        <br>
        
        <!-- 
            MVC STRICT: All navigation MUST go through Servlets!
            Direct JSP-to-JSP links are NOT allowed.
            
            Using a form with method="get" to navigate to HistoryServlet.
            This ensures all requests go through the Controller layer.
        -->
        <form action="history" method="get">
            <input type="submit" value="View Scan History" style="background: #28a745;">
        </form>
    </div>
</body>
</html>
