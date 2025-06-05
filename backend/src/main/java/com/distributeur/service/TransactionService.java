package com.distributeur.service;

import com.distributeur.model.Product;
import com.distributeur.model.Transaction;
import com.distributeur.model.TransactionStatus;
import com.distributeur.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private CoinService coinService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ChangeService changeService;
    
    public Transaction getCurrentTransaction() {
        Optional<Transaction> transaction = transactionRepository
            .findFirstByStatusOrderByCreatedAtDesc(TransactionStatus.IN_PROGRESS);
        
        if (transaction.isPresent()) {
            return transaction.get();
        } else {
            // Créer une nouvelle transaction
            Transaction newTransaction = new Transaction();
            return transactionRepository.save(newTransaction);
        }
    }
    
    public Transaction insertCoin(BigDecimal coinValue) {
        if (!coinService.isValidCoin(coinValue)) {
            throw new IllegalArgumentException("Pièce non valide: " + coinValue + " MAD");
        }
        
        Transaction transaction = getCurrentTransaction();
        transaction.addCoin(coinValue);
        coinService.addCoinToInventory(coinValue);
        
        return transactionRepository.save(transaction);
    }
    
    public Transaction selectProduct(Long productId) {
        Transaction transaction = getCurrentTransaction();
        
        if (transaction.getStatus() != TransactionStatus.IN_PROGRESS) {
            throw new IllegalStateException("La transaction n'est pas active");
        }

        Optional<Product> productOpt = productService.getProductById(productId);
        if (!productOpt.isPresent()) {
            throw new IllegalArgumentException("Produit non trouvé: " + productId);
        }
        
        Product product = productOpt.get();
        
        if (!product.isAvailable()) {
            throw new IllegalStateException("Le produit " + product.getName() + " n'est plus disponible");
        }
        
        if (!transaction.canPurchase(product)) {
            throw new IllegalStateException("Solde insuffisant pour " + product.getName() + 
                ". Solde actuel: " + transaction.getTotalInserted() + " MAD, Prix: " + 
                product.getPrice() + " MAD");
        }
        
        BigDecimal changeAmount = transaction.getTotalInserted().subtract(product.getPrice());
        if (changeAmount.compareTo(BigDecimal.ZERO) > 0 && !changeService.canMakeChange(changeAmount)) {
            throw new IllegalStateException("Impossible de rendre la monnaie exacte de " + changeAmount + " MAD");
        }
        
        // Effectuer l'achat
        if (!productService.purchaseProduct(productId)) {
            throw new IllegalStateException("Erreur lors de l'achat du produit");
        }
        transaction.setSelectedProduct(product);
        
        // Calculer et distribuer la monnaie
        Map<BigDecimal, Integer> change = changeService.calculateOptimalChange(changeAmount);
        changeService.dispenseChange(change);
        
        transaction.complete();
        return transactionRepository.save(transaction);
    }
    
    public Transaction cancelTransaction() {
        Transaction transaction = getCurrentTransaction();
        
        if (transaction.getStatus() != TransactionStatus.IN_PROGRESS) {
            throw new IllegalStateException("Aucune transaction en cours à annuler");
        }
        
        // Rendre toutes les pièces insérées
        BigDecimal totalToReturn = transaction.getTotalInserted();
        Map<BigDecimal, Integer> changeToReturn = changeService.calculateOptimalChange(totalToReturn);
        
        if (!changeService.canMakeChange(totalToReturn)) {
            throw new IllegalStateException("Impossible de rendre la monnaie pour l'annulation");
        }
        
        changeService.dispenseChange(changeToReturn);
        transaction.cancel();
        
        return transactionRepository.save(transaction);
    }
    
    public BigDecimal getCurrentBalance() {
        Transaction transaction = getCurrentTransaction();
        return transaction.getTotalInserted();
    }
}
