package com.devapix.exception;

public class EndpointMismatchException extends BadRequestException {
    public EndpointMismatchException(String message) {
        super(message);
    }
}
