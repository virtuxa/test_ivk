@echo off
echo Building Squares Game with Java 17...

REM Set Java 17 path
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

REM Check if Java 17 exists
if not exist "%JAVA_HOME%\bin\javac.exe" (
    echo ERROR: Java 17 not found at %JAVA_HOME%
    echo Please check the path or install Java 17
    pause
    exit /b 1
)

echo Using Java 17 from: %JAVA_HOME%

REM Clean previous build
if exist "build" rmdir /s /q build
mkdir build
mkdir build\classes

echo Compiling Java files...
"%JAVA_HOME%\bin\javac" -encoding UTF-8 -d build\classes -cp src\main\java src\main\java\com\ivk\squares\model\*.java src\main\java\com\ivk\squares\ai\*.java src\main\java\com\ivk\squares\game\*.java src\main\java\com\ivk\squares\ConsoleGame.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Creating JAR file...
cd build\classes
"%JAVA_HOME%\bin\jar" cfe ..\squares-game.jar com.ivk.squares.ConsoleGame com\ivk\squares\model\*.class com\ivk\squares\ai\*.class com\ivk\squares\game\*.class com\ivk\squares\ConsoleGame.class
if %ERRORLEVEL% NEQ 0 (
    echo Trying alternative JAR creation...
    "%JAVA_HOME%\bin\jar" cf ..\squares-game.jar -C . .
    echo Main-Class: com.ivk.squares.ConsoleGame > ..\MANIFEST.MF
    "%JAVA_HOME%\bin\jar" cfm ..\squares-game.jar ..\MANIFEST.MF -C . .
)
cd ..\..

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Build successful!
    echo ========================================
    echo.
    echo Example commands:
    echo GAME 5 user,W comp,B
    echo MOVE 2 3
    echo HELP
    echo EXIT
) else (
    echo JAR creation failed!
)