package com.devapix.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiExecutionDataDTO {

    private String baseUrl;
    private String endpoint;
    private String method;
    private String headersJson;
    private String paramsJson;
    private String sampleRequest;
}
