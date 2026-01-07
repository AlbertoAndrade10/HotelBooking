package com.poroflio.hotel_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HotelNotFoundException extends RuntimeException {
    public HotelNotFoundException(String id) {
        super("Hotel not found with id: " + id);
    }
}
