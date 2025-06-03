# Test Simple - VendingPro API
# =============================

Write-Host "VendingPro API - Test Simple" -ForegroundColor Cyan
Write-Host "============================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080/api/vending"

# Fonction de test simple
function Test-API {
    param([string]$Name, [string]$Method, [string]$Url)
    
    Write-Host "Test: $Name..." -ForegroundColor Yellow -NoNewline
    
    try {
        $response = Invoke-RestMethod -Uri $Url -Method $Method
        if ($response.success) {
            Write-Host " SUCCES" -ForegroundColor Green
            return $response
        } else {
            Write-Host " ECHEC" -ForegroundColor Yellow
            return $null
        }
    }
    catch {
        Write-Host " ERREUR" -ForegroundColor Red
        return $null
    }
}

Write-Host "Tests de base..." -ForegroundColor Green
Write-Host ""

# Tests essentiels
$products = Test-API "Recuperation des produits" "GET" "$baseUrl/products"
Test-API "Pieces valides" "GET" "$baseUrl/valid-coins"
Test-API "Solde initial" "GET" "$baseUrl/balance"

Write-Host ""
Write-Host "Test d'insertion de pieces..." -ForegroundColor Green

# Test insertion de pieces
Test-API "Insertion 1 MAD" "POST" "$baseUrl/insert-coin?coinValue=1.0"
Test-API "Insertion 2 MAD" "POST" "$baseUrl/insert-coin?coinValue=2.0"
$balance = Test-API "Verification solde" "GET" "$baseUrl/balance"

Write-Host ""
Write-Host "Test d'achat..." -ForegroundColor Green

# Test achat si possible
if ($products -and $products.data -and $products.data.Count -gt 0) {
    $firstProduct = $products.data[0]
    Write-Host "   Tentative d'achat: $($firstProduct.name)" -ForegroundColor Cyan
    $purchase = Test-API "Achat produit" "POST" "$baseUrl/select-product/$($firstProduct.id)"
    
    if ($purchase -and $purchase.data) {
        Write-Host "   Prix: $($purchase.data.purchasedProduct.price) MAD" -ForegroundColor Green
        if ($purchase.data.changeAmount -gt 0) {
            Write-Host "   Monnaie: $($purchase.data.changeAmount) MAD" -ForegroundColor Yellow
        }
    }
} else {
    Test-API "Annulation transaction" "POST" "$baseUrl/cancel"
}

Write-Host ""
Write-Host "Tests de monitoring..." -ForegroundColor Green

Test-API "Statistiques" "GET" "$baseUrl/statistics"
Test-API "Etat de sante" "GET" "$baseUrl/health"
Test-API "Alertes" "GET" "$baseUrl/alerts"

Write-Host ""
Write-Host "Test simple termine!" -ForegroundColor Green
Write-Host ""

# Test manuel avec curl
Write-Host "Exemples de commandes curl:" -ForegroundColor Cyan
Write-Host "curl -X GET http://localhost:8080/api/vending/products" -ForegroundColor White
Write-Host "curl -X POST `"http://localhost:8080/api/vending/insert-coin?coinValue=1.0`"" -ForegroundColor White
Write-Host "curl -X POST http://localhost:8080/api/vending/select-product/1" -ForegroundColor White
