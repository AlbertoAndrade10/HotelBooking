package com.poroflio.hotel_service.kafka.services.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.poroflio.hotel_service.kafka.events.Room.RoomDeletedEvent;
import com.poroflio.hotel_service.kafka.events.Room.RoomRegisteredEvent;
import com.poroflio.hotel_service.kafka.events.Room.RoomUpdatedEvent;

@Component
public class RoomEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public RoomEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // event for registered
    public void publishRoomRegistered(RoomRegisteredEvent event) {
        kafkaTemplate.send(
                "room-registered",
                event);
        System.out.println("Published room-registered for for hoteId: " + event.getHotelUUID());
    }

    // event for updated
    public void publishRoomUpdated(RoomUpdatedEvent event) {
        kafkaTemplate.send(
                "room-updated",
                event);
        System.out.println("Updated room-updated for for hoteId: " + event.getHotelUUID());
    }

    // event for delete
    public void deleteRoom(RoomDeletedEvent event) {
        kafkaTemplate.send(
                "room-deleted",
                event);
        System.out.println("Published room-deleted for hotel-id " + event.getRoomUUID());
    }
}
