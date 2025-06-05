package com.distributeur.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "coin_inventory")
public class CoinInventory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, precision = 10, scale = 2, unique = true)
    private BigDecimal coinValue;
    
    @Column(nullable = false)
    private Integer quantity;
    
    // Constructeurs
    public CoinInventory() {}
    
    public CoinInventory(BigDecimal coinValue, Integer quantity) {
        this.coinValue = coinValue;
        this.quantity = quantity;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public BigDecimal getCoinValue() {
        return coinValue;
    }
    
    public void setCoinValue(BigDecimal coinValue) {
        this.coinValue = coinValue;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    // Méthodes utilitaires
    public void addCoins(int count) {
        this.quantity += count;
    }
    
    public boolean removeCoins(int count) {
        if (this.quantity >= count) {
            this.quantity -= count;
            return true;
        }
        return false;
    }
    
    public boolean hasEnoughCoins(int count) {
        return this.quantity >= count;
    }
    
    @Override
    public String toString() {
        return "CoinInventory{" +
                "id=" + id +
                ", coinValue=" + coinValue +
                ", quantity=" + quantity +
                '}';
    }
}
