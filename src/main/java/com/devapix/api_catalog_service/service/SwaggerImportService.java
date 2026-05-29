package com.devapix.service;

public interface SwaggerImportService {

    Integer importFromUrl(String swaggerUrl, Integer apiId) throws Exception;

    int importFromFile(String content, Integer apiId) throws Exception;
}
