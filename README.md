# Fake URL Detector - Complete MVC Java Web Application

A complete Java web application built using **Core Java**, **Servlets**, **JSP**, and **JDBC** to detect potentially fake or phishing URLs through rule-based analysis.

## Project Overview

This application allows users to:
1. Input a URL through a web form
2. Analyze the URL using multiple security rules
3. View a risk score and classification (SAFE / SUSPICIOUS / DANGEROUS)
4. Store and view scan history in a MySQL database

## Technology Stack

| Layer | Technology |
|-------|------------|
| Language | Core Java |
| Controller | Java Servlets |
| View | JSP (JavaServer Pages) with JSTL + EL |
| Data Access | JDBC (Java Database Connectivity) |
| Database | MySQL |
| Server | Apache Tomcat 9 |

## MVC Architecture

This project follows strict **Model-View-Controller (MVC)** architecture:

```
┌─────────────────────────────────────────────────────────────┐
│                         BROWSER                             │
└──────────────┬──────────────────────────────┬───────────────┘
               │                              │
         HTTP GET/POST                 HTTP GET/POST
               │                              │
    ┌──────────▼──────────┐        ┌──────────▼──────────┐
    │   IndexServlet      │        │   CheckURLServlet   │
    │   (Controller)      │        │   (Controller)      │
    └──────────┬──────────┘        └──────────┬──────────┘
               │                              │
               │  Forwards to                 │
               │  index.jsp                   │
    ┌──────────▼──────────┐                   │
    │     index.jsp       │◄──────────────────┘
    │      (View)         │        Forwards to
    │  URL Input Form     │        result.jsp
    └─────────────────────┘                   │
                                              │
                                   ┌──────────▼──────────┐
                                   │    result.jsp       │
                                   │      (View)         │
                                   │   Scan Results      │
                                   └─────────────────────┘
                                              │
                                              ▼
                                   ┌─────────────────────┐
                                   │   HistoryServlet    │
                                   │    (Controller)     │
                                   └──────────┬──────────┘
                                              │
                                   ┌──────────▼──────────┐
                                   │    history.jsp      │
                                   │      (View)         │
                                   │   Scan History      │
                                   └─────────────────────┘
```

### Request Flow Diagram (Text)

```
[User] --enters URL--> [index.jsp] --POST /check--> [CheckURLServlet]
                                                          │
                              ┌───────────────────────────┘
                              ▼
                    [URLDetector.java] (Model - Business Logic)
                              │
                    Calculates score & classifies
                              │
                              ▼
                    [URLDao.java] (DAO - Database)
                              │
                    Inserts into MySQL (PreparedStatement)
                              │
                              ▼
                    Sets URLResult object in request
                              │
                              ▼
                    [RequestDispatcher.forward()]
                              │
                              ▼
                    [result.jsp] (View - JSP + EL)
                              │
                    Displays URL, Score, Result
                              │
[User] --clicks View History--> [GET /history] --> [HistoryServlet]
                                                          │
                              ┌───────────────────────────┘
                              ▼
                    [URLDao.getAllLogs()] (DAO)
                              │
                    Fetches List<URLLog> from DB
                              │
                              ▼
                    Sets logs list in request
                              │
                              ▼
                    [RequestDispatcher.forward()]
                              │
                              ▼
                    [history.jsp] (View - JSP + JSTL)
                              │
                    Displays table of all scans
```

## Project Structure

```
FakeURLDetector/
├── src/main/java/com/fakeurldetector/
│   ├── model/
│   │   ├── URLDetector.java        # Model: URL analysis business logic
│   │   ├── URLResult.java          # Model: Encapsulates scan result (url, score, result)
│   │   └── URLLog.java             # Model: Represents one DB record
│   ├── dao/
│   │   └── URLDao.java             # DAO: Database operations (insert, fetch)
│   ├── util/
│   │   └── DBConnection.java       # Utility: MySQL connection manager
│   └── servlet/
│       ├── IndexServlet.java       # Controller: Navigates to home page
│       ├── CheckURLServlet.java    # Controller: Handles URL scan requests
│       └── HistoryServlet.java     # Controller: Handles history view requests
├── WebContent/
│   ├── index.jsp                   # View: URL input form
│   ├── result.jsp                  # View: Displays scan result
│   ├── history.jsp                 # View: Displays scan history (JSTL + EL)
│   └── WEB-INF/
│       ├── web.xml                 # Deployment descriptor
│       ├── classes/                # Compiled .class files
│       └── lib/                    # JAR dependencies
│           ├── mysql-connector-j-8.0.33.jar
│           └── jstl-1.2.jar
└── README.md
```

## File Descriptions

### Model Layer

| File | Purpose |
|------|---------|
| **URLDetector.java** | Contains all URL analysis rules. Calculates risk score and classifies as SAFE/SUSPICIOUS/DANGEROUS. |
| **URLResult.java** | JavaBean that holds the result of one URL scan (url, score, result). Passed from Servlet to JSP via request attribute. |
| **URLLog.java** | JavaBean that represents one row from the `url_logs` database table. Used for displaying history. |

### Controller Layer (Servlets)

| File | URL Mapping | Purpose |
|------|-------------|---------|
| **IndexServlet.java** | `/index` | Forwards to `index.jsp` (home page navigation) |
| **CheckURLServlet.java** | `/check` | Receives URL, calls URLDetector, calls URLDao, forwards to `result.jsp` |
| **HistoryServlet.java** | `/history` | Fetches all logs from URLDao, forwards to `history.jsp` |

### View Layer (JSP)

| File | Purpose |
|------|---------|
| **index.jsp** | Simple HTML form to enter URL. Submits to `/check`. |
| **result.jsp** | Displays scan result using Expression Language: `${urlResult.url}`, `${urlResult.score}`, `${urlResult.result}` |
| **history.jsp** | Displays scan history table using JSTL `<c:forEach>` and EL. No Java scriptlets. |

### Data Access Layer

| File | Purpose |
|------|---------|
| **URLDao.java** | Provides `insertURL()` and `getAllLogs()` using JDBC `PreparedStatement`. |
| **DBConnection.java** | Manages MySQL connection. Configurable URL, username, password. |

## Database Schema

```sql
CREATE DATABASE fakeurldb;
USE fakeurldb;

CREATE TABLE url_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    url TEXT NOT NULL,
    score INT NOT NULL,
    result VARCHAR(20) NOT NULL,
    checked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## URL Detection Rules

| Rule | Description | Risk Points |
|------|-------------|-------------|
| Contains '@' | Credential stuffing / redirect trick | +3 |
| Encoded @ (%40) | Hidden @ symbol | +3 |
| Uses HTTP | Unencrypted connection | +2 |
| Very Long URL (>100 chars) | Obfuscation attempt | +3 |
| Long URL (>60 chars) | Suspicious length | +2 |
| IP Address instead of domain | Direct IP access | +4 |
| Suspicious keywords | login, verify, bank, etc. | +2 each |
| Brand impersonation | Fake brand domains | +3 |
| URL shorteners | bit.ly, tinyurl, etc. | +3 |
| Suspicious TLDs | .tk, .ml, .xyz, etc. | +3 |
| Excessive subdomains | >3 dots in domain | +2 |
| Numbers in domain | bank-123.com | +2 |
| Multiple hyphens/dots | -- or .. patterns | +2 |
| Port in URL | Non-standard port | +2 |
| Double slash in path | Redirect attempt | +2 |
| Excessive parameters | >3 & or >1 ? | +2 |
| Hex obfuscation | Multiple %xx sequences | +2 |
| Mixed case domain | GoOgLe.com | +1 |
| Unicode/homograph | Non-ASCII characters | +4 |
| Data URI scheme | data:text/html | +8 |
| JavaScript protocol | javascript: | +8 |

## Risk Classification

| Score Range | Classification | Color |
|-------------|----------------|-------|
| 0 - 3 | SAFE | Green |
| 4 - 7 | SUSPICIOUS | Orange |
| 8+ | DANGEROUS | Red |

## Prerequisites

1. **JDK 8 or above** installed
2. **Apache Tomcat 9 or above** installed
3. **MySQL Server** installed and running
4. **MySQL JDBC Driver** (`mysql-connector-j-8.0.33.jar`)
5. **JSTL Library** (`jstl-1.2.jar`)

## Step-by-Step Setup

### Step 1: Database Setup

Open MySQL Command Line or MySQL Workbench and run:

```sql
CREATE DATABASE fakeurldb;
USE fakeurldb;

CREATE TABLE url_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    url TEXT NOT NULL,
    score INT NOT NULL,
    result VARCHAR(20) NOT NULL,
    checked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Step 2: Update Database Credentials

Open `src/main/java/com/fakeurldetector/util/DBConnection.java` and update:

```java
private static final String USER = "root";           // Your MySQL username
private static final String PASSWORD = "your_password"; // Your MySQL password
```

### Step 3: Compile Java Files

Open Command Prompt in project folder and run:

```cmd
javac -d WebContent/WEB-INF/classes src/main/java/com/fakeurldetector/model/*.java src/main/java/com/fakeurldetector/dao/*.java src/main/java/com/fakeurldetector/util/*.java src/main/java/com/fakeurldetector/servlet/*.java
```

### Step 4: Add JAR Dependencies

Copy these JARs to `WebContent/WEB-INF/lib/`:
- `mysql-connector-j-8.0.33.jar`
- `jstl-1.2.jar`

### Step 5: Deploy to Tomcat

**Option A: Manual Deployment**
1. Copy the entire `WebContent` folder contents to `apache-tomcat-9.x/webapps/FakeURLDetector/`
2. Or create a WAR file and deploy through Tomcat Manager

**Option B: Using Eclipse/IntelliJ**
1. Import as Dynamic Web Project
2. Add MySQL Connector and JSTL JARs to Build Path
3. Configure Tomcat Server
4. Run on Server

### Step 6: Run the Application

1. Start MySQL Server
2. Start Apache Tomcat Server
3. Open browser and visit:
   ```
   http://localhost:8080/FakeURLDetector/
   ```

## How Request Flows (Detailed)

### Flow 1: Check a URL

```
1. Browser displays index.jsp (form to enter URL)
2. User enters URL and clicks "Check URL"
3. Form POSTs to /check (CheckURLServlet)
4. CheckURLServlet.doPost():
   a. Gets "url" parameter from request
   b. Validates input (not null/empty)
   c. Calls URLDetector.calculateScore(url) → Model
   d. Calls URLDetector.classify(score) → Model
   e. Creates URLResult object
   f. Calls URLDao.insertURL(url, score, result) → DAO
   g. Sets urlResult attribute in request
   h. RequestDispatcher forwards to result.jsp
5. result.jsp displays:
   - URL: ${urlResult.url}
   - Score: ${urlResult.score}
   - Result: ${urlResult.result} (with CSS color)
```

### Flow 2: View History

```
1. User clicks "View History" button
2. Form GETs to /history (HistoryServlet)
3. HistoryServlet.doGet():
   a. Calls URLDao.getAllLogs() → DAO
   b. Sets logs attribute in request
   c. RequestDispatcher forwards to history.jsp
4. history.jsp displays:
   - If logs empty: "No scan history found"
   - If logs present: Table with ID, URL, Score, Result, Checked At
   - Uses JSTL <c:forEach> to iterate
   - Uses EL ${log.id}, ${log.url}, etc.
```

### Flow 3: Navigate Home

```
1. User clicks "Check Another URL" or "Back to Home"
2. Form GETs to /index (IndexServlet)
3. IndexServlet.doGet():
   a. RequestDispatcher forwards to index.jsp
4. index.jsp displays the input form again
```

## Why No Java Code in JSP?

This project uses **Expression Language (EL)** and **JSTL** instead of Java scriptlets (`<% %>`):

| Feature | Old Way (Scriptlet) | New Way (EL + JSTL) |
|---------|---------------------|---------------------|
| Access attribute | `<%= request.getAttribute("url") %>` | `${url}` |
| Iterate list | `<% for(URLLog log : logs) { %>` | `<c:forEach items="${logs}" var="log">` |
| Conditional | `<% if(logs == null) { %>` | `<c:if test="${empty logs}">` |
| Access property | `<%= log.getUrl() %>` | `${log.url}` |

**Benefits:**
- Cleaner, more readable JSP code
- Better separation of concerns (no business logic in View)
- Easier to maintain and debug
- Follows modern JSP best practices

## Viva Questions You May Face

### Q1: What is MVC architecture?
**Answer:** MVC stands for Model-View-Controller.
- **Model**: Java classes that contain business logic (URLDetector, URLResult, URLLog)
- **View**: JSP pages that display data (index.jsp, result.jsp, history.jsp)
- **Controller**: Servlets that handle HTTP requests and coordinate Model/View

### Q2: Why use PreparedStatement instead of Statement?
**Answer:**
1. **SQL Injection Protection**: User input is treated as data, not executable SQL
2. **Performance**: SQL is pre-compiled, faster for repeated executions
3. **Type Safety**: Methods like `setString()`, `setInt()` ensure correct data types
4. **Readability**: Clear separation of SQL logic and data values

### Q3: How does the URL detection work?
**Answer:** Rule-based scoring system:
- Each suspicious feature adds points to a risk score
- Total score determines classification: SAFE (0-3), SUSPICIOUS (4-7), DANGEROUS (8+)
- Checks for @ symbol, HTTP, IP addresses, suspicious keywords, brand impersonation, etc.

### Q4: What is the role of web.xml?
**Answer:** Deployment descriptor for the web application:
- Maps URL patterns to servlets (`/check` → CheckURLServlet)
- Configures welcome files (default page)
- Located in `WEB-INF/` which is not accessible via URL

### Q5: Why is URLResult a separate class?
**Answer:**
- Encapsulates scan result data (url, score, result) into one object
- Servlet passes ONE object to JSP instead of multiple loose attributes
- Follows JavaBean convention (private fields + public getters/setters)
- Easy to extend (can add scanTime, detailedReport later)

### Q6: What is JSTL and why use it?
**Answer:** JSTL (JavaServer Pages Standard Tag Library) provides standard tags for JSP:
- `<c:forEach>`: Iteration (replaces `for` loops)
- `<c:if>`: Conditional (replaces `if` statements)
- No Java code in JSP → cleaner, more maintainable views

### Q7: How does data flow from Servlet to JSP?
**Answer:**
1. Servlet sets attribute: `request.setAttribute("urlResult", urlResult)`
2. Servlet forwards: `request.getRequestDispatcher("result.jsp").forward(request, response)`
3. JSP accesses using EL: `${urlResult.url}`, `${urlResult.score}`, `${urlResult.result}`

### Q8: What is a DAO and why use it?
**Answer:** DAO (Data Access Object) pattern:
- Separates database operations from business logic
- Centralizes all SQL queries in one place
- If database changes, only DAO needs modification
- Follows Single Responsibility Principle

## License

This project is open source and available for educational use.
#   f a k e - u r l - d e t o e c t o r -  
 