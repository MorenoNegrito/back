package com.tienda.mascotas.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactivar CSRF para permitir POST desde frontend
                .cors(cors -> cors.disable()) // CORS se maneja en CorsConfig
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Permitir todas las peticiones sin autenticación
                );

        return http.build();
    }
}