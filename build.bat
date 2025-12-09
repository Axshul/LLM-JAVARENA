@echo off
echo Building LLM Council Arena...
echo.

REM Create directories
if not exist "target\classes" mkdir target\classes
if not exist "target\lib" mkdir target\lib

REM Download dependencies if needed
if not exist "target\lib\gson-2.10.1.jar" (
    echo Downloading Gson...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar' -OutFile 'target\lib\gson-2.10.1.jar'"
)

REM Compile
echo Compiling...
javac -d target\classes -cp "target\lib\*" src\main\java\org\example\*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [OK] Build complete! Run with: run.bat
) else (
    echo.
    echo [ERROR] Build failed!
)
pause
