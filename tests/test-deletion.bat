@echo off
echo Compiling TestDeletion.java...
javac -cp "src/main/java" TestDeletion.java

echo.
echo Running deletion test...
java -cp ".;src/main/java" TestDeletion

echo.
echo Test completed. Press any key to exit.
pause