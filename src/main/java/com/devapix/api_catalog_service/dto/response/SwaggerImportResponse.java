package com.devapix.api_catalog_service.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SwaggerImportResponse {

    private Integer apiId;
    private String message;
    private int endpointsImported;
}
