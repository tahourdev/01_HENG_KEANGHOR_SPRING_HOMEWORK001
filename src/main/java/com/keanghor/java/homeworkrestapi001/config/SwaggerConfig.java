package com.keanghor.java.homeworkrestapi001.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ticketing System for Public Transport")
                        .version("1.0.0")
                        .description("The Ticketing System for Public Transport is a Spring Boot application for managing ticket bookings, payments, and scheduling."));
    }
}
