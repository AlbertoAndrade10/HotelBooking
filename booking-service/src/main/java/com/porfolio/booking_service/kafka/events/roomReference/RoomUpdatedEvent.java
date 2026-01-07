package com.porfolio.booking_service.kafka.events.roomReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomUpdatedEvent {
    private String UUID;
    private String hotelUUID;
    private String roomType;
}
