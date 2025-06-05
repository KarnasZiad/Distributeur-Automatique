@echo off
echo ========================================
echo   Compilation du Distributeur Automatique
echo ========================================
echo.

REM Définir JAVA_HOME
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

echo Configuration JAVA_HOME: %JAVA_HOME%
echo.

echo Compilation avec Maven...
.\mvnw.cmd clean compile

echo.
echo Compilation terminée !
pause
