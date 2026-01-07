package com.porfolio.booking_service.exception.hotel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HotelInactiveException extends RuntimeException {
    public HotelInactiveException(String message) {
        super(message);
    }

}
