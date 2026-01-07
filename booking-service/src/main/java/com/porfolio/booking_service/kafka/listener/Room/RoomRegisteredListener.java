package com.porfolio.booking_service.kafka.listener.Room;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.porfolio.booking_service.kafka.events.roomReference.RoomRegisteredEvent;
import com.porfolio.booking_service.service.hotel.roomReference.IRoomReferenceService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RoomRegisteredListener {
    private final IRoomReferenceService roomReferenceService;
    private final ObjectMapper objectMapper;

    public RoomRegisteredListener(IRoomReferenceService roomReferenceService, ObjectMapper objectMapper) {
        this.roomReferenceService = roomReferenceService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "room-registered", groupId = "booking-service-group")
    public void onRoomRegistered(String message) {
        try {
            RoomRegisteredEvent event = objectMapper.readValue(message, RoomRegisteredEvent.class);

            roomReferenceService.registeredRoom(event);

            log.info("Room registered: {}", event.getRoomType());

        } catch (Exception e) {
            log.error("Failed to process hotel-registered event ", e);
        }

    }
}
