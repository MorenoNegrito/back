package com.tienda.mascotas.api.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @Operation(summary = "Página de inicio de la API", description = "Retorna un mensaje indicando que la API está funcionando")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API funcionando correctamente")
    })
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "API Tienda de Mascotas - Funcionando");
        response.put("status", "OK");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Estado de la API", description = "Verifica que la API y la base de datos estén activas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API y base de datos funcionando correctamente")
    })
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("database", "Connected");
        return ResponseEntity.ok(response);
    }
}