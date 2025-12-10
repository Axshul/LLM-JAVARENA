@echo off
echo ========================================
echo   Testing Gemini API Keys
echo ========================================
echo.

REM Compile test program
echo Compiling test program...
javac -d target\classes -cp "target\lib\*" TestGeminiKeys.java

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo.
echo Running tests...
echo.
java -cp "target\classes;target\lib\*" TestGeminiKeys

pause
