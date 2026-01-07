package com.porfolio.booking_service.kafka.listener.Hotel;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.porfolio.booking_service.kafka.events.hotelReference.HotelUpdatedEvent;
import com.porfolio.booking_service.service.hotel.hotelReference.IHotelReferenceService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HotelUpdatedListener {

    private final IHotelReferenceService hotelReferenceService;
    private final ObjectMapper objectMapper;

    public HotelUpdatedListener(IHotelReferenceService hotelReferenceService, ObjectMapper objectMapper) {
        this.hotelReferenceService = hotelReferenceService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "hotel-updated", groupId = "booking-service-group")
    public void onHotelUpdated(String message) {
        try {
            HotelUpdatedEvent event = objectMapper.readValue(message, HotelUpdatedEvent.class);
            hotelReferenceService.updateHotel(event);
            log.info("Hotel updated: {}", event.getHotelId());
        } catch (Exception e) {
            log.error("Failed to process hotel-updated event", e);
        }

    }
}
