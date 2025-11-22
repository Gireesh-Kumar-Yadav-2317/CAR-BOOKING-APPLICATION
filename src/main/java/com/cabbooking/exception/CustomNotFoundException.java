package com.cabbooking.exception;

/**
 * Thrown when a resource (User, Driver, Admin) is not found.
 */
public class CustomNotFoundException extends RuntimeException {

    public CustomNotFoundException() {
        super();
    }

    public CustomNotFoundException(String message) {
        super(message);
    }

    public CustomNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
