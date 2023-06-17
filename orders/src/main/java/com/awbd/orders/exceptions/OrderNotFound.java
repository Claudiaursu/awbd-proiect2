package com.awbd.orders.exceptions;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFound extends RuntimeException {
    public OrderNotFound(String message) {
        super(message);
    }
}
