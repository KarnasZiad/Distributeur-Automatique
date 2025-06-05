package com.distributeur.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transactions")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalInserted = BigDecimal.ZERO;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.IN_PROGRESS;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column
    private LocalDateTime completedAt;
    
    @ManyToOne
    @JoinColumn(name = "selected_product_id")
    private Product selectedProduct;
    
    @ElementCollection
    @CollectionTable(name = "transaction_coins", joinColumns = @JoinColumn(name = "transaction_id"))
    @Column(name = "coin_value", precision = 10, scale = 2)
    private List<BigDecimal> insertedCoins = new ArrayList<>();
    
    // Constructeurs
    public Transaction() {}
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
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
    
    // Méthodes utilitaires
    public void addCoin(BigDecimal coinValue) {
        insertedCoins.add(coinValue);
        totalInserted = totalInserted.add(coinValue);
    }
    
    public void complete() {
        this.status = TransactionStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }
    
    public void cancel() {
        this.status = TransactionStatus.CANCELLED;
        this.completedAt = LocalDateTime.now();
    }
    
    public boolean canPurchase(Product product) {
        return totalInserted.compareTo(product.getPrice()) >= 0;
    }
    
    public BigDecimal getChangeAmount() {
        if (selectedProduct != null) {
            return totalInserted.subtract(selectedProduct.getPrice());
        }
        return totalInserted;
    }
}
