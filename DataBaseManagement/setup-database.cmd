@echo off
echo Setting up WealthWise Database...
cd /d "D:\wealthwise backup"

echo.
echo Compiling database setup...
javac QuickDBSetup.java

echo.
echo Creating database...
java QuickDBSetup

echo.
echo Database setup complete!
echo.
echo The mazebank.db file has been created with sample data.
echo You can now run your WealthWise application.
echo.
pause