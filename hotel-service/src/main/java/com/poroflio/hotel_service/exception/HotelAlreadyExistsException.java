package com.poroflio.hotel_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class HotelAlreadyExistsException extends RuntimeException {
    public HotelAlreadyExistsException(String id) {
        super("Hotel already exists with id: " + id);
    }
}
