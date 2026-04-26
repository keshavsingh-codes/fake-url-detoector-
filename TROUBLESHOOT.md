# URL Not Working - Troubleshooting Guide

## Common Causes and Fixes

### CAUSE 1: Folder Name Has Spaces
Your folder is named `fake url detector` which contains spaces.
Tomcat does NOT handle spaces well in context paths.

**FIX:** Rename the project folder to `FakeURLDetector` (no spaces)

```
Before: C:/Users/Keshav Kumar/Desktop/fake url detector
After:  C:/Users/Keshav Kumar/Desktop/FakeURLDetector
```

Then redeploy to Tomcat's webapps folder.

---

### CAUSE 2: Project Not Deployed to Tomcat
The project must be placed in Tomcat's `webapps` folder.

**FIX - Step by Step:**

1. Find your Tomcat installation, for example:
   ```
   C:/apache-tomcat-9.0.xx/webapps/
   ```

2. Copy your project folder there:
   ```
   C:/apache-tomcat-9.0.xx/webapps/FakeURLDetector/
   ```

3. Inside `FakeURLDetector/` folder, the structure should be:
   ```
   FakeURLDetector/
   ├── index.jsp
   ├── result.jsp
   ├── history.jsp
   ├── WEB-INF/
   │   ├── web.xml
   │   ├── lib/
   │   │   └── mysql-connector-java-8.0.xx.jar
   │   └── classes/
   │       └── com/fakeurldetector/...
   ```

**IMPORTANT:** The `WebContent` folder should NOT be in the deployed path.
The contents of `WebContent` should be directly in `FakeURLDetector/`.

---

### CAUSE 3: Tomcat Not Running

**FIX:**
1. Open Command Prompt as Administrator
2. Navigate to Tomcat bin directory:
   ```cmd
   cd C:/apache-tomcat-9.0.xx/bin
   ```
3. Start Tomcat:
   ```cmd
   startup.bat
   ```
4. Check if Tomcat is running by visiting:
   ```
   http://localhost:8080/
   ```
   If you see Tomcat homepage, it's running.

---

### CAUSE 4: Java Files Not Compiled

**FIX:**

1. Open Command Prompt in your project folder:
   ```cmd
   cd "C:/Users/Keshav Kumar/Desktop/FakeURLDetector"
   ```

2. Create classes directory:
   ```cmd
   mkdir WebContent\WEB-INF\classes
   ```

3. Compile all Java files:
   ```cmd
   javac -d WebContent/WEB-INF/classes src/main/java/com/fakeurldetector/util/DBConnection.java src/main/java/com/fakeurldetector/model/URLDetector.java src/main/java/com/fakeurldetector/dao/URLDao.java src/main/java/com/fakeurldetector/servlet/CheckURLServlet.java
   ```

4. Verify compiled files exist:
   ```cmd
   dir WebContent\WEB-INF\classes\com\fakeurldetector\
   ```

---

### CAUSE 5: Incorrect URL Path

**Try these URLs one by one:**

```
http://localhost:8080/FakeURLDetector/
http://localhost:8080/FakeURLDetector/index.jsp
http://localhost:8080/fake%20url%20detector/
http://localhost:8080/fakeurl/
```

The exact URL depends on what you named the folder in webapps.

---

### CAUSE 6: Port Conflict (8080 in use)

**FIX:**

1. Check if port 8080 is free:
   ```cmd
   netstat -ano | findstr :8080
   ```

2. If something is using port 8080, change Tomcat port:
   - Open `C:/apache-tomcat-9.0.xx/conf/server.xml`
   - Find: `<Connector port="8080"`
   - Change to: `<Connector port="8081"`
   - Use: `http://localhost:8081/FakeURLDetector/`

---

### CAUSE 7: MySQL Connector Not Found

If you see `ClassNotFoundException: com.mysql.cj.jdbc.Driver`

**FIX:**

1. Download `mysql-connector-java-8.0.xx.jar`
2. Copy to: `FakeURLDetector/WEB-INF/lib/`
3. Also copy to: `C:/apache-tomcat-9.0.xx/lib/`
4. Restart Tomcat

---

## QUICK DIAGNOSTIC CHECKLIST

Check these in order:

1. **Is Tomcat running?**
   - Visit: `http://localhost:8080/`
   - Should see Tomcat welcome page

2. **Is project deployed correctly?**
   - Check: `C:/apache-tomcat-9.0.xx/webapps/FakeURLDetector/`
   - Should see index.jsp directly inside (NOT inside WebContent)

3. **Are Java files compiled?**
   - Check: `FakeURLDetector/WEB-INF/classes/com/fakeurldetector/`
   - Should see .class files

4. **Is MySQL connector present?**
   - Check: `FakeURLDetector/WEB-INF/lib/mysql-connector-java-8.0.xx.jar`

5. **Check Tomcat logs for errors:**
   - Open: `C:/apache-tomcat-9.0.xx/logs/catalina.out`
   - Or: `C:/apache-tomcat-9.0.xx/logs/localhost.log`

---

## CORRECT FOLDER STRUCTURE IN WEBAPPS

```
apache-tomcat-9.0.xx/
└── webapps/
    └── FakeURLDetector/          <-- Your app folder (NO spaces)
        ├── index.jsp             <-- Landing page
        ├── result.jsp            <-- Result display
        ├── history.jsp           <-- History page
        ├── README.md             <-- Documentation
        └── WEB-INF/
            ├── web.xml           <-- Servlet config
            ├── lib/
            │   └── mysql-connector-java-8.0.xx.jar
            └── classes/
                └── com/
                    └── fakeurldetector/
                        ├── util/
                        │   └── DBConnection.class
                        ├── model/
                        │   └── URLDetector.class
                        ├── dao/
                        │   └── URLDao.class
                        └── servlet/
                            └── CheckURLServlet.class
```

**NOTE:** The `WebContent` folder should NOT exist in the deployed app.
Only the CONTENTS of WebContent should be there.

---

## EASIEST WAY TO FIX (RECOMMENDED)

### Step 1: Create Proper WAR Structure

Create a new folder called `FakeURLDetector` on your Desktop:

```
Desktop/
└── FakeURLDetector/
    ├── index.jsp
    ├── result.jsp
    ├── history.jsp
    ├── README.md
    └── WEB-INF/
        ├── web.xml
        ├── lib/
        │   └── mysql-connector-java-8.0.xx.jar
        └── classes/
```

### Step 2: Copy Files

1. Copy `index.jsp`, `result.jsp`, `history.jsp` from `WebContent/`
2. Copy `WEB-INF/web.xml` from `WebContent/WEB-INF/`
3. Copy MySQL JAR to `WEB-INF/lib/`
4. Compile Java files to `WEB-INF/classes/`

### Step 3: Copy to Tomcat

Copy the entire `FakeURLDetector` folder to:
```
C:/apache-tomcat-9.0.xx/webapps/
```

### Step 4: Start Tomcat and Test

```cmd
C:/apache-tomcat-9.0.xx/bin/startup.bat
```

Then visit:
```
http://localhost:8080/FakeURLDetector/
```

