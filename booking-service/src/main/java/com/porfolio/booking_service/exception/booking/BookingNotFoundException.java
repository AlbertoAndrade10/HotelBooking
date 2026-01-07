package com.porfolio.booking_service.exception.booking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String id) {
        super("Booking not found with id: " + id);
    }
}
