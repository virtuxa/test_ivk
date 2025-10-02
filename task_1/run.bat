@echo off
echo Starting Squares Game...

REM Set Java 17 path
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

echo Running: java -jar build\squares-game.jar
echo.
java -jar build\squares-game.jar
