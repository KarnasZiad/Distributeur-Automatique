# 🧪 Script de Test Complet - VendingPro API
# =====================================================

Write-Host "🛒 VendingPro API - Tests Complets" -ForegroundColor Cyan
Write-Host "====================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080/api/vending"
$testResults = @()

# Fonction pour tester un endpoint
function Test-Endpoint {
    param(
        [string]$Name,
        [string]$Method,
        [string]$Url,
        [hashtable]$Headers = @{},
        [string]$Body = $null
    )
    
    Write-Host "🔍 Test: $Name" -ForegroundColor Yellow
    Write-Host "   $Method $Url" -ForegroundColor Gray
    
    try {
        $params = @{
            Uri = $Url
            Method = $Method
            Headers = $Headers
        }
        
        if ($Body) {
            $params.Body = $Body
            $params.ContentType = "application/json"
        }
        
        $response = Invoke-RestMethod @params
        
        if ($response.success -eq $true) {
            Write-Host "   ✅ SUCCÈS" -ForegroundColor Green
            $script:testResults += @{Name=$Name; Status="SUCCÈS"; Response=$response}
        } else {
            Write-Host "   ⚠️  ÉCHEC: $($response.message)" -ForegroundColor Yellow
            $script:testResults += @{Name=$Name; Status="ÉCHEC"; Response=$response}
        }
        
        return $response
    }
    catch {
        Write-Host "   ❌ ERREUR: $($_.Exception.Message)" -ForegroundColor Red
        $script:testResults += @{Name=$Name; Status="ERREUR"; Error=$_.Exception.Message}
        return $null
    }
    
    Write-Host ""
}

# Fonction pour afficher une réponse
function Show-Response {
    param($Response, [string]$Title)
    
    if ($Response) {
        Write-Host "📊 $Title:" -ForegroundColor Magenta
        if ($Response.data) {
            $Response.data | ConvertTo-Json -Depth 3 | Write-Host -ForegroundColor White
        } else {
            $Response | ConvertTo-Json -Depth 2 | Write-Host -ForegroundColor White
        }
        Write-Host ""
    }
}

Write-Host "🚀 Démarrage des tests..." -ForegroundColor Green
Write-Host ""

# =====================================================
# 1. TESTS DE BASE
# =====================================================

Write-Host "📋 1. TESTS DE BASE" -ForegroundColor Cyan
Write-Host "==================" -ForegroundColor Cyan

# Test 1: Récupérer les produits
$products = Test-Endpoint -Name "Récupération des produits" -Method "GET" -Url "$baseUrl/products"
Show-Response $products "Produits disponibles"

# Test 2: Vérifier les pièces valides
$validCoins = Test-Endpoint -Name "Pièces valides" -Method "GET" -Url "$baseUrl/valid-coins"
Show-Response $validCoins "Pièces acceptées"

# Test 3: Consulter le solde initial
$balance = Test-Endpoint -Name "Solde initial" -Method "GET" -Url "$baseUrl/balance"
Show-Response $balance "Solde actuel"

# =====================================================
# 2. TESTS D'INSERTION DE PIÈCES
# =====================================================

Write-Host "💰 2. TESTS D'INSERTION DE PIÈCES" -ForegroundColor Cyan
Write-Host "=================================" -ForegroundColor Cyan

# Test des différentes pièces
$coins = @(0.5, 1.0, 2.0, 5.0, 10.0)
foreach ($coin in $coins) {
    $result = Test-Endpoint -Name "Insertion pièce $coin MAD" -Method "POST" -Url "$baseUrl/insert-coin?coinValue=$coin"
    if ($result) {
        Write-Host "   💰 Solde après insertion: $($result.data.totalInserted) MAD" -ForegroundColor Green
    }
}

# Test pièce invalide
Test-Endpoint -Name "Pièce invalide (3 MAD)" -Method "POST" -Url "$baseUrl/insert-coin?coinValue=3.0"

# Vérifier le solde après insertions
$newBalance = Test-Endpoint -Name "Solde après insertions" -Method "GET" -Url "$baseUrl/balance"
Show-Response $newBalance "Nouveau solde"

# =====================================================
# 3. TESTS D'ACHAT
# =====================================================

Write-Host "🛒 3. TESTS D'ACHAT" -ForegroundColor Cyan
Write-Host "==================" -ForegroundColor Cyan

# Essayer d'acheter le premier produit
if ($products -and $products.data -and $products.data.Count -gt 0) {
    $firstProduct = $products.data[0]
    Write-Host "🎯 Tentative d'achat: $($firstProduct.name) - $($firstProduct.price) MAD" -ForegroundColor Yellow
    
    $purchase = Test-Endpoint -Name "Achat produit $($firstProduct.name)" -Method "POST" -Url "$baseUrl/select-product/$($firstProduct.id)"
    Show-Response $purchase "Résultat d'achat"
}

# Essayer d'acheter un produit inexistant
Test-Endpoint -Name "Achat produit inexistant (ID 999)" -Method "POST" -Url "$baseUrl/select-product/999"

# =====================================================
# 4. TESTS DE TRANSACTION
# =====================================================

Write-Host "🔄 4. TESTS DE TRANSACTION" -ForegroundColor Cyan
Write-Host "=========================" -ForegroundColor Cyan

# Insérer quelques pièces pour les tests
Test-Endpoint -Name "Insertion 5 MAD pour test" -Method "POST" -Url "$baseUrl/insert-coin?coinValue=5.0" | Out-Null
Test-Endpoint -Name "Insertion 2 MAD pour test" -Method "POST" -Url "$baseUrl/insert-coin?coinValue=2.0" | Out-Null

# Vérifier le statut de transaction
$transactionStatus = Test-Endpoint -Name "Statut de transaction" -Method "GET" -Url "$baseUrl/transaction-status"
Show-Response $transactionStatus "Statut transaction"

# Annuler la transaction
$cancel = Test-Endpoint -Name "Annulation de transaction" -Method "POST" -Url "$baseUrl/cancel"
Show-Response $cancel "Résultat annulation"

# Vérifier le solde après annulation
Test-Endpoint -Name "Solde après annulation" -Method "GET" -Url "$baseUrl/balance"

# =====================================================
# 5. TESTS DE MONITORING
# =====================================================

Write-Host "📊 5. TESTS DE MONITORING" -ForegroundColor Cyan
Write-Host "=========================" -ForegroundColor Cyan

# Test des statistiques
$stats = Test-Endpoint -Name "Statistiques" -Method "GET" -Url "$baseUrl/statistics"
Show-Response $stats "Statistiques complètes"

# Test de santé
$health = Test-Endpoint -Name "État de santé" -Method "GET" -Url "$baseUrl/health"
Show-Response $health "État de santé"

# Test des alertes
$alerts = Test-Endpoint -Name "Alertes système" -Method "GET" -Url "$baseUrl/alerts"
Show-Response $alerts "Alertes"

# Test de diagnostic
$diagnostic = Test-Endpoint -Name "Diagnostic complet" -Method "POST" -Url "$baseUrl/diagnostic"
Show-Response $diagnostic "Rapport de diagnostic"

# =====================================================
# 6. TESTS DE STRESS
# =====================================================

Write-Host "⚡ 6. TESTS DE STRESS" -ForegroundColor Cyan
Write-Host "====================" -ForegroundColor Cyan

Write-Host "🔥 Test de stress: 10 insertions rapides" -ForegroundColor Yellow
for ($i = 1; $i -le 10; $i++) {
    $coin = $coins[(Get-Random -Maximum $coins.Length)]
    Write-Host "   Insertion $i/10: $coin MAD" -ForegroundColor Gray
    Test-Endpoint -Name "Stress test $i" -Method "POST" -Url "$baseUrl/insert-coin?coinValue=$coin" | Out-Null
}

# Annuler après le stress test
Test-Endpoint -Name "Nettoyage après stress test" -Method "POST" -Url "$baseUrl/cancel" | Out-Null

# =====================================================
# 7. RÉSUMÉ DES TESTS
# =====================================================

Write-Host "📈 RÉSUMÉ DES TESTS" -ForegroundColor Cyan
Write-Host "==================" -ForegroundColor Cyan

$successCount = ($testResults | Where-Object { $_.Status -eq "SUCCÈS" }).Count
$failureCount = ($testResults | Where-Object { $_.Status -eq "ÉCHEC" }).Count
$errorCount = ($testResults | Where-Object { $_.Status -eq "ERREUR" }).Count
$totalTests = $testResults.Count

Write-Host ""
Write-Host "📊 Résultats:" -ForegroundColor White
Write-Host "   ✅ Succès: $successCount" -ForegroundColor Green
Write-Host "   ⚠️  Échecs: $failureCount" -ForegroundColor Yellow
Write-Host "   ❌ Erreurs: $errorCount" -ForegroundColor Red
Write-Host "   📋 Total: $totalTests tests" -ForegroundColor Cyan

$successRate = [math]::Round(($successCount / $totalTests) * 100, 2)
Write-Host "   🎯 Taux de réussite: $successRate%" -ForegroundColor $(if($successRate -gt 80) {"Green"} elseif($successRate -gt 60) {"Yellow"} else {"Red"})

Write-Host ""

if ($errorCount -gt 0) {
    Write-Host "❌ ERREURS DÉTECTÉES:" -ForegroundColor Red
    $testResults | Where-Object { $_.Status -eq "ERREUR" } | ForEach-Object {
        Write-Host "   - $($_.Name): $($_.Error)" -ForegroundColor Red
    }
    Write-Host ""
}

if ($failureCount -gt 0) {
    Write-Host "⚠️  ÉCHECS DÉTECTÉS:" -ForegroundColor Yellow
    $testResults | Where-Object { $_.Status -eq "ÉCHEC" } | ForEach-Object {
        Write-Host "   - $($_.Name): $($_.Response.message)" -ForegroundColor Yellow
    }
    Write-Host ""
}

Write-Host "🎉 Tests terminés!" -ForegroundColor Green
Write-Host ""

# =====================================================
# 8. SCÉNARIO COMPLET D'UTILISATION
# =====================================================

Write-Host "🎬 SCÉNARIO COMPLET D'UTILISATION" -ForegroundColor Cyan
Write-Host "=================================" -ForegroundColor Cyan

Write-Host "📖 Simulation d'un achat complet..." -ForegroundColor Yellow
Write-Host ""

# Étape 1: Vérifier les produits disponibles
Write-Host "1️⃣ Consultation des produits..." -ForegroundColor White
$availableProducts = Test-Endpoint -Name "Produits pour scénario" -Method "GET" -Url "$baseUrl/products"

if ($availableProducts -and $availableProducts.data) {
    $targetProduct = $availableProducts.data[0]
    Write-Host "   🎯 Produit sélectionné: $($targetProduct.name) - $($targetProduct.price) MAD" -ForegroundColor Green

    # Étape 2: Insérer les pièces nécessaires
    Write-Host "2️⃣ Insertion des pièces..." -ForegroundColor White
    $targetPrice = [double]$targetProduct.price.Replace(',', '.')
    $insertedAmount = 0

    # Insérer des pièces jusqu'à avoir assez
    while ($insertedAmount -lt $targetPrice) {
        $coinToInsert = if (($targetPrice - $insertedAmount) -ge 5) { 5.0 }
                       elseif (($targetPrice - $insertedAmount) -ge 2) { 2.0 }
                       elseif (($targetPrice - $insertedAmount) -ge 1) { 1.0 }
                       else { 0.5 }

        $insertResult = Test-Endpoint -Name "Insertion $coinToInsert MAD" -Method "POST" -Url "$baseUrl/insert-coin?coinValue=$coinToInsert"
        if ($insertResult -and $insertResult.data) {
            $insertedAmount = [double]$insertResult.data.totalInserted.Replace(',', '.')
            Write-Host "   💰 Total inséré: $insertedAmount MAD" -ForegroundColor Green
        }
    }

    # Étape 3: Effectuer l'achat
    Write-Host "3️⃣ Achat du produit..." -ForegroundColor White
    $purchaseResult = Test-Endpoint -Name "Achat final" -Method "POST" -Url "$baseUrl/select-product/$($targetProduct.id)"

    if ($purchaseResult -and $purchaseResult.success) {
        Write-Host "   🎉 Achat réussi!" -ForegroundColor Green
        if ($purchaseResult.data.changeAmount -gt 0) {
            Write-Host "   💸 Monnaie rendue: $($purchaseResult.data.changeAmount) MAD" -ForegroundColor Yellow
        }
    }

    # Étape 4: Vérifier les statistiques finales
    Write-Host "4️⃣ Vérification des statistiques..." -ForegroundColor White
    $finalStats = Test-Endpoint -Name "Statistiques finales" -Method "GET" -Url "$baseUrl/statistics"
    if ($finalStats -and $finalStats.data) {
        Write-Host "   📊 Transactions totales: $($finalStats.data.totalTransactions)" -ForegroundColor Cyan
        Write-Host "   💰 Revenus totaux: $($finalStats.data.totalRevenue) MAD" -ForegroundColor Cyan
    }
}

Write-Host ""
Write-Host "🎉 Tests terminés!" -ForegroundColor Green
Write-Host ""
Write-Host "💡 Pour plus de détails, consultez:" -ForegroundColor Cyan
Write-Host "   - API Documentation: http://localhost:8080" -ForegroundColor White
Write-Host "   - Console H2: http://localhost:8080/h2-console" -ForegroundColor White
Write-Host "   - Logs application dans la console Spring Boot" -ForegroundColor White
