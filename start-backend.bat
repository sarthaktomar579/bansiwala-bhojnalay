@echo off
echo ==========================================
echo   Starting Backend (Spring Boot)...
echo   Press Ctrl+C to stop
echo ==========================================
cd backend
call mvnw.cmd spring-boot:run
