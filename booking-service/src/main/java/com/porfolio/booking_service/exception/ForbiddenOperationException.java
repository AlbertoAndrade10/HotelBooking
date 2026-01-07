package com.porfolio.booking_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenOperationException extends RuntimeException {
    public ForbiddenOperationException() {
        super("Forbidden operation");
    }

}
