package com.porfolio.booking_service.exception.booking;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookingAlreadyExists extends RuntimeException {

    public BookingAlreadyExists(String id) {
        super("Booking already exists with id: " + id);
    }

}
