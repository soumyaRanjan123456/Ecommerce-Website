
package com.Order_Service.OrderService.client;

import com.Order_Service.OrderService.exception.ResourceNotFoundException;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class ProductClient {

    private final WebClient webClient;

    public ProductClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8081")
                .build();
    }

    public ProductResponse getProductById(Long productId) {
        try {
            ProductResponse product = webClient.get()
                    .uri("/api/products/{id}", productId)
                    .retrieve()
                    .bodyToMono(ProductResponse.class)
                    .block();

            if (product == null) {
                throw new IllegalStateException(
                        "Product Service returned an empty response");
            }

            return product;

        } catch (WebClientResponseException ex) {

            if (ex.getStatusCode().value() == 404) {
                throw new ResourceNotFoundException(
                        "Product not found with ID: " + productId);
            }

            throw new IllegalStateException(
                    "Product Service returned HTTP "
                            + ex.getStatusCode().value());

        } catch (WebClientRequestException ex) {

            throw new IllegalStateException(
                    "Cannot connect to Product Service on port 8081");
        }
    }
}
