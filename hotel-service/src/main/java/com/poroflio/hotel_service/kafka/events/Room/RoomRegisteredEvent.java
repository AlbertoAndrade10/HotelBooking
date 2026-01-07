package com.poroflio.hotel_service.kafka.events.Room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomRegisteredEvent {
    private String roomUUID;
    private String hotelUUID;
    private String roomType;

}
