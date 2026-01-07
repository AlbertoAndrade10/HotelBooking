package com.poroflio.hotel_service.kafka.services.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.poroflio.hotel_service.kafka.events.Hotel.HotelDeletedEvent;
import com.poroflio.hotel_service.kafka.events.Hotel.HotelRegisteredEvent;
import com.poroflio.hotel_service.kafka.events.Hotel.HotelUpdatedEvent;

@Component
public class HotelEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public HotelEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // event for registered
    public void publishHotelRegistered(HotelRegisteredEvent event) {
        kafkaTemplate.send(
                "hotel-registered",
                event);
        System.out.println("Published hotel-registered event for hotelId: " + event.getHotelId());
    }

    // event for updated
    public void publishHotelUpdated(HotelUpdatedEvent event) {
        kafkaTemplate.send(
                "hotel-updated",
                event);
        System.out.println("Published hotel-updated event for hotelId: " + event.getHotelId());
    }

    // event for deleted
    public void deletedHotel(HotelDeletedEvent event) {
        kafkaTemplate.send(
                "hotel-deleted",
                event);
        System.out.println("Published hotel-deleted event for hotelID: " + event.getHotelID());
    }
}
