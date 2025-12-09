@echo off
echo ========================================
echo   LLM-JAVARENA - Compiling...
echo ========================================
echo.

REM Create directories
if not exist "target\classes" mkdir target\classes
if not exist "target\lib" mkdir target\lib

echo Downloading dependencies...
echo.

REM Download Gson
if not exist "target\lib\gson-2.10.1.jar" (
    echo Downloading Gson...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar' -OutFile 'target\lib\gson-2.10.1.jar'"
)

REM Download Jansi
if not exist "target\lib\jansi-2.4.0.jar" (
    echo Downloading Jansi...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/fusesource/jansi/jansi/2.4.0/jansi-2.4.0.jar' -OutFile 'target\lib\jansi-2.4.0.jar'"
)

echo.
echo Compiling Java files...
javac -d target\classes -cp "target\lib\*" src\main\java\org\example\*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo   Starting LLM-JAVARENA...
echo ========================================
echo.

java -cp "target\classes;target\lib\*" org.example.App

pause
