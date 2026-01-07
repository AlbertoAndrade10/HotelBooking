package com.poroflio.hotel_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoomNotFoundWithHotelId extends RuntimeException {
    public RoomNotFoundWithHotelId(String hotelId) {
        super("No rooms found for hotel with id: " + hotelId);
    }

}
