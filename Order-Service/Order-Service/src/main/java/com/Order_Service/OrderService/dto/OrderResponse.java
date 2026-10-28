package com.Order_Service.OrderService.dto;

import com.Order_Service.OrderService.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private OrderStatus status;
    private LocalDateTime createdAt;
}
