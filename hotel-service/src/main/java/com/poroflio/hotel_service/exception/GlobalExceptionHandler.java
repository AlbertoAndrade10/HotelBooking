package com.poroflio.hotel_service.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ============================
    // NOT FOUND (404)
    // ============================

    @ExceptionHandler(HotelNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleHotelNotFoundException(HotelNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), "HotelNotFoundException");
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoomNotFoundException(RoomNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), "RoomNotFoundException");
    }

    @ExceptionHandler(RoomNotFoundWithHotelId.class)
    public ResponseEntity<ErrorResponse> handleRoomNotFoundWithHotelId(RoomNotFoundWithHotelId ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), "RoomNotFoundWithHotelId");
    }

    @ExceptionHandler(RoomTypeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoomTypeNotFoundException(RoomTypeNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), "RoomTypeNotFoundException");
    }

    @ExceptionHandler(EmptyListException.class)
    public ResponseEntity<ErrorResponse> handleEmptyListException(EmptyListException ex) {
        return buildError(HttpStatus.NOT_FOUND,
                ex.getMessage(),
                "EmptyListException");
    }

    // ============================
    // CONFLICT (409)
    // ============================

    @ExceptionHandler(HotelAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleHotelAlreadyExistsException(HotelAlreadyExistsException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), "HotelAlreadyExistsException");
    }

    @ExceptionHandler(DuplicateHotelException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateHotelException(DuplicateHotelException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), "DuplicateHotelException");
    }

    // ============================
    // BAD REQUEST (400)
    // ============================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return buildError(
                HttpStatus.BAD_REQUEST,
                "Validation failed: " + errors,
                "ValidationException");
    }

    // ============================
    // INTERNAL ERROR (500)
    // ============================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred: " + ex.getMessage(),
                "InternalServerException");
    }

    // ============================
    // utility method
    // ============================

    private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String msg, String type) {
        ErrorResponse response = new ErrorResponse(
                status.value(),
                msg,
                type,
                LocalDateTime.now());
        return new ResponseEntity<>(response, status);
    }
}
