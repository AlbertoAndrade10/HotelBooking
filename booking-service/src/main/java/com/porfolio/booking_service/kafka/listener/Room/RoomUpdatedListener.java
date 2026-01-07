package com.porfolio.booking_service.kafka.listener.Room;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.porfolio.booking_service.kafka.events.roomReference.RoomUpdatedEvent;
import com.porfolio.booking_service.service.hotel.roomReference.IRoomReferenceService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RoomUpdatedListener {
    private final IRoomReferenceService roomReferenceService;
    private final ObjectMapper objectMapper;

    public RoomUpdatedListener(IRoomReferenceService roomReferenceService, ObjectMapper objectMapper) {
        this.roomReferenceService = roomReferenceService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "room-updated", groupId = "booking-service-group")
    public void onRoomUpdated(String message) {
        try {
            RoomUpdatedEvent event = objectMapper.readValue(message, RoomUpdatedEvent.class);
            roomReferenceService.updatedRoom(event);
            log.info("Hotel updated: {}", event.getRoomType());
        } catch (Exception e) {
            log.info("Failed to process room-updated event", e);
        }
    }
}
