package com.distributeur.service;

import com.distributeur.model.CoinInventory;
import com.distributeur.repository.CoinInventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoinServiceTest {
    
    @Mock
    private CoinInventoryRepository coinInventoryRepository;
    
    @InjectMocks
    private CoinService coinService;
    
    @BeforeEach
    void setUp() {
        // Configuration initiale si nécessaire
    }
    
    @Test
    void testIsValidCoin_ValidCoins() {
        assertTrue(coinService.isValidCoin(new BigDecimal("0.5")));
        assertTrue(coinService.isValidCoin(new BigDecimal("1.0")));
        assertTrue(coinService.isValidCoin(new BigDecimal("2.0")));
        assertTrue(coinService.isValidCoin(new BigDecimal("5.0")));
        assertTrue(coinService.isValidCoin(new BigDecimal("10.0")));
    }
    
    @Test
    void testIsValidCoin_InvalidCoins() {
        assertFalse(coinService.isValidCoin(new BigDecimal("0.25")));
        assertFalse(coinService.isValidCoin(new BigDecimal("3.0")));
        assertFalse(coinService.isValidCoin(new BigDecimal("20.0")));
        assertFalse(coinService.isValidCoin(new BigDecimal("0.1")));
    }
    
    @Test
    void testAddCoinToInventory_NewCoin() {
        BigDecimal coinValue = new BigDecimal("1.0");
        when(coinInventoryRepository.findByCoinValue(coinValue)).thenReturn(Optional.empty());
        
        coinService.addCoinToInventory(coinValue);
        
        verify(coinInventoryRepository).save(any(CoinInventory.class));
    }
    
    @Test
    void testAddCoinToInventory_ExistingCoin() {
        BigDecimal coinValue = new BigDecimal("1.0");
        CoinInventory existingCoin = new CoinInventory(coinValue, 5);
        when(coinInventoryRepository.findByCoinValue(coinValue)).thenReturn(Optional.of(existingCoin));
        
        coinService.addCoinToInventory(coinValue);
        
        assertEquals(6, existingCoin.getQuantity());
        verify(coinInventoryRepository).save(existingCoin);
    }
    
    @Test
    void testRemoveCoinFromInventory_Success() {
        BigDecimal coinValue = new BigDecimal("2.0");
        CoinInventory coin = new CoinInventory(coinValue, 10);
        when(coinInventoryRepository.findByCoinValue(coinValue)).thenReturn(Optional.of(coin));
        
        boolean result = coinService.removeCoinFromInventory(coinValue, 3);
        
        assertTrue(result);
        assertEquals(7, coin.getQuantity());
        verify(coinInventoryRepository).save(coin);
    }
    
    @Test
    void testRemoveCoinFromInventory_InsufficientQuantity() {
        BigDecimal coinValue = new BigDecimal("2.0");
        CoinInventory coin = new CoinInventory(coinValue, 2);
        when(coinInventoryRepository.findByCoinValue(coinValue)).thenReturn(Optional.of(coin));
        
        boolean result = coinService.removeCoinFromInventory(coinValue, 5);
        
        assertFalse(result);
        assertEquals(2, coin.getQuantity()); // Quantité inchangée
        verify(coinInventoryRepository, never()).save(coin);
    }
    
    @Test
    void testGetCoinQuantity_ExistingCoin() {
        BigDecimal coinValue = new BigDecimal("5.0");
        CoinInventory coin = new CoinInventory(coinValue, 15);
        when(coinInventoryRepository.findByCoinValue(coinValue)).thenReturn(Optional.of(coin));
        
        int quantity = coinService.getCoinQuantity(coinValue);
        
        assertEquals(15, quantity);
    }
    
    @Test
    void testGetCoinQuantity_NonExistingCoin() {
        BigDecimal coinValue = new BigDecimal("5.0");
        when(coinInventoryRepository.findByCoinValue(coinValue)).thenReturn(Optional.empty());
        
        int quantity = coinService.getCoinQuantity(coinValue);
        
        assertEquals(0, quantity);
    }
}
