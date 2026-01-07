package com.poroflio.hotel_service.kafka.events.Hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelDeletedEvent {
    private String hotelID;
}
