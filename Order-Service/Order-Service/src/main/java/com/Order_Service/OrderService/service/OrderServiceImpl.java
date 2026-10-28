
package com.Order_Service.OrderService.service;

import com.Order_Service.OrderService.client.ProductClient;
import com.Order_Service.OrderService.client.ProductResponse;
import com.Order_Service.OrderService.dto.OrderRequest;
import com.Order_Service.OrderService.dto.OrderResponse;
import com.Order_Service.OrderService.entity.Order;
import com.Order_Service.OrderService.entity.OrderStatus;
import com.Order_Service.OrderService.exception.ResourceNotFoundException;
import com.Order_Service.OrderService.repository.OrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log =
            LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        log.info("Creating order for product ID: {}",
                request.getProductId());

        ProductResponse product =
                productClient.getProductById(request.getProductId());

        if (product.getPrice() == null ||
                product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Invalid product price");
        }

        if (product.getQuantity() == null ||
                product.getQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException(
                    "Insufficient product stock");
        }

        BigDecimal totalAmount = product.getPrice()
                .multiply(BigDecimal.valueOf(request.getQuantity()));

        Order order = new Order();
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setTotalAmount(totalAmount);
        order.setShippingAddress(request.getShippingAddress());
        order.setStatus(OrderStatus.CREATED);

        Order savedOrder = orderRepository.save(order);

        log.info("Order created successfully. ID: {}",
                savedOrder.getId());

        return mapToResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {

        log.info("Fetching order ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with ID: " + id));

        return mapToResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {

        log.info("Fetching all orders");

        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long id) {

        log.info("Cancelling order ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with ID: " + id));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException(
                    "Order is already cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);

        Order updatedOrder = orderRepository.save(order);

        log.info("Order cancelled successfully. ID: {}", id);

        return mapToResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {

        log.info("Deleting order ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with ID: " + id));

        orderRepository.delete(order);

        log.info("Order deleted successfully. ID: {}", id);
    }

    private OrderResponse mapToResponse(Order order) {

        return new OrderResponse(
                order.getId(),
                order.getProductId(),
                order.getQuantity(),
                order.getTotalAmount(),
                order.getShippingAddress(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}
