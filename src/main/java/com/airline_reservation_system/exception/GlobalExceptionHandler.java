package com.airline_reservation_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle all ResponseStatusExceptions (404, 409, etc.)
    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatusException(ResponseStatusException ex) {
        return ex.getReason();
    }

    // Handle any other unhandled exceptions
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex) {
        return "Error: " + ex.getMessage();
    }
}