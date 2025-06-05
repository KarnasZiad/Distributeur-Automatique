package com.distributeur.config;

import com.distributeur.model.CoinInventory;
import com.distributeur.model.Product;
import com.distributeur.repository.CoinInventoryRepository;
import com.distributeur.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CoinInventoryRepository coinInventoryRepository;
    
    @Override
    public void run(String... args) throws Exception {
        initializeProducts();
        initializeCoinInventory();
    }
    
    private void initializeProducts() {
        if (productRepository.count() == 0) {
            // Créer des produits de test
            productRepository.save(new Product("Coca-Cola", new BigDecimal("2.5"), 10, "Boisson gazeuse rafraîchissante"));
            productRepository.save(new Product("Eau Minérale", new BigDecimal("1.0"), 15, "Eau minérale naturelle"));
            productRepository.save(new Product("Chips", new BigDecimal("3.0"), 8, "Chips salées croustillantes"));
            productRepository.save(new Product("Chocolat", new BigDecimal("4.5"), 12, "Barre de chocolat au lait"));
            productRepository.save(new Product("Café", new BigDecimal("2.0"), 20, "Café expresso"));
            productRepository.save(new Product("Sandwich", new BigDecimal("6.0"), 5, "Sandwich jambon-fromage"));
            productRepository.save(new Product("Jus d'Orange", new BigDecimal("3.5"), 7, "Jus d'orange frais"));
            productRepository.save(new Product("Biscuits", new BigDecimal("2.5"), 9, "Biscuits aux pépites de chocolat"));
            
            System.out.println("Produits initialisés avec succès");
        }
    }
    
    private void initializeCoinInventory() {
        if (coinInventoryRepository.count() == 0) {
            // Initialiser l'inventaire des pièces avec des quantités de départ
            coinInventoryRepository.save(new CoinInventory(new BigDecimal("0.5"), 50));
            coinInventoryRepository.save(new CoinInventory(new BigDecimal("1.0"), 40));
            coinInventoryRepository.save(new CoinInventory(new BigDecimal("2.0"), 30));
            coinInventoryRepository.save(new CoinInventory(new BigDecimal("5.0"), 20));
            coinInventoryRepository.save(new CoinInventory(new BigDecimal("10.0"), 10));
            
            System.out.println("Inventaire des pièces initialisé avec succès");
        }
    }
}
