package com.porfolio.booking_service.kafka.listener.Room;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.porfolio.booking_service.kafka.events.roomReference.RoomDeletedEvent;
import com.porfolio.booking_service.service.hotel.roomReference.IRoomReferenceService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RoomDeletedListener {
    private final IRoomReferenceService roomReferenceService;
    private final ObjectMapper objectMapper;

    public RoomDeletedListener(IRoomReferenceService roomReferenceService, ObjectMapper objectMapper) {
        this.roomReferenceService = roomReferenceService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "room-deleted", groupId = "booking-service-group")
    public void onDeleteRoom(String message) {
        try {
            RoomDeletedEvent event = objectMapper.readValue(message, RoomDeletedEvent.class);
            roomReferenceService.deleteRoom(event);
        } catch (Exception e) {
            log.error("Failed to process delete-room event", e);
        }
    }

}
