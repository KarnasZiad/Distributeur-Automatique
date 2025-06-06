package com.distributeur.controller;

import com.distributeur.dto.ApiResponse;
import com.distributeur.dto.PurchaseRequest;
import com.distributeur.dto.PurchaseResultDto;
import com.distributeur.dto.TransactionStatusDto;
import com.distributeur.model.Product;
import com.distributeur.model.Transaction;
import com.distributeur.service.ChangeService;
import com.distributeur.service.CoinService;
import com.distributeur.service.ProductService;
import com.distributeur.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vending")
public class VendingMachineController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private ChangeService changeService;

    @PostMapping("/insert-coin")
    public ResponseEntity<ApiResponse<TransactionStatusDto>> insertCoin(@RequestParam BigDecimal coinValue) {
        try {
            Transaction transaction = transactionService.insertCoin(coinValue);
            TransactionStatusDto statusDto = convertToStatusDto(transaction);

            return ResponseEntity.ok(ApiResponse.success(
                "Pièce de " + coinValue + " MAD insérée avec succès", statusDto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Pièce non valide: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de l'insertion de la pièce: " + e.getMessage()));
        }
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<Product>>> getAvailableProducts() {
        try {
            List<Product> products = productService.getAvailableProducts();
            return ResponseEntity.ok(ApiResponse.success("Produits disponibles récupérés", products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la récupération des produits: " + e.getMessage()));
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<ApiResponse<BigDecimal>> getCurrentBalance() {
        try {
            BigDecimal balance = transactionService.getCurrentBalance();
            return ResponseEntity.ok(ApiResponse.success("Solde actuel", balance));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la récupération du solde: " + e.getMessage()));
        }
    }

    @PostMapping("/select-product/{productId}")
    public ResponseEntity<ApiResponse<PurchaseResultDto>> selectProduct(@PathVariable Long productId) {
        try {
            Transaction transaction = transactionService.selectProduct(productId);

            BigDecimal changeAmount = transaction.getChangeAmount();
            Map<BigDecimal, Integer> changeCoins = changeService.calculateOptimalChange(changeAmount);
            List<String> changeDisplay = changeService.formatChangeForDisplay(changeCoins);            List<Product> productList = List.of(transaction.getSelectedProduct());
            PurchaseResultDto result = new PurchaseResultDto(
                productList,
                transaction.getTotalInserted(),
                changeAmount,
                changeCoins,
                changeDisplay
            );

            return ResponseEntity.ok(ApiResponse.success("Produit acheté avec succès", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Produit invalide: " + e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de l'achat: " + e.getMessage()));
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse<Map<BigDecimal, Integer>>> cancelTransaction() {
        try {
            Transaction transaction = transactionService.cancelTransaction();
            BigDecimal refundAmount = transaction.getTotalInserted();
            Map<BigDecimal, Integer> refundCoins = changeService.calculateOptimalChange(refundAmount);

            return ResponseEntity.ok(ApiResponse.success("Transaction annulée, monnaie rendue", refundCoins));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de l'annulation: " + e.getMessage()));
        }
    }

    @GetMapping("/transaction-status")
    public ResponseEntity<ApiResponse<TransactionStatusDto>> getTransactionStatus() {
        try {
            Transaction transaction = transactionService.getCurrentTransaction();
            TransactionStatusDto statusDto = convertToStatusDto(transaction);

            return ResponseEntity.ok(ApiResponse.success("Statut de la transaction", statusDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la récupération du statut: " + e.getMessage()));
        }
    }

    @GetMapping("/valid-coins")
    public ResponseEntity<ApiResponse<List<BigDecimal>>> getValidCoins() {
        try {
            List<BigDecimal> validCoins = coinService.getValidCoins();
            return ResponseEntity.ok(ApiResponse.success("Pièces valides", validCoins));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la récupération des pièces valides: " + e.getMessage()));
        }
    }

    @PostMapping("/bulk-purchase")
    public ResponseEntity<ApiResponse<PurchaseResultDto>> bulkPurchase(@RequestBody List<PurchaseRequest> purchases) {
        try {            List<Product> selectedProducts = new ArrayList<>();

            // Vérifier tous les produits
            for (PurchaseRequest purchase : purchases) {
                productService.getProductById(Long.valueOf(purchase.getProductId()))
                    .ifPresent(selectedProducts::add);
            }

            if (selectedProducts.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Aucun produit valide trouvé"));
            }            // Calculer le coût total
            BigDecimal totalCost = selectedProducts.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Vérifier le solde
            BigDecimal balance = transactionService.getCurrentBalance();
            if (balance.compareTo(totalCost) < 0) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Solde insuffisant pour l'achat total"));
            }

            // Effectuer la transaction
            Transaction transaction = transactionService.processMultipleProducts(selectedProducts);

            // Préparer le résultat
            PurchaseResultDto result = new PurchaseResultDto(
                selectedProducts,
                transaction.getTotalInserted(),
                transaction.getChangeAmount(),
                changeService.calculateOptimalChange(transaction.getChangeAmount()),
                changeService.formatChangeForDisplay(changeService.calculateOptimalChange(transaction.getChangeAmount()))
            );

            return ResponseEntity.ok(ApiResponse.success("Achats effectués avec succès", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    private TransactionStatusDto convertToStatusDto(Transaction transaction) {
        TransactionStatusDto dto = new TransactionStatusDto();
        dto.setTransactionId(transaction.getId());
        dto.setTotalInserted(transaction.getTotalInserted());
        dto.setStatus(transaction.getStatus());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setSelectedProduct(transaction.getSelectedProduct());
        dto.setInsertedCoins(transaction.getInsertedCoins());
        dto.setChangeAmount(transaction.getChangeAmount());
        return dto;
    }
}
