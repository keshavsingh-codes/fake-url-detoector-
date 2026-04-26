@echo off
echo Setting JAVA_HOME and starting Tomcat...
set JAVA_HOME=C:\Program Files\Java\jdk-26.0.1
set JRE_HOME=C:\Program Files\Java\jdk-26.0.1
set CATALINA_HOME=C:\Users\Keshav Kumar\Desktop\apache-tomcat-9.0.98
cd /d "%CATALINA_HOME%\bin"
call startup.bat
echo Tomcat started! Open http://localhost:8080/FakeURLDetector/
pause

