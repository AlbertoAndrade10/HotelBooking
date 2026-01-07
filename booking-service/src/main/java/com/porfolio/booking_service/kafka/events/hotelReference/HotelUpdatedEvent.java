package com.porfolio.booking_service.kafka.events.hotelReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelUpdatedEvent {
    private String hotelId;
    private String name;
}
