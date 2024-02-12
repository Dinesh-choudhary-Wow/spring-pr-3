package com.springAndBoot3.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoomTypeUnavailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleRoomTypeUnavailableException(RoomTypeUnavailableException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConstraintViolationException(ConstraintViolationException ex) {
        return ex.getMessage();
    }
}