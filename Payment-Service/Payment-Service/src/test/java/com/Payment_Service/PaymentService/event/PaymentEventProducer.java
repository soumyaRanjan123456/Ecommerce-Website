package com.Payment_Service.PaymentService.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class PaymentEventProducer {
    private static final Logger log =
            LoggerFactory.getLogger(PaymentEventProducer.class);

    private static final String TOPIC = "payment-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PaymentEventProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(PaymentEvent event) {

        try {
            String message = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(
                    TOPIC,
                    String.valueOf(event.getOrderId()),
                    message
            ).whenComplete((result, error) -> {

                if (error != null) {
                    log.error("Failed to publish payment event", error);
                } else {
                    log.info(
                            "Payment event published for order ID: {}",
                            event.getOrderId()
                    );
                }
            });

        } catch (JsonProcessingException ex) {
            log.error("Failed to convert payment event to JSON", ex);
        }
    }
}
