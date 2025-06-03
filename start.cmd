@echo off
echo ========================================
echo   Distributeur Automatique - API REST
echo ========================================
echo.

REM Définir JAVA_HOME
set JAVA_HOME=C:\Program Files\Java\jdk-17

echo Configuration JAVA_HOME: %JAVA_HOME%
echo.

echo Démarrage de l'application Spring Boot...
echo L'application sera accessible sur http://localhost:8080
echo.
echo Endpoints disponibles:
echo - GET  /api/vending/products        - Consulter les produits
echo - GET  /api/vending/valid-coins     - Pièces valides
echo - GET  /api/vending/balance         - Solde actuel
echo - POST /api/vending/insert-coin     - Insérer une pièce
echo - POST /api/vending/select-product  - Acheter un produit
echo - POST /api/vending/cancel          - Annuler la transaction
echo - GET  /api/vending/transaction-status - Statut transaction
echo.
echo Console H2: http://localhost:8080/h2-console
echo.

.\mvnw.cmd spring-boot:run
