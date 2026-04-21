@echo off
echo ==========================================
echo   Starting Frontend (Angular)...
echo   Open http://localhost:4200 in browser
echo   Press Ctrl+C to stop
echo ==========================================
cd frontend
call npx ng serve --open
