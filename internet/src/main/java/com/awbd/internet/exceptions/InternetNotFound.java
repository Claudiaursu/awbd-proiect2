package com.awbd.internet.exceptions;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class InternetNotFound extends RuntimeException {
    public InternetNotFound(String message) {
        super(message);
    }
}
