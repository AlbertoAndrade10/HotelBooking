package com.porfolio.booking_service.exception.room;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RoomAlreadyExistsException extends RuntimeException {
    public RoomAlreadyExistsException(String hotelUUID) {
        super("Room already exits for this hotelUUID: " + hotelUUID);
    }
}
