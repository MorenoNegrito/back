package com.tienda.mascotas.api.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Tienda de Mascotas")
                        .version("1.0.0")
                        .description("Documentación de la API REST para la gestión de productos, usuarios y pedidos en Tienda de Mascotas.")
                        .contact(new Contact()
                                .name("Evaluacion")
                        )
                );
    }
}