package com.capico.property.exception;

public class DuplicatePropertyException
        extends RuntimeException {

    public DuplicatePropertyException(
            String message
    ) {
        super(message);
    }
}