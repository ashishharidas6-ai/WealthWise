@echo off
echo ===================================
echo WealthWise Client Deletion Fix
echo ===================================
echo.

echo Compiling fix script...
javac -cp "src/main/java" FixDeletion.java

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo.
echo Running deletion fix...
echo.
java -cp ".;src/main/java" FixDeletion

echo.
echo ===================================
echo Fix completed!
echo ===================================
echo.
echo Next steps:
echo 1. Run your WealthWise application
echo 2. Login as admin (username: admin, password: admin123)
echo 3. Go to Clients view and test deletion
echo.
pause