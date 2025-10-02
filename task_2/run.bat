@echo off
echo Starting Squares Web Service...

REM Set Java 17 path
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

echo Running: java -jar build\squares-web-service.jar
echo.
echo The service will be available at: http://localhost:8080
echo.
echo API endpoints:
echo POST /api/squares/nextMove
echo POST /api/squares/gameStatus
echo.
echo Press Ctrl+C to stop the service
echo.
java -jar build\squares-web-service.jar