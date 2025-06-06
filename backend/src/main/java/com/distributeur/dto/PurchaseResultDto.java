package com.distributeur.dto;

import com.distributeur.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class PurchaseResultDto {
    private List<Product> purchasedProducts;
    private BigDecimal totalPaid;
    private BigDecimal changeAmount;
    private Map<BigDecimal, Integer> changeCoins;
    private List<String> changeDisplay;

    // Constructor for bulk purchase
    public PurchaseResultDto(List<Product> products, BigDecimal totalPaid, 
                           BigDecimal changeAmount, Map<BigDecimal, Integer> changeCoins,
                           List<String> changeDisplay) {
        this.purchasedProducts = products;
        this.totalPaid = totalPaid;
        this.changeAmount = changeAmount;
        this.changeCoins = changeCoins;
        this.changeDisplay = changeDisplay;
    }

    // Getters
    public List<Product> getPurchasedProducts() { return purchasedProducts; }
    public BigDecimal getTotalPaid() { return totalPaid; }
    public BigDecimal getChangeAmount() { return changeAmount; }
    public Map<BigDecimal, Integer> getChangeCoins() { return changeCoins; }
    public List<String> getChangeDisplay() { return changeDisplay; }
}
