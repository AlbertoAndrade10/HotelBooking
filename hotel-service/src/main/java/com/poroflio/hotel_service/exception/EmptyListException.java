package com.poroflio.hotel_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class EmptyListException extends RuntimeException {
    public EmptyListException() {
        super("The list is empty.");
    }

}
