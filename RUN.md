# How to Run Fake URL Detector - Complete Step-by-Step Guide

## Method 1: Using Eclipse IDE (Recommended for Beginners)

### Step 1: Install Prerequisites
1. Download and install **JDK 8 or above** from Oracle website
2. Download and install **Eclipse IDE for Enterprise Java Developers**
3. Download **Apache Tomcat 9** (zip file) from tomcat.apache.org
4. Download **MySQL Installer** and install MySQL Server + MySQL Workbench
5. Download **mysql-connector-java-8.0.xx.jar** (MySQL JDBC Driver)

### Step 2: Create Database
1. Open MySQL Workbench or MySQL Command Line Client
2. Run these exact commands:
```sql
CREATE DATABASE fakeurldb;
USE fakeurldb;
CREATE TABLE url_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(500) NOT NULL,
    score INT NOT NULL,
    result VARCHAR(20) NOT NULL,
    checked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Step 3: Import Project in Eclipse
1. Open Eclipse
2. Go to **File → Import → Existing Projects into Workspace**
3. Browse to: `C:/Users/Keshav Kumar/Desktop/fake url detector`
4. Click Finish

### Step 4: Add MySQL JDBC Driver
1. Right-click on project → **Build Path → Configure Build Path**
2. Go to **Libraries** tab → Click **Add External JARs**
3. Select your `mysql-connector-java-8.0.xx.jar`
4. Also copy this JAR to `WebContent/WEB-INF/lib/` folder

### Step 5: Configure Tomcat Server in Eclipse
1. In Eclipse, go to **Window → Show View → Servers**
2. Right-click in Servers view → **New → Server**
3. Select **Apache → Tomcat v9.0 Server** → Next
4. Click **Browse** and select your Tomcat installation folder
5. Click Finish

### Step 6: Deploy and Run
1. Right-click on project → **Run As → Run on Server**
2. Select your Tomcat server → Finish
3. Browser will open automatically at `http://localhost:8080/FakeURLDetector/`

---

## Method 2: Manual Deployment (Without IDE)

### Step 1: Compile Java Files
Open Command Prompt in the project folder and run:
```cmd
mkdir WebContent\WEB-INF\classes
javac -d WebContent/WEB-INF/classes src/main/java/com/fakeurldetector/util/DBConnection.java src/main/java/com/fakeurldetector/model/URLDetector.java src/main/java/com/fakeurldetector/dao/URLDao.java src/main/java/com/fakeurldetector/servlet/CheckURLServlet.java
```

### Step 2: Copy MySQL Driver
Copy `mysql-connector-java-8.0.xx.jar` into `WebContent/WEB-INF/lib/`

### Step 3: Deploy to Tomcat
1. Copy the entire project folder to: `C:/apache-tomcat-9.x/webapps/`
2. Rename the folder to `FakeURLDetector`

### Step 4: Start Tomcat
Run:
```cmd
C:/apache-tomcat-9.x/bin/startup.bat
```

### Step 5: Access Application
Open browser and go to:
```
http://localhost:8080/FakeURLDetector/
```

---

## Method 3: Using VS Code (Quick Method)

### Step 1: Install Extensions
1. Open VS Code
2. Install "Extension Pack for Java"
3. Install "Community Server Connectors" (for Tomcat)

### Step 2: Build Project
Press `Ctrl+Shift+P` → Type "Java: Export Jar" or use terminal:
```cmd
javac -d WebContent/WEB-INF/classes src/main/java/com/fakeurldetector/*/*.java
```

### Step 3: Create WAR File
```cmd
cd WebContent
jar -cvf FakeURLDetector.war .
```

### Step 4: Deploy WAR to Tomcat
Copy `FakeURLDetector.war` to `C:/apache-tomcat-9.x/webapps/`
Tomcat will automatically extract it.

### Step 5: Start and Access
1. Start Tomcat: `C:/apache-tomcat-9.x/bin/startup.bat`
2. Open: `http://localhost:8080/FakeURLDetector/`

---

## Important Configuration Checklist

Before running, make sure:
- [ ] MySQL Server is running (check Services app in Windows)
- [ ] Database `fakeurldb` is created
- [ ] Table `url_logs` is created
- [ ] `DBConnection.java` has correct MySQL password
- [ ] MySQL JDBC JAR is in `WEB-INF/lib/`
- [ ] Tomcat is installed and configured
- [ ] Java files are compiled to `WEB-INF/classes/`

---

## Common Errors & Solutions

### Error: "MySQL JDBC Driver not found"
**Solution:** Add `mysql-connector-java-8.0.xx.jar` to `WEB-INF/lib/`

### Error: "Access denied for user 'root'@'localhost'"
**Solution:** Open `DBConnection.java` and fix your MySQL password

### Error: "Unknown database 'fakeurldb'"
**Solution:** Run the CREATE DATABASE SQL command in MySQL

### Error: "HTTP Status 404"
**Solution:** Check that project folder is in `webapps/` and URL path is correct

### Error: "HTTP Status 500 - ClassNotFoundException"
**Solution:** Java files are not compiled. Run javac command from Method 2.

---

## Quick Test After Running

1. Open `http://localhost:8080/FakeURLDetector/`
2. Enter URL: `http://login-verify-bank.com@192.168.1.1/something-very-long-and-suspicious`
3. Click "Check URL"
4. You should see: **Result: DANGEROUS** (score should be high)
5. Click "View Scan History" to see the stored record

