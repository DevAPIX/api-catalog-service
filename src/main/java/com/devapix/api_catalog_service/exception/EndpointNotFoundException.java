package com.devapix.exception;

public class EndpointNotFoundException extends ResourceNotFoundException {
    public EndpointNotFoundException(String message) {
        super(message);
    }
}
