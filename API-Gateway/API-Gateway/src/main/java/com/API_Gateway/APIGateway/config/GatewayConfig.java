package com.API_Gateway.APIGateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("product-service", route -> route
                        .path("/api/products", "/api/products/**")
                        .uri("http://localhost:8081"))

                .route("order-service", route -> route
                        .path("/api/orders", "/api/orders/**")
                        .uri("http://localhost:8082"))

                .route("payment-service", route -> route
                        .path("/api/payments", "/api/payments/**")
                        .uri("http://localhost:8083"))

                .build();
    }
}
