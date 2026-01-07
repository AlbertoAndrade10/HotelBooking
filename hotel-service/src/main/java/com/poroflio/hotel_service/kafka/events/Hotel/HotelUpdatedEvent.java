package com.poroflio.hotel_service.kafka.events.Hotel;

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
