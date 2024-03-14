package com.papel.orders.exceptions;

public class ConflictException extends RuntimeException {

    public ConflictException() {
        super("Entry already exists");
    }

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}