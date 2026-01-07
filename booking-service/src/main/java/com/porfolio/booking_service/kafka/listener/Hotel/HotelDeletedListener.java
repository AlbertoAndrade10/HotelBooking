package com.porfolio.booking_service.kafka.listener.Hotel;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.porfolio.booking_service.kafka.events.hotelReference.HotelDeletedEvent;
import com.porfolio.booking_service.service.hotel.hotelReference.IHotelReferenceService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HotelDeletedListener {
    private IHotelReferenceService hotelReferenceService;
    private ObjectMapper objectMapper;

    public HotelDeletedListener(IHotelReferenceService hotelReferenceService, ObjectMapper objectMapper) {
        this.hotelReferenceService = hotelReferenceService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "hotel-deleted", groupId = "booking-service-group")
    public void onHotelDeleted(String message) {
        try {
            HotelDeletedEvent event = objectMapper.readValue(message, HotelDeletedEvent.class);
            hotelReferenceService.deleteHotel(event);
            log.info("Hotel deleted: {}", event.getHotelUUID());

        } catch (Exception e) {
            log.error("Failed to process hotel-deleted event", e);
        }

    }

}
