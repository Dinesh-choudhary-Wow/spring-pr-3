package com.springAndBoot3.exception;

public class RoomTypeUnavailableException extends RuntimeException {
    public RoomTypeUnavailableException(String message) {
        super(message);
    }
}