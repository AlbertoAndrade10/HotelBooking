package com.porfolio.booking_service.exception.booking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidBookingTransitionException extends RuntimeException {
    public InvalidBookingTransitionException() {
        super("Invalid booking transition");
    }

}
