package com.porfolio.booking_service.kafka.events.roomReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomRegisteredEvent {
    private String roomUUID;
    private String hotelUUID;
    private String roomType;
}
