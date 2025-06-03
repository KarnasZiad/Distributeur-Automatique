package com.distributeur.controller;

import com.distributeur.dto.ApiResponse;
import com.distributeur.dto.StatisticsDto;
import com.distributeur.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/vending")
@CrossOrigin(origins = "*")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<StatisticsDto>> getStatistics() {
        try {
            StatisticsDto stats = statisticsService.getOverallStatistics();
            return ResponseEntity.ok(ApiResponse.success("Statistiques récupérées", stats));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Erreur lors de la récupération des statistiques: " + e.getMessage()));
        }
    }

    @GetMapping("/statistics/daily")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDailyStatistics() {
        try {
            Map<String, Object> stats = statisticsService.getDailyStatistics();
            return ResponseEntity.ok(ApiResponse.success("Statistiques journalières récupérées", stats));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Erreur lors de la récupération des statistiques journalières: " + e.getMessage()));
        }
    }

    @GetMapping("/statistics/products")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProductStatistics() {
        try {
            Map<String, Object> stats = statisticsService.getProductStatistics();
            return ResponseEntity.ok(ApiResponse.success("Statistiques des produits récupérées", stats));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Erreur lors de la récupération des statistiques des produits: " + e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getHealthCheck() {
        try {
            Map<String, Object> health = statisticsService.getSystemHealth();
            return ResponseEntity.ok(ApiResponse.success("État du système", health));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Erreur lors de la vérification de l'état: " + e.getMessage()));
        }
    }

    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAlerts() {
        try {
            Map<String, Object> alerts = statisticsService.getSystemAlerts();
            return ResponseEntity.ok(ApiResponse.success("Alertes système", alerts));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Erreur lors de la récupération des alertes: " + e.getMessage()));
        }
    }

    @PostMapping("/diagnostic")
    public ResponseEntity<ApiResponse<Map<String, Object>>> runDiagnostic() {
        try {
            Map<String, Object> diagnostic = statisticsService.runSystemDiagnostic();
            return ResponseEntity.ok(ApiResponse.success("Diagnostic effectué", diagnostic));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Erreur lors du diagnostic: " + e.getMessage()));
        }
    }

    @GetMapping("/coin-inventory")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCoinInventory() {
        try {
            Map<String, Object> inventory = statisticsService.getCoinInventoryStatus();
            return ResponseEntity.ok(ApiResponse.success("Inventaire des pièces", inventory));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Erreur lors de la récupération de l'inventaire: " + e.getMessage()));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTransactionHistory(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String status) {
        try {
            Map<String, Object> history = statisticsService.getTransactionHistory(limit, status);
            return ResponseEntity.ok(ApiResponse.success("Historique des transactions", history));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Erreur lors de la récupération de l'historique: " + e.getMessage()));
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<ApiResponse<String>> resetSystem() {
        try {
            statisticsService.resetSystem();
            return ResponseEntity.ok(ApiResponse.success("Système réinitialisé", "OK"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Erreur lors de la réinitialisation: " + e.getMessage()));
        }
    }

    @GetMapping("/export")
    public ResponseEntity<ApiResponse<Map<String, Object>>> exportData(
            @RequestParam(defaultValue = "json") String format) {
        try {
            Map<String, Object> data = statisticsService.exportSystemData(format);
            return ResponseEntity.ok(ApiResponse.success("Données exportées", data));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Erreur lors de l'export: " + e.getMessage()));
        }
    }
}
