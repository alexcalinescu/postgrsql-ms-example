package com.papel.orders.exceptions;

public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super("Invalid request");
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
