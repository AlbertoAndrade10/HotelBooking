package com.porfolio.booking_service.kafka.listener.Hotel;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.porfolio.booking_service.kafka.events.hotelReference.HotelRegisteredEvent;
import com.porfolio.booking_service.service.hotel.hotelReference.IHotelReferenceService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HotelRegistrationListener {
    private final IHotelReferenceService hotelReferenceService;
    private final ObjectMapper objectMapper;

    public HotelRegistrationListener(IHotelReferenceService hotelReferenceService, ObjectMapper objectMapper) {
        this.hotelReferenceService = hotelReferenceService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "hotel-registered", groupId = "booking-service-group")
    public void onHotelRegistered(String message) {
        try {

            HotelRegisteredEvent event = objectMapper.readValue(message, HotelRegisteredEvent.class);

            hotelReferenceService.registerHotel(event);

            log.info("Hotel registered: {}", event.getHotelId());

        } catch (Exception e) {
            log.error("Failed to process hotel-registered event", e);
        }
    }
}
