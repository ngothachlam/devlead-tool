@echo off
echo Use by placing a shortcut to this bat file and run!
echo.
echo This script automatically finds all jars in the lib folder, adds them to the classpath and runs the
echo devlead-tool.jar in the dist directory using current java command (java_home).
echo.
echo Author: Jonas Olofsson
echo.
echo Lets go!!
echo.

SETLOCAL ENABLEDELAYEDEXPANSION
set FILES=
for /f %%a IN ('dir /b lib *.jar') do set FILES=!FILES!;lib\%%a
   
@echo on
java -cp dist\devlead-tool.jar%FILES% com.jonas.agile.devleadtool.Main properties/log4j-prod.properties

pause
