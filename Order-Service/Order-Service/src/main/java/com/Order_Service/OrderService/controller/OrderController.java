package com.Order_Service.OrderService.controller;
import com.Order_Service.OrderService.dto.OrderRequest;
import com.Order_Service.OrderService.dto.OrderResponse;
import com.Order_Service.OrderService.service.OrderService;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private static final Logger log =
            LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create Order
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request) {

        log.info("Received request to create order");

        OrderResponse response = orderService.createOrder(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    // Get All Orders
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {

        log.info("Received request to fetch all orders");

        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // Get Order By ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id) {

        log.info("Received request to fetch order ID: {}", id);

        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // Cancel Order
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id) {

        log.info("Received request to cancel order ID: {}", id);

        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    // Delete Order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable Long id) {

        log.info("Received request to delete order ID: {}", id);

        orderService.deleteOrder(id);

        return ResponseEntity.noContent().build();
    }

}
