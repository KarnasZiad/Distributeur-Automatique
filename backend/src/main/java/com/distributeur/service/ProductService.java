package com.distributeur.service;

import com.distributeur.model.Product;
import com.distributeur.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public List<Product> getAvailableProducts() {
        return productRepository.findAvailableProducts();
    }
    
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    public boolean isProductAvailable(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        return product.isPresent() && product.get().isAvailable();
    }
    
    public boolean purchaseProduct(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            if (product.isAvailable()) {
                product.decrementQuantity();
                productRepository.save(product);
                return true;
            }
        }
        return false;
    }
    
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
