package com.Payment_Service.PaymentService.controller;

import com.Payment_Service.PaymentService.dto.PaymentRequest;
import com.Payment_Service.PaymentService.dto.PaymentResponse;
import com.Payment_Service.PaymentService.service.PaymentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")

public class PaymentController {
    private static final Logger log =
            LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // 1. Create Payment
    @PostMapping
    public ResponseEntity<PaymentResponse> makePayment(
            @Valid @RequestBody PaymentRequest request) {

        log.info("Creating payment for order ID: {}", request.getOrderId());

        PaymentResponse response = paymentService.makePayment(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. Get Payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(
            @PathVariable Long id) {

        log.info("Fetching payment ID: {}", id);

        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    // 3. Get Payment Status
    @GetMapping("/{id}/status")
    public ResponseEntity<PaymentResponse> getPaymentStatus(
            @PathVariable Long id) {

        log.info("Checking payment status for ID: {}", id);

        return ResponseEntity.ok(paymentService.getPaymentStatus(id));
    }

    // 4. Delete Payment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {

        log.info("Deleting payment ID: {}", id);

        paymentService.deletePayment(id);

        return ResponseEntity.noContent().build();
    }

}
