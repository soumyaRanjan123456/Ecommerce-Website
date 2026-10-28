package com.Order_Service.OrderService.service;

import com.Order_Service.OrderService.dto.OrderRequest;
import com.Order_Service.OrderService.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getAllOrders();

    OrderResponse cancelOrder(Long id);

    void deleteOrder(Long id);
}
