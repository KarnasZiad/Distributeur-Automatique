package com.distributeur.service;

import com.distributeur.model.CoinInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChangeService {
    
    @Autowired
    private CoinService coinService;
    
    public Map<BigDecimal, Integer> calculateOptimalChange(BigDecimal changeAmount) {
        Map<BigDecimal, Integer> change = new HashMap<>();
        
        if (changeAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return change;
        }
        
        List<CoinInventory> availableCoins = coinService.getAllCoinsInventory();
        BigDecimal remainingAmount = changeAmount;
        
        // Algorithme glouton : utiliser les plus grosses pièces en premier
        for (CoinInventory coinInventory : availableCoins) {
            BigDecimal coinValue = coinInventory.getCoinValue();
            int availableQuantity = coinInventory.getQuantity();
            
            if (remainingAmount.compareTo(coinValue) >= 0 && availableQuantity > 0) {
                // Calculer combien de pièces de cette valeur on peut utiliser
                int neededCoins = remainingAmount.divide(coinValue).intValue();
                int coinsToUse = Math.min(neededCoins, availableQuantity);
                
                if (coinsToUse > 0) {
                    change.put(coinValue, coinsToUse);
                    BigDecimal usedAmount = coinValue.multiply(new BigDecimal(coinsToUse));
                    remainingAmount = remainingAmount.subtract(usedAmount);
                }
            }
        }
        
        // Vérifier si on peut rendre la monnaie exacte
        if (remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
            // Impossible de rendre la monnaie exacte
            return new HashMap<>();
        }
        
        return change;
    }
    
    public boolean canMakeChange(BigDecimal changeAmount) {
        Map<BigDecimal, Integer> change = calculateOptimalChange(changeAmount);
        return !change.isEmpty() || changeAmount.compareTo(BigDecimal.ZERO) == 0;
    }
    
    public void dispenseChange(Map<BigDecimal, Integer> change) {
        for (Map.Entry<BigDecimal, Integer> entry : change.entrySet()) {
            BigDecimal coinValue = entry.getKey();
            Integer quantity = entry.getValue();
            coinService.removeCoinFromInventory(coinValue, quantity);
        }
    }
    
    public List<String> formatChangeForDisplay(Map<BigDecimal, Integer> change) {
        List<String> formattedChange = new ArrayList<>();
        
        for (Map.Entry<BigDecimal, Integer> entry : change.entrySet()) {
            BigDecimal coinValue = entry.getKey();
            Integer quantity = entry.getValue();
            formattedChange.add(quantity + " x " + coinValue + " MAD");
        }
        
        return formattedChange;
    }
}
