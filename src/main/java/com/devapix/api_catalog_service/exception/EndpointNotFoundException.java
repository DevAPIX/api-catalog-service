package com.devapix.api_catalog_service.exception;

public class EndpointNotFoundException extends ResourceNotFoundException {
    public EndpointNotFoundException(String message) {
        super(message);
    }
}
