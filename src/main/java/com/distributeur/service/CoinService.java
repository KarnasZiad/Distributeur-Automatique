package com.distributeur.service;

import com.distributeur.model.CoinInventory;
import com.distributeur.repository.CoinInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CoinService {
    
    private static final List<BigDecimal> VALID_COINS = Arrays.asList(
        new BigDecimal("0.5"),
        new BigDecimal("1.0"),
        new BigDecimal("2.0"),
        new BigDecimal("5.0"),
        new BigDecimal("10.0")
    );
    
    @Autowired
    private CoinInventoryRepository coinInventoryRepository;
    
    public boolean isValidCoin(BigDecimal coinValue) {
        return VALID_COINS.contains(coinValue);
    }
    
    public List<BigDecimal> getValidCoins() {
        return VALID_COINS;
    }
    
    public void addCoinToInventory(BigDecimal coinValue) {
        Optional<CoinInventory> existingCoin = coinInventoryRepository.findByCoinValue(coinValue);
        
        if (existingCoin.isPresent()) {
            CoinInventory coin = existingCoin.get();
            coin.addCoins(1);
            coinInventoryRepository.save(coin);
        } else {
            CoinInventory newCoin = new CoinInventory(coinValue, 1);
            coinInventoryRepository.save(newCoin);
        }
    }
    
    public boolean removeCoinFromInventory(BigDecimal coinValue, int quantity) {
        Optional<CoinInventory> coinOpt = coinInventoryRepository.findByCoinValue(coinValue);
        
        if (coinOpt.isPresent()) {
            CoinInventory coin = coinOpt.get();
            if (coin.removeCoins(quantity)) {
                coinInventoryRepository.save(coin);
                return true;
            }
        }
        return false;
    }
    
    public int getCoinQuantity(BigDecimal coinValue) {
        Optional<CoinInventory> coinOpt = coinInventoryRepository.findByCoinValue(coinValue);
        return coinOpt.map(CoinInventory::getQuantity).orElse(0);
    }
    
    public List<CoinInventory> getAllCoinsInventory() {
        return coinInventoryRepository.findAllOrderByCoinValueDesc();
    }
}
