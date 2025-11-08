@echo off
echo ========================================
echo   BAAHUBALI Bank Management System
echo   Starting Application...
echo ========================================
echo.

echo Building application...
call mvn clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Build failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo   Build Successful!
echo   Starting Spring Boot Application...
echo ========================================
echo.
echo Access the application at:
echo   http://localhost:8080
echo.
echo H2 Database Console:
echo   http://localhost:8080/h2-console
echo   JDBC URL: jdbc:h2:file:./data/baahubali-bank
echo   Username: sa
echo   Password: (leave empty)
echo.
echo Test Accounts:
echo   Admin:    admin@baahubali.com / admin123
echo   Employee: employee@baahubali.com / employee123
echo   Customer: alice@baahubali.com / password
echo   Customer: bob@baahubali.com / password
echo.
echo Press Ctrl+C to stop the application
echo ========================================
echo.

call mvn spring-boot:run