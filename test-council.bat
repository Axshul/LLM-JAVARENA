@echo off
echo Testing Council with all 10 models...
echo.

(
echo /models
echo /council What is 2+2?
echo exit
) | java -cp "target\classes;target\lib\*" org.example.App

pause