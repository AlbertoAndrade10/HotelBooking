package com.porfolio.booking_service.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.porfolio.booking_service.exception.booking.BookingAlreadyExists;
import com.porfolio.booking_service.exception.booking.BookingNotFoundException;
import com.porfolio.booking_service.exception.booking.InvalidBookingException;
import com.porfolio.booking_service.exception.booking.InvalidBookingTransitionException;
import com.porfolio.booking_service.exception.hotel.HotelAlreadyExistsException;
import com.porfolio.booking_service.exception.hotel.HotelInactiveException;
import com.porfolio.booking_service.exception.hotel.HotelNotFoundException;
import com.porfolio.booking_service.exception.room.RoomAlreadyExistsException;
import com.porfolio.booking_service.exception.room.RoomNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // --------------------------------
    // ------------ Booking -----------
    // --------------------------------
    // handle BookingNotFoundException
    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookingNotFoundException(BookingNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                "BookingNotFoundException",
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // handle InvalidBookingException
    @ExceptionHandler(InvalidBookingException.class)
    public ResponseEntity<ErrorResponse> handleInvalidBookingException(InvalidBookingException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                "InvalidBookingException",
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // handle BookingAlreadyExists
    @ExceptionHandler(BookingAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleBookingAlreadyExists(BookingAlreadyExists ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                "BookingAlreadyExists",
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // handle Invalid Booking TransitionException
    @ExceptionHandler(InvalidBookingTransitionException.class)
    public ResponseEntity<ErrorResponse> invalidBookingTransitionException(InvalidBookingTransitionException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage(),
                "InvalidBookingTransitionException", LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // --------------------------------
    // ------------- Hotel ------------
    // --------------------------------

    // handle HotelInactiveException
    @ExceptionHandler(HotelInactiveException.class)
    public ResponseEntity<ErrorResponse> handleHotelInactiveException(HotelInactiveException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                "HotelInactiveException",
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // HOTEL ALREADY EXISTS EXCEPTION
    @ExceptionHandler(HotelAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleHotelAlreadyExistsException(HotelAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                "HotelAlreadyExistsException",
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // HOTEL NOT FOUND EXCEPTION
    @ExceptionHandler(HotelNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleHotelNotFoundException(HotelNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                "HotelNotFoundException",
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // --------------------------------
    // ------------- Room -------------
    // --------------------------------
    @ExceptionHandler(RoomAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleRoomAlreadyExistsException(RoomAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage(),
                "RoomAlreadyExists", LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoomNotFoundException(RoomNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
                "RoomNotFound", LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // --------------------------------
    // ------------ OTHERS ------------
    // --------------------------------

    // handle ForbiddenOperationException

    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenOperationException(ForbiddenOperationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                "ForbiddenOperationException",
                LocalDateTime.now());

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);

    }

    // handle validations errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        String message = "Validation failed: " + errors.toString();
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message,
                "ValidationException",
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // handle uncontrolled exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred: " + ex.getMessage(),
                "InternalServerException",
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
