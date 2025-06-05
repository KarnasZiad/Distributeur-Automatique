package com.distributeur.service;

import com.distributeur.model.CoinInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChangeServiceTest {
    
    @Mock
    private CoinService coinService;
    
    @InjectMocks
    private ChangeService changeService;
    
    private List<CoinInventory> mockCoinInventory;
    
    @BeforeEach
    void setUp() {
        // Créer un inventaire de pièces de test
        mockCoinInventory = Arrays.asList(
            new CoinInventory(new BigDecimal("10.0"), 5),
            new CoinInventory(new BigDecimal("5.0"), 10),
            new CoinInventory(new BigDecimal("2.0"), 15),
            new CoinInventory(new BigDecimal("1.0"), 20),
            new CoinInventory(new BigDecimal("0.5"), 25)
        );
    }
    
    @Test
    void testCalculateOptimalChange_ExactChange() {
        when(coinService.getAllCoinsInventory()).thenReturn(mockCoinInventory);
        
        BigDecimal changeAmount = new BigDecimal("13.5");
        Map<BigDecimal, Integer> change = changeService.calculateOptimalChange(changeAmount);
        
        assertFalse(change.isEmpty());
        assertEquals(1, change.get(new BigDecimal("10.0")).intValue());
        assertEquals(1, change.get(new BigDecimal("2.0")).intValue());
        assertEquals(1, change.get(new BigDecimal("1.0")).intValue());
        assertEquals(1, change.get(new BigDecimal("0.5")).intValue());
        
        // Vérifier que le total est correct
        BigDecimal total = change.entrySet().stream()
            .map(entry -> entry.getKey().multiply(new BigDecimal(entry.getValue())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(0, changeAmount.compareTo(total));
    }
    
    @Test
    void testCalculateOptimalChange_ZeroAmount() {
        Map<BigDecimal, Integer> change = changeService.calculateOptimalChange(BigDecimal.ZERO);
        assertTrue(change.isEmpty());
    }
    
    @Test
    void testCalculateOptimalChange_NegativeAmount() {
        Map<BigDecimal, Integer> change = changeService.calculateOptimalChange(new BigDecimal("-5.0"));
        assertTrue(change.isEmpty());
    }
    
    @Test
    void testCalculateOptimalChange_InsufficientCoins() {
        // Créer un inventaire avec très peu de pièces
        List<CoinInventory> limitedInventory = Arrays.asList(
            new CoinInventory(new BigDecimal("10.0"), 0),
            new CoinInventory(new BigDecimal("5.0"), 1),
            new CoinInventory(new BigDecimal("2.0"), 0),
            new CoinInventory(new BigDecimal("1.0"), 0),
            new CoinInventory(new BigDecimal("0.5"), 0)
        );
        
        when(coinService.getAllCoinsInventory()).thenReturn(limitedInventory);
        
        BigDecimal changeAmount = new BigDecimal("7.5");
        Map<BigDecimal, Integer> change = changeService.calculateOptimalChange(changeAmount);
        
        // Ne peut pas rendre 7.5 avec seulement une pièce de 5.0
        assertTrue(change.isEmpty());
    }
    
    @Test
    void testCanMakeChange_Possible() {
        when(coinService.getAllCoinsInventory()).thenReturn(mockCoinInventory);
        
        assertTrue(changeService.canMakeChange(new BigDecimal("8.5")));
        assertTrue(changeService.canMakeChange(BigDecimal.ZERO));
    }
    
    @Test
    void testCanMakeChange_Impossible() {
        List<CoinInventory> limitedInventory = Arrays.asList(
            new CoinInventory(new BigDecimal("10.0"), 0),
            new CoinInventory(new BigDecimal("5.0"), 1),
            new CoinInventory(new BigDecimal("2.0"), 0),
            new CoinInventory(new BigDecimal("1.0"), 0),
            new CoinInventory(new BigDecimal("0.5"), 0)
        );
        
        when(coinService.getAllCoinsInventory()).thenReturn(limitedInventory);
        
        assertFalse(changeService.canMakeChange(new BigDecimal("7.5")));
    }
    
    @Test
    void testFormatChangeForDisplay() {
        Map<BigDecimal, Integer> change = Map.of(
            new BigDecimal("5.0"), 1,
            new BigDecimal("2.0"), 2,
            new BigDecimal("0.5"), 1
        );
        
        List<String> formatted = changeService.formatChangeForDisplay(change);
        
        assertEquals(3, formatted.size());
        assertTrue(formatted.contains("1 x 5.0 MAD"));
        assertTrue(formatted.contains("2 x 2.0 MAD"));
        assertTrue(formatted.contains("1 x 0.5 MAD"));
    }
}
