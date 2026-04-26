<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Scan History</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f4f4f4; }
        .container { width: 800px; margin: 40px auto; background: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 0 10px #ccc; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 10px; border-bottom: 1px solid #ddd; text-align: center; }
        th { background: #007bff; color: #fff; }
        tr:nth-child(even) { background: #f9f9f9; }
        tr:hover { background: #f1f1f1; }
        .safe { color: green; font-weight: bold; }
        .suspicious { color: orange; font-weight: bold; }
        .dangerous { color: red; font-weight: bold; }
        .btn { padding: 10px 20px; background: #007bff; color: #fff; border: none; border-radius: 4px; cursor: pointer; font-size: 14px; margin-top: 20px; }
        .btn:hover { background: #0056b3; }
        .no-data { text-align: center; color: #666; padding: 20px; }
    </style>
</head>
<body>
    <div class="container">
        <h2>Scan History</h2>

        <c:if test="${empty logs}">
            <p class="no-data">No scan history found. Check some URLs first!</p>
        </c:if>

        <c:if test="${not empty logs}">
            <table>
                <tr>
                    <th>ID</th>
                    <th>URL</th>
                    <th>Score</th>
                    <th>Result</th>
                    <th>Checked At</th>
                </tr>
                <c:forEach items="${logs}" var="log">
                    <tr>
                        <td>${log.id}</td>
                        <td style="word-break:break-all;">${log.url}</td>
                        <td>${log.score}</td>
                        <td class="${log.resultCssClass}">${log.result}</td>
                        <td>${log.checkedAt}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>

        <form action="index" method="get" style="text-align: center;">
            <input type="submit" value="Back to Home" class="btn">
        </form>
    </div>
</body>
</html>
