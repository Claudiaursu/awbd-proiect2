package com.awbd.billing.controllers;

import com.awbd.billing.exceptions.BillingNotFound;
import com.awbd.billing.exceptions.ExceptionPattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedExceptionHandlerBilling extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BillingNotFound.class)
    protected ResponseEntity<Object> handleException(RuntimeException ex, WebRequest request) {
        ExceptionPattern exception = new ExceptionPattern(new Date(), ex.getMessage(), request.getDescription(true));

        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }
}
