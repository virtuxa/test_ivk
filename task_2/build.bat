@echo off
echo Building Squares Web Service with Java 17...

REM Set Java 17 path
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

REM Check if Java 17 exists
if not exist "%JAVA_HOME%\bin\javac.exe" (
    echo ERROR: Java 17 not found at %JAVA_HOME%
    echo Please check the path or install Java 17
    exit /b 1
)

echo Using Java 17 from: %JAVA_HOME%

REM Clean previous build
if exist "build" rmdir /s /q build
mkdir build
mkdir build\classes

echo Compiling Java files...
"%JAVA_HOME%\bin\javac" -encoding UTF-8 -d build\classes -cp src\main\java src\main\java\com\ivk\squares\dto\*.java src\main\java\com\ivk\squares\service\*.java src\main\java\com\ivk\squares\controller\*.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    exit /b 1
)

echo Creating JAR file...
cd build\classes
"%JAVA_HOME%\bin\jar" cf ..\squares-web-service.jar com\ivk\squares\dto\*.class com\ivk\squares\service\*.class com\ivk\squares\controller\*.class
echo Main-Class: com.ivk.squares.controller.GameController > ..\MANIFEST.MF
"%JAVA_HOME%\bin\jar" cfm ..\squares-web-service.jar ..\MANIFEST.MF com\ivk\squares\dto\*.class com\ivk\squares\service\*.class com\ivk\squares\controller\*.class
cd ..\..

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Build successful!
    echo ========================================
    echo.
    echo To run the service:
    echo run.bat
    echo.
    echo The service will be available at: http://localhost:8080
) else (
    echo JAR creation failed!
    exit /b 1
)
