package com.capico.property.exception;

public class ValidationException
        extends RuntimeException {

    public ValidationException(
            String message
    ) {
        super(message);
    }
}