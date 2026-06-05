package com.devapix.api_catalog_service.dto;


import lombok.Data;

@Data
public class SwaggerImportRequest {
    private String url;
    private Integer apiId;
}
