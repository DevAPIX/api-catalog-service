package com.devapix.api_catalog_service.dto;

import lombok.Data;

@Data
public class ApiSearchRequest {

    private String category;
    private String visibility;
    private String query;
    private int page;
    private int size;
    private String sortBy;
    private String direction;
}
