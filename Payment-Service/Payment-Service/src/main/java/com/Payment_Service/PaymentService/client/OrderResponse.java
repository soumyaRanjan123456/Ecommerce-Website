package com.Payment_Service.PaymentService.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResponse {
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String status;
    private LocalDateTime createdAt;
}
