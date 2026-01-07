package com.poroflio.hotel_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoomTypeNotFoundException extends RuntimeException {
    public RoomTypeNotFoundException(Long id) {
        super("Room type not found with id: " + id);
    }
}
