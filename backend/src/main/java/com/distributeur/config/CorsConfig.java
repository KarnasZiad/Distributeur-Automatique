package com.distributeur.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Autoriser uniquement notre frontend
        config.addAllowedOrigin("http://localhost:5173");
        
        // Autoriser les en-têtes standard
        config.addAllowedHeader("*");
        
        // Autoriser les méthodes HTTP nécessaires
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");
        
        // Autoriser les cookies
        config.setAllowCredentials(true);
        
        // Appliquer la configuration à tous les endpoints de l'API
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}
