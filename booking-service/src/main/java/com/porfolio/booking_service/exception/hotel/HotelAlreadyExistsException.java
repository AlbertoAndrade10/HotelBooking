package com.porfolio.booking_service.exception.hotel;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class HotelAlreadyExistsException extends RuntimeException {
    public HotelAlreadyExistsException(String message) {
        super(message);
    }

}
