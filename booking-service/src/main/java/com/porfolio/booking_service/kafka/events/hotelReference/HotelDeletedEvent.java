package com.porfolio.booking_service.kafka.events.hotelReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelDeletedEvent {
    private String hotelUUID;
}
