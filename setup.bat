@echo off
echo ==========================================
echo   Bansiwala Bhojnalay - First Time Setup
echo ==========================================
echo.

echo [1/4] Checking Java...
java -version 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed!
    echo Download Java 17+ from: https://adoptium.net/temurin/releases/
    echo Install it and re-run this script.
    pause
    exit /b 1
)
echo Java found!
echo.

echo [2/4] Checking Node.js...
node -v 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Node.js is not installed!
    echo Download Node.js 18+ from: https://nodejs.org/
    echo Install it and re-run this script.
    pause
    exit /b 1
)
echo Node.js found!
echo.

echo [3/4] Installing frontend dependencies...
cd frontend
call npm install
cd ..
echo Frontend dependencies installed!
echo.

echo [4/4] Building backend (downloading Maven dependencies)...
cd backend
call mvnw.cmd dependency:resolve -q
cd ..
echo Backend dependencies ready!
echo.

echo ==========================================
echo   Setup Complete! 
echo   Run start-backend.bat in one terminal
echo   Run start-frontend.bat in another terminal
echo   Open http://localhost:4200 in browser
echo ==========================================
pause
