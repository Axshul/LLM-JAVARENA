@echo off
cd /d "%~dp0"

REM Check if already compiled
if not exist "target\classes\org\example\App.class" (
    echo First time setup - building...
    call build.bat
    echo.
)

REM Run
java -cp "target\classes;target\lib\*" org.example.App
