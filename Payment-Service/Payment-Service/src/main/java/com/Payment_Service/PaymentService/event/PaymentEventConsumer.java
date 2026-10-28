package com.Payment_Service.PaymentService.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventConsumer {
    private static final Logger log =
            LoggerFactory.getLogger(PaymentEventConsumer.class);

    @KafkaListener(
            topics = "payment-events",
            groupId = "payment-audit-group"
    )
    public void consume(String message) {

        log.info("Received payment event: {}", message);
    }
}
