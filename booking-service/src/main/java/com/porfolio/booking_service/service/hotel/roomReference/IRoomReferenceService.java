package com.porfolio.booking_service.service.hotel.roomReference;

import com.porfolio.booking_service.kafka.events.roomReference.RoomDeletedEvent;
import com.porfolio.booking_service.kafka.events.roomReference.RoomRegisteredEvent;
import com.porfolio.booking_service.kafka.events.roomReference.RoomUpdatedEvent;

public interface IRoomReferenceService {
    void registeredRoom(RoomRegisteredEvent event);

    void updatedRoom(RoomUpdatedEvent event);

    void deleteRoom(RoomDeletedEvent event);
}
