@echo off
echo ==========================================
echo   COMPILING STUDENT LEAVE MANAGEMENT
echo ==========================================

if not exist bin mkdir bin

echo Compiling Java files...
javac -cp "lib/*" -d bin src/main/*.java src/main/model/*.java src/main/util/*.java src/main/service/*.java

if %errorlevel% equ 0 (
    echo Compilation successful!
    echo.
    echo To run the application, execute:
    echo   run.bat
) else (
    echo Compilation failed!
    exit /b 1
)
