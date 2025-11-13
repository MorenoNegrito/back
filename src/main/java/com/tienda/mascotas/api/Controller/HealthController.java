package com.tienda.mascotas.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

package com.tienda.mascotas.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> home() {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "API Tienda de Mascotas - Funcionando");
        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        try (Connection conn = dataSource.getConnection()) {
            response.put("database", "Conectado");
            response.put("status", "OK");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("database", "Error: " + e.getMessage());
            response.put("status", "ERROR");
            return ResponseEntity.status(500).body(response);
        }
    }
}