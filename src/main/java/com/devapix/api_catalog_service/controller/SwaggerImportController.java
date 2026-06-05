package com.devapix.api_catalog_service.controller;


import com.devapix.api_catalog_service.dto.response.SwaggerImportResponse;
import com.devapix.api_catalog_service.exception.BadRequestException;
import com.devapix.api_catalog_service.exception.ResourceNotFoundException;
import com.devapix.api_catalog_service.model.ApiEndpoint;
import com.devapix.api_catalog_service.repo.ApiEndpointRepo;
import com.devapix.api_catalog_service.repo.ApiRepo;
import com.devapix.api_catalog_service.service.SwaggerImportService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/swagger")
@RequiredArgsConstructor
public class SwaggerImportController {

    private final SwaggerImportService service;
    private final ApiEndpointRepo endpointrepo;
    private final ApiRepo apiRepo;
    private final MessageSource messageSource;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("json", "yaml", "yml");
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList("application/json", "application/x-yaml", "text/yaml", "application/yaml", "application/octet-stream");

    @PostMapping("/url")
    public SwaggerImportResponse importFromUrl(@RequestParam String url, @RequestParam Integer apiId) throws Exception {
        Integer count = service.importFromUrl(url, apiId);
        return new SwaggerImportResponse(apiId, messageSource.getMessage("swagger.import.url.success", null, LocaleContextHolder.getLocale()), count);
    }

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SwaggerImportResponse importFromFile(@RequestParam("file") MultipartFile file, @RequestParam("apiId") Integer apiId) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException(messageSource.getMessage("swagger.import.file.empty", null, LocaleContextHolder.getLocale()));
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                throw new BadRequestException(messageSource.getMessage("swagger.import.file.invalid.type", null, LocaleContextHolder.getLocale()));
            }
        }

        String contentType = file.getContentType();
        if (contentType != null && !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new BadRequestException(messageSource.getMessage("swagger.import.file.invalid.type", null, LocaleContextHolder.getLocale()));
        }

        String content = new String(file.getBytes());
        int count = service.importFromFile(content, apiId);
        return new SwaggerImportResponse(apiId, messageSource.getMessage("swagger.import.file.success", null, LocaleContextHolder.getLocale()), count);
    }

    @PostMapping("/internal/endpoints")
    public ApiEndpoint createEndpoint(@RequestBody ApiEndpoint endpoint) {
        if (!apiRepo.existsById(endpoint.getApiId())) {
            throw new ResourceNotFoundException(messageSource.getMessage("api.not.found", new Object[]{endpoint.getApiId()}, LocaleContextHolder.getLocale()));
        }
        return endpointrepo.save(endpoint);
    }

    @GetMapping("/internal/endpoints/{id}")
    public ApiEndpoint getEndpointById(@PathVariable Integer id) {
        return endpointrepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("endpoint.not.found", new Object[]{id}, LocaleContextHolder.getLocale())));
    }

    @PutMapping("/internal/endpoints/{id}")
    public ApiEndpoint updateEndpoint(@PathVariable Integer id, @RequestBody ApiEndpoint update) {
        ApiEndpoint existing = endpointrepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("endpoint.not.found", new Object[]{id}, LocaleContextHolder.getLocale())));
        if (update.getEndpoint() != null) existing.setEndpoint(update.getEndpoint());
        if (update.getMethod() != null) existing.setMethod(update.getMethod());
        if (update.getHeadersJson() != null) existing.setHeadersJson(update.getHeadersJson());
        if (update.getParamsJson() != null) existing.setParamsJson(update.getParamsJson());
        if (update.getSampleRequest() != null) existing.setSampleRequest(update.getSampleRequest());
        if (update.getSampleResponse() != null) existing.setSampleResponse(update.getSampleResponse());
        if (update.getStatusCodesJson() != null) existing.setStatusCodesJson(update.getStatusCodesJson());
        return endpointrepo.save(existing);
    }

    @DeleteMapping("/internal/endpoints/{id}")
    public ResponseEntity<Map<String, String>> deleteEndpoint(@PathVariable Integer id) {
        if (!endpointrepo.existsById(id)) {
            throw new ResourceNotFoundException(messageSource.getMessage("endpoint.not.found", new Object[]{id}, LocaleContextHolder.getLocale()));
        }
        endpointrepo.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", messageSource.getMessage("endpoint.delete.success", null, LocaleContextHolder.getLocale()));
        return ResponseEntity.ok(response);
    }
}
