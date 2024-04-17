package com.ys.hotelroommanagementbackend.exception;

public class ImpossibleOperationException extends RuntimeException {
    public ImpossibleOperationException(String message) {
        super(message);
    }

    public ImpossibleOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
