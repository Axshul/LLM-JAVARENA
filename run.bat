@echo off
cd /d "%~dp0"
java -cp "target\classes;target\lib\*" org.example.App
