package com.distributeur.repository;

import com.distributeur.model.CoinInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CoinInventoryRepository extends JpaRepository<CoinInventory, Long> {
    
    Optional<CoinInventory> findByCoinValue(BigDecimal coinValue);
    
    @Query("SELECT c FROM CoinInventory c ORDER BY c.coinValue DESC")
    List<CoinInventory> findAllOrderByCoinValueDesc();
}
