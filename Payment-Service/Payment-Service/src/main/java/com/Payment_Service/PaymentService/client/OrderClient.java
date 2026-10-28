package com.Payment_Service.PaymentService.client;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
@Component
public class OrderClient {
    private final WebClient webClient;

    public OrderClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8082")
                .build();
    }

    public OrderResponse getOrderById(Long orderId) {
        try {
            return webClient.get()
                    .uri("/api/orders/{id}", orderId)
                    .retrieve()
                    .bodyToMono(OrderResponse.class)
                    .block();

        } catch (WebClientResponseException.NotFound ex) {
            throw new ResourceNotFoundException(
                    "Order not found with ID: " + orderId);

        } catch (WebClientResponseException ex) {
            throw new IllegalStateException(
                    "Order Service returned HTTP "
                            + ex.getStatusCode().value());

        } catch (WebClientRequestException ex) {
            throw new IllegalStateException(
                    "Order Service is unavailable on port 8082");
        }
    }

}
