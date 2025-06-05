# 🚀 Test Rapide - VendingPro API
# ===============================

Write-Host "🛒 VendingPro API - Test Rapide" -ForegroundColor Cyan
Write-Host "===============================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080/api/vending"

# Fonction de test simple
function Quick-Test {
    param([string]$Name, [string]$Method, [string]$Url)
    
    Write-Host "🔍 $Name..." -ForegroundColor Yellow -NoNewline
    
    try {
        $response = Invoke-RestMethod -Uri $Url -Method $Method
        if ($response.success) {
            Write-Host " ✅" -ForegroundColor Green
            return $response
        } else {
            Write-Host " ⚠️" -ForegroundColor Yellow
            return $null
        }
    }
    catch {
        Write-Host " ❌" -ForegroundColor Red
        return $null
    }
}

Write-Host "🚀 Tests de base..." -ForegroundColor Green
Write-Host ""

# Tests essentiels
$products = Quick-Test "Récupération des produits" "GET" "$baseUrl/products"
Quick-Test "Pièces valides" "GET" "$baseUrl/valid-coins"
Quick-Test "Solde initial" "GET" "$baseUrl/balance"

Write-Host ""
Write-Host "💰 Test d'insertion de pièces..." -ForegroundColor Green

# Test insertion de pièces
Quick-Test "Insertion 1 MAD" "POST" "$baseUrl/insert-coin?coinValue=1.0"
Quick-Test "Insertion 2 MAD" "POST" "$baseUrl/insert-coin?coinValue=2.0"
$balance = Quick-Test "Vérification solde" "GET" "$baseUrl/balance"

Write-Host ""
Write-Host "🛒 Test d'achat..." -ForegroundColor Green

# Test achat si possible
if ($products -and $products.data -and $products.data.Count -gt 0) {
    $firstProduct = $products.data[0]
    Write-Host "   🎯 Tentative d'achat: $($firstProduct.name)" -ForegroundColor Cyan
    $purchase = Quick-Test "Achat produit" "POST" "$baseUrl/select-product/$($firstProduct.id)"
    
    if ($purchase -and $purchase.data) {
        Write-Host "   💰 Prix: $($purchase.data.purchasedProduct.price) MAD" -ForegroundColor Green
        if ($purchase.data.changeAmount -gt 0) {
            Write-Host "   💸 Monnaie: $($purchase.data.changeAmount) MAD" -ForegroundColor Yellow
        }
    }
} else {
    Quick-Test "Annulation transaction" "POST" "$baseUrl/cancel"
}

Write-Host ""
Write-Host "📊 Tests de monitoring..." -ForegroundColor Green

Quick-Test "Statistiques" "GET" "$baseUrl/statistics"
Quick-Test "État de santé" "GET" "$baseUrl/health"
Quick-Test "Alertes" "GET" "$baseUrl/alerts"

Write-Host ""
Write-Host "🎉 Test rapide terminé!" -ForegroundColor Green
Write-Host ""
Write-Host "Pour des tests complets, executez: .\test-api.ps1" -ForegroundColor Cyan
