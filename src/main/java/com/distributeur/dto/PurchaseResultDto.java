package com.distributeur.dto;

import com.distributeur.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class PurchaseResultDto {
    
    private Product purchasedProduct;
    private BigDecimal totalPaid;
    private BigDecimal changeAmount;
    private Map<BigDecimal, Integer> changeCoins;
    private List<String> changeDisplay;
    
    // Constructeurs
    public PurchaseResultDto() {}
    
    public PurchaseResultDto(Product purchasedProduct, BigDecimal totalPaid, 
                           BigDecimal changeAmount, Map<BigDecimal, Integer> changeCoins,
                           List<String> changeDisplay) {
        this.purchasedProduct = purchasedProduct;
        this.totalPaid = totalPaid;
        this.changeAmount = changeAmount;
        this.changeCoins = changeCoins;
        this.changeDisplay = changeDisplay;
    }
    
    // Getters et Setters
    public Product getPurchasedProduct() {
        return purchasedProduct;
    }
    
    public void setPurchasedProduct(Product purchasedProduct) {
        this.purchasedProduct = purchasedProduct;
    }
    
    public BigDecimal getTotalPaid() {
        return totalPaid;
    }
    
    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }
    
    public BigDecimal getChangeAmount() {
        return changeAmount;
    }
    
    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }
    
    public Map<BigDecimal, Integer> getChangeCoins() {
        return changeCoins;
    }
    
    public void setChangeCoins(Map<BigDecimal, Integer> changeCoins) {
        this.changeCoins = changeCoins;
    }
    
    public List<String> getChangeDisplay() {
        return changeDisplay;
    }
    
    public void setChangeDisplay(List<String> changeDisplay) {
        this.changeDisplay = changeDisplay;
    }
}
