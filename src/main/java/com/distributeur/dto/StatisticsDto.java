package com.distributeur.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class StatisticsDto {
    
    // Statistiques générales
    private Long totalTransactions;
    private Long completedTransactions;
    private Long cancelledTransactions;
    private BigDecimal totalRevenue;
    private BigDecimal averageTransactionValue;
    
    // Statistiques des produits
    private Long totalProductsSold;
    private String mostPopularProduct;
    private String leastPopularProduct;
    private Map<String, Long> productSales;
    
    // Statistiques des pièces
    private BigDecimal totalCoinsInserted;
    private BigDecimal totalChangeGiven;
    private Map<BigDecimal, Integer> coinInventory;
    private Map<BigDecimal, Long> coinUsageStats;
    
    // Statistiques temporelles
    private LocalDateTime lastTransactionTime;
    private LocalDateTime systemStartTime;
    private Long uptimeMinutes;
    
    // État du système
    private String systemStatus;
    private Integer lowStockProducts;
    private Integer unavailableProducts;
    private Boolean changeAvailable;
    
    // Performance
    private Double averageResponseTime;
    private Long totalApiCalls;
    private Long errorCount;
    
    // Constructeurs
    public StatisticsDto() {}
    
    public StatisticsDto(Long totalTransactions, Long completedTransactions, 
                        Long cancelledTransactions, BigDecimal totalRevenue) {
        this.totalTransactions = totalTransactions;
        this.completedTransactions = completedTransactions;
        this.cancelledTransactions = cancelledTransactions;
        this.totalRevenue = totalRevenue;
    }
    
    // Getters et Setters
    public Long getTotalTransactions() {
        return totalTransactions;
    }
    
    public void setTotalTransactions(Long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }
    
    public Long getCompletedTransactions() {
        return completedTransactions;
    }
    
    public void setCompletedTransactions(Long completedTransactions) {
        this.completedTransactions = completedTransactions;
    }
    
    public Long getCancelledTransactions() {
        return cancelledTransactions;
    }
    
    public void setCancelledTransactions(Long cancelledTransactions) {
        this.cancelledTransactions = cancelledTransactions;
    }
    
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }
    
    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    
    public BigDecimal getAverageTransactionValue() {
        return averageTransactionValue;
    }
    
    public void setAverageTransactionValue(BigDecimal averageTransactionValue) {
        this.averageTransactionValue = averageTransactionValue;
    }
    
    public Long getTotalProductsSold() {
        return totalProductsSold;
    }
    
    public void setTotalProductsSold(Long totalProductsSold) {
        this.totalProductsSold = totalProductsSold;
    }
    
    public String getMostPopularProduct() {
        return mostPopularProduct;
    }
    
    public void setMostPopularProduct(String mostPopularProduct) {
        this.mostPopularProduct = mostPopularProduct;
    }
    
    public String getLeastPopularProduct() {
        return leastPopularProduct;
    }
    
    public void setLeastPopularProduct(String leastPopularProduct) {
        this.leastPopularProduct = leastPopularProduct;
    }
    
    public Map<String, Long> getProductSales() {
        return productSales;
    }
    
    public void setProductSales(Map<String, Long> productSales) {
        this.productSales = productSales;
    }
    
    public BigDecimal getTotalCoinsInserted() {
        return totalCoinsInserted;
    }
    
    public void setTotalCoinsInserted(BigDecimal totalCoinsInserted) {
        this.totalCoinsInserted = totalCoinsInserted;
    }
    
    public BigDecimal getTotalChangeGiven() {
        return totalChangeGiven;
    }
    
    public void setTotalChangeGiven(BigDecimal totalChangeGiven) {
        this.totalChangeGiven = totalChangeGiven;
    }
    
    public Map<BigDecimal, Integer> getCoinInventory() {
        return coinInventory;
    }
    
    public void setCoinInventory(Map<BigDecimal, Integer> coinInventory) {
        this.coinInventory = coinInventory;
    }
    
    public Map<BigDecimal, Long> getCoinUsageStats() {
        return coinUsageStats;
    }
    
    public void setCoinUsageStats(Map<BigDecimal, Long> coinUsageStats) {
        this.coinUsageStats = coinUsageStats;
    }
    
    public LocalDateTime getLastTransactionTime() {
        return lastTransactionTime;
    }
    
    public void setLastTransactionTime(LocalDateTime lastTransactionTime) {
        this.lastTransactionTime = lastTransactionTime;
    }
    
    public LocalDateTime getSystemStartTime() {
        return systemStartTime;
    }
    
    public void setSystemStartTime(LocalDateTime systemStartTime) {
        this.systemStartTime = systemStartTime;
    }
    
    public Long getUptimeMinutes() {
        return uptimeMinutes;
    }
    
    public void setUptimeMinutes(Long uptimeMinutes) {
        this.uptimeMinutes = uptimeMinutes;
    }
    
    public String getSystemStatus() {
        return systemStatus;
    }
    
    public void setSystemStatus(String systemStatus) {
        this.systemStatus = systemStatus;
    }
    
    public Integer getLowStockProducts() {
        return lowStockProducts;
    }
    
    public void setLowStockProducts(Integer lowStockProducts) {
        this.lowStockProducts = lowStockProducts;
    }
    
    public Integer getUnavailableProducts() {
        return unavailableProducts;
    }
    
    public void setUnavailableProducts(Integer unavailableProducts) {
        this.unavailableProducts = unavailableProducts;
    }
    
    public Boolean getChangeAvailable() {
        return changeAvailable;
    }
    
    public void setChangeAvailable(Boolean changeAvailable) {
        this.changeAvailable = changeAvailable;
    }
    
    public Double getAverageResponseTime() {
        return averageResponseTime;
    }
    
    public void setAverageResponseTime(Double averageResponseTime) {
        this.averageResponseTime = averageResponseTime;
    }
    
    public Long getTotalApiCalls() {
        return totalApiCalls;
    }
    
    public void setTotalApiCalls(Long totalApiCalls) {
        this.totalApiCalls = totalApiCalls;
    }
    
    public Long getErrorCount() {
        return errorCount;
    }
    
    public void setErrorCount(Long errorCount) {
        this.errorCount = errorCount;
    }
    
    // Méthodes utilitaires
    public Double getSuccessRate() {
        if (totalTransactions == null || totalTransactions == 0) {
            return 0.0;
        }
        return (completedTransactions.doubleValue() / totalTransactions.doubleValue()) * 100;
    }
    
    public Double getCancellationRate() {
        if (totalTransactions == null || totalTransactions == 0) {
            return 0.0;
        }
        return (cancelledTransactions.doubleValue() / totalTransactions.doubleValue()) * 100;
    }
}
