@echo off
echo Creating mazebank.db database...
cd /d "D:\wealthwise backup"

echo Compiling database creation class...
javac -cp "." CreateMazeBankDB.java

echo Running database creation...
java -cp "." CreateMazeBankDB

echo.
echo Database creation complete!
echo You can now run your WealthWise application.
pause