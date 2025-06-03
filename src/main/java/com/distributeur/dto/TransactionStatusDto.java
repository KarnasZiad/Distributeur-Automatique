package com.distributeur.dto;

import com.distributeur.model.Product;
import com.distributeur.model.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionStatusDto {
    
    private Long transactionId;
    private BigDecimal totalInserted;
    private TransactionStatus status;
    private LocalDateTime createdAt;
    private Product selectedProduct;
    private List<BigDecimal> insertedCoins;
    private BigDecimal changeAmount;
    
    // Constructeurs
    public TransactionStatusDto() {}
    
    // Getters et Setters
    public Long getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
    
    public BigDecimal getTotalInserted() {
        return totalInserted;
    }
    
    public void setTotalInserted(BigDecimal totalInserted) {
        this.totalInserted = totalInserted;
    }
    
    public TransactionStatus getStatus() {
        return status;
    }
    
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public Product getSelectedProduct() {
        return selectedProduct;
    }
    
    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }
    
    public List<BigDecimal> getInsertedCoins() {
        return insertedCoins;
    }
    
    public void setInsertedCoins(List<BigDecimal> insertedCoins) {
        this.insertedCoins = insertedCoins;
    }
    
    public BigDecimal getChangeAmount() {
        return changeAmount;
    }
    
    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }
}
