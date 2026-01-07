package com.porfolio.booking_service.kafka.service.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public BookingEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishBookingCreated(Object event) {
        kafkaTemplate.send("booking-created", event);
    }

    public void publishBookingStatusChanged(Object event) {
        kafkaTemplate.send("booking-status-changed", event);
    }

}
