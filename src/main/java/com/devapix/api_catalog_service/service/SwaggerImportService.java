package com.devapix.api_catalog_service.service;

public interface SwaggerImportService {

    Integer importFromUrl(String swaggerUrl, Integer apiId) throws Exception;

    int importFromFile(String content, Integer apiId) throws Exception;
}
