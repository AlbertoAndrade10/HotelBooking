package com.poroflio.hotel_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateHotelException extends RuntimeException {

    public DuplicateHotelException(String name) {
        super("Hotel already exists with name: " + name);
    }

}
