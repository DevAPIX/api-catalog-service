package com.devapix.controller;

import com.devapix.dto.response.SwaggerImportResponse;
import com.devapix.exception.BadRequestException;
import com.devapix.exception.ResourceNotFoundException;
import com.devapix.model.ApiEndpoint;
import com.devapix.repo.ApiEndpointRepo;
import com.devapix.service.SwaggerImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/swagger")
@RequiredArgsConstructor
public class SwaggerImportController {

    private final SwaggerImportService service;
    private final ApiEndpointRepo endpointrepo;
    private final MessageSource messageSource;

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
        String content = new String(file.getBytes());
        int count = service.importFromFile(content, apiId);
        return new SwaggerImportResponse(apiId, messageSource.getMessage("swagger.import.file.success", null, LocaleContextHolder.getLocale()), count);
    }

    @GetMapping("/internal/endpoints/{id}")
    public ApiEndpoint getEndpointById(@PathVariable Integer id) {
        return endpointrepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("endpoint.not.found", new Object[]{id}, LocaleContextHolder.getLocale())));
    }
}
