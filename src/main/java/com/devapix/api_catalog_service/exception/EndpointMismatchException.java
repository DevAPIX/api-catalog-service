package com.devapix.api_catalog_service.exception;

public class EndpointMismatchException extends BadRequestException {
    public EndpointMismatchException(String message) {
        super(message);
    }
}
