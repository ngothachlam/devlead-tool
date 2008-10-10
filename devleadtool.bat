rem start "C:\Program Files\Java\jdk1.6.0_07\bin\javaw.exe" -classpath devlead-tool-20081009.jar com.jonas.agile.devleadtool.Main

for /f %%a IN ('dir /b lib *.jar') do set jars

pause

java -cp dist\devlead-tool-20081009.jar;lib\axis-1.4.jar;lib\cglib-nodep-2.1_3.jar;lib\commons-codec-1.3.jar com.jonas.agile.devleadtool.Main

pause
cls