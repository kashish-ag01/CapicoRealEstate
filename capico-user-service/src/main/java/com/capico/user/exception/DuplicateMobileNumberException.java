package com.capico.user.exception;

public class DuplicateMobileNumberException extends RuntimeException {

    public DuplicateMobileNumberException(String message) {
        super(message);
    }
}