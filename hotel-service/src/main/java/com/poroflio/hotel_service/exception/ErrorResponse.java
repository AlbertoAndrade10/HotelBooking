package com.poroflio.hotel_service.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int statusCode;
    private String message;
    private String error;
    private LocalDateTime timestamp;

}
