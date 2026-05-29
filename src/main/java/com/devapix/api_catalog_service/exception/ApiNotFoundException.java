package com.devapix.exception;

public class ApiNotFoundException extends ResourceNotFoundException {
    public ApiNotFoundException(String message) {
        super(message);
    }
}
