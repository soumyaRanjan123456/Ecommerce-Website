package com.Payment_Service.PaymentService.service;

import com.Payment_Service.PaymentService.client.OrderClient;
import com.Payment_Service.PaymentService.client.OrderResponse;
import com.Payment_Service.PaymentService.dto.PaymentRequest;
import com.Payment_Service.PaymentService.dto.PaymentResponse;
import com.Payment_Service.PaymentService.entity.Payment;
import com.Payment_Service.PaymentService.repository.PaymentRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import com.Payment_Service.PaymentService.entity.PaymentStatus;

@Service
public class PaymentServiceImpl implements PaymentService{

    private static final Logger log =
            LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            OrderClient orderClient) {
        this.paymentRepository = paymentRepository;
        this.orderClient = orderClient;
    }

    @Override
    @Transactional
    public PaymentResponse makePayment(PaymentRequest request) {

        log.info("Creating payment for order ID: {}",
                request.getOrderId());

        // Prevent a second payment record for the same order.
        if (paymentRepository.findByOrderId(request.getOrderId()).isPresent()) {
            throw new IllegalArgumentException(
                    "Payment already exists for this order");
        }

        // Fetch order details from Order Service.
        OrderResponse order =
                orderClient.getOrderById(request.getOrderId());

        if (order == null || order.getTotalAmount() == null
                || order.getTotalAmount().signum() <= 0) {
            throw new IllegalArgumentException(
                    "Order has an invalid payment amount");
        }

        if ("CANCELLED".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot pay for a cancelled order");
        }

        // Create payment record.
        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(request.getPaymentMethod().trim());
        payment.setTransactionId("TXN-" + UUID.randomUUID());

        // Demo only: this simulates payment success.
        payment.setStatus(PaymentStatus.SUCCESS);

        Payment savedPayment = paymentRepository.save(payment);

        log.info("Payment created successfully. Payment ID: {}",
                savedPayment.getId());

        return mapToResponse(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long id) {

        log.info("Fetching payment ID: {}", id);

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with ID: " + id));

        return mapToResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentStatus(Long id) {

        log.info("Checking payment status for ID: {}", id);

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with ID: " + id));

        return mapToResponse(payment);
    }

    @Override
    @Transactional
    public void deletePayment(Long id) {

        log.info("Deleting payment ID: {}", id);

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with ID: " + id));

        paymentRepository.delete(payment);

        log.info("Payment deleted successfully. ID: {}", id);
    }

    private PaymentResponse mapToResponse(Payment payment) {

        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getTransactionId(),
                payment.getStatus(),
                payment.getCreatedAt()
        );
    }

}
