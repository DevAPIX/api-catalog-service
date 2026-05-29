package com.devapix.api_catalog_service.service.impl;

import com.devapix.api_catalog_service.exception.SwaggerImportException;
import com.devapix.api_catalog_service.model.ApiEndpoint;
import com.devapix.api_catalog_service.repo.ApiEndpointRepo;
import com.devapix.api_catalog_service.service.SwaggerImportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import com.devapix.api_catalog_service.exception.*;
import com.devapix.api_catalog_service.repo.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SwaggerImportServiceImpl implements SwaggerImportService {

    private final ApiEndpointRepo repository;
    private final ApiRepo apiRepo;
    private final ObjectMapper mapper;
    private final MessageSource messageSource;

    @Override
    public Integer importFromUrl(String swaggerUrl, Integer apiId) throws Exception {

        if (!apiRepo.existsById(apiId)) {
            throw new ApiNotFoundException(messageSource.getMessage("api.not.found", new Object[]{apiId}, LocaleContextHolder.getLocale()));
        }
        SwaggerParseResult result = new OpenAPIV3Parser().readLocation(swaggerUrl, null, null);
        OpenAPI openAPI = result.getOpenAPI();

        if (openAPI == null) {
            throw new SwaggerImportException(messageSource.getMessage("swagger.import.url.invalid", new Object[]{swaggerUrl}, LocaleContextHolder.getLocale()));
        }
        saveEndpoints(openAPI, apiId);
        return apiId;
    }

    @Override
    public int importFromFile(String content, Integer apiId) throws Exception {

        if (!apiRepo.existsById(apiId)) {
            throw new com.devapix.api_catalog_service.exception.ApiNotFoundException(messageSource.getMessage("api.not.found", new Object[]{apiId}, LocaleContextHolder.getLocale()));
        }
        SwaggerParseResult result = new OpenAPIV3Parser().readContents(content);
        OpenAPI openAPI = result.getOpenAPI();
        if (openAPI == null) {
            throw new SwaggerImportException(messageSource.getMessage("swagger.import.file.invalid", null, LocaleContextHolder.getLocale()));
        }
        saveEndpoints(openAPI, apiId);
        return apiId;
    }

    private void saveEndpoints(OpenAPI openAPI, Integer apiId) throws Exception {

        Paths paths = openAPI.getPaths();
        if (paths == null) return;
        for (Map.Entry<String, PathItem> pathEntry : paths.entrySet()) {
            String endpoint = pathEntry.getKey();
            PathItem pathItem = pathEntry.getValue();
            Map<PathItem.HttpMethod, Operation> operations = pathItem.readOperationsMap();
            for (Map.Entry<PathItem.HttpMethod, Operation> op : operations.entrySet()) {
                Operation operation = op.getValue();
                ApiEndpoint apiEndpoint = ApiEndpoint.builder().apiId(apiId).endpoint(endpoint).method(op.getKey().name())
                        .headersJson(extractHeaders(operation)).paramsJson(extractParams(operation)).sampleRequest(extractRequestBody(operation))
                        .sampleResponse(extractResponse(operation)).statusCodesJson(extractStatusCodes(operation)).build();
                repository.save(apiEndpoint);
            }
        }
    }

    private String extractParams(Operation operation) throws Exception {
        Map<String, String> params = new HashMap<>();
        if (operation.getParameters() != null) {
            for (Parameter p : operation.getParameters()) {
                if ("query".equalsIgnoreCase(p.getIn())) {
                    params.put(p.getName(), "");
                }
            }
        }
        return mapper.writeValueAsString(params);
    }

    private String extractHeaders(Operation operation) throws Exception {
        Map<String, String> headers = new HashMap<>();
        if (operation.getParameters() != null) {
            for (Parameter p : operation.getParameters()) {
                if ("header".equalsIgnoreCase(p.getIn())) {
                    headers.put(p.getName(), "");
                }
            }
        }
        return mapper.writeValueAsString(headers);
    }

    private String extractRequestBody(Operation operation) throws Exception {
        if (operation.getRequestBody() == null) return "{}";
        return mapper.writeValueAsString(operation.getRequestBody().getContent());
    }

    private String extractResponse(Operation operation) throws Exception {
        if (operation.getResponses() == null) return "{}";
        return mapper.writeValueAsString(operation.getResponses());
    }

    private String extractStatusCodes(Operation operation) throws Exception {
        if (operation.getResponses() == null) return "[]";
        return mapper.writeValueAsString(operation.getResponses().keySet());
    }
}
