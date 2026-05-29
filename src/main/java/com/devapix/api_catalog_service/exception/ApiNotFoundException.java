package com.devapix.api_catalog_service.exception;

public class ApiNotFoundException extends ResourceNotFoundException {
    public ApiNotFoundException(String message) {
        super(message);
    }
}
