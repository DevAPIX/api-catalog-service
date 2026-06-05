package com.devapix.api_catalog_service.dto;


import lombok.Data;

@Data
public class ApiEndpoint {

    private Integer id;
    private Integer apiId;
    private String endpoint;
    private String method;
    private String headersJson;
    private String paramsJson;
    private String sampleRequest;
    private String sampleResponse;
    private String statusCodesJson;
}
