<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!--
    result.jsp
    
    This page displays the URL scan results.
    It receives data from CheckURLServlet via request attributes.
    
    Expression Language (EL):
    - ${urlResult.url}     -> urlResult.getUrl()
    - ${urlResult.score}   -> urlResult.getScore()
    - ${urlResult.result}  -> urlResult.getResult()
    
    Why use a Model object instead of loose attributes?
    - Cleaner code: One object instead of multiple setAttribute() calls
    - Type safety: The JSP knows it's working with a URLResult object
    - Encapsulation: Related data stays together
    - Easier to extend: Add new fields to URLResult without changing Servlet logic
    
    EL makes it easy to access Java objects in JSP without scriptlets.
-->
<!DOCTYPE html>
<html>
<head>
    <title>Scan Result</title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            background: #f4f4f4; 
        }
        
        .container { 
            width: 400px; 
            margin: 80px auto; 
            background: #fff; 
            padding: 30px; 
            border-radius: 8px; 
            box-shadow: 0 0 10px #ccc; 
            text-align: center;  /* Center all content */
        }
        
        /* Dynamic CSS classes based on result */
        .safe { 
            color: green;           /* Green for safe URLs */
            font-weight: bold;
        }
        
        .suspicious { 
            color: orange;          /* Orange for suspicious URLs */
            font-weight: bold;
        }
        
        .dangerous { 
            color: red;             /* Red for dangerous URLs */
            font-weight: bold;
        }
        
        a { 
            text-decoration: none; 
            color: #007bff;
            margin: 0 5px;          /* Spacing between links */
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Scan Result</h2>
        
        <!-- 
            Display scan details using Expression Language (EL)
            These values come from the URLResult object set in CheckURLServlet
        -->
        <p><strong>URL:</strong> ${urlResult.url}</p>
        <p><strong>Score:</strong> ${urlResult.score}</p>
        <p>
            <strong>Result:</strong>
            <!-- 
                Dynamic CSS class based on result value
                ${urlResult.resultCssClass} converts SAFE -> safe
                This matches our CSS classes: .safe, .suspicious, .dangerous
            -->
            <span class="${urlResult.resultCssClass}">${urlResult.result}</span>
        </p>
        
        <!-- 
            MVC STRICT: All navigation MUST go through Servlets!
            Using forms to navigate instead of direct JSP links.
        -->
        <form action="index" method="get" style="display:inline;">
            <input type="submit" value="Check Another URL" style="background: #007bff; color: white; padding: 8px 15px; border: none; border-radius: 4px; cursor: pointer;">
        </form>
        
        <form action="history" method="get" style="display:inline;">
            <input type="submit" value="View History" style="background: #28a745; color: white; padding: 8px 15px; border: none; border-radius: 4px; cursor: pointer;">
        </form>
    </div>
</body>
</html>
