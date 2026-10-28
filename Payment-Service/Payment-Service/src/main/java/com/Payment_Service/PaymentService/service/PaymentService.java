package com.Payment_Service.PaymentService.service;

import com.Payment_Service.PaymentService.dto.PaymentRequest;
import com.Payment_Service.PaymentService.dto.PaymentResponse;

public interface PaymentService {
    PaymentResponse makePayment(PaymentRequest request);

    PaymentResponse getPaymentById(Long id);

    PaymentResponse getPaymentStatus(Long id);

    void deletePayment(Long id);
}
