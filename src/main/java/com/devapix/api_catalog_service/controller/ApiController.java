package com.devapix.api_catalog_service.controller;

import com.devapix.api_catalog_service.dto.ApiExecutionDataDTO;
import com.devapix.api_catalog_service.dto.ApiSearchRequest;
import com.devapix.api_catalog_service.dto.CreateApiRequest;
import com.devapix.api_catalog_service.dto.response.ApiResponse;
import com.devapix.api_catalog_service.exception.*;
import com.devapix.api_catalog_service.mapper.ApiMapper;
import com.devapix.api_catalog_service.model.ApiEndpoint;
import com.devapix.api_catalog_service.model.ApiModel;
import com.devapix.api_catalog_service.repo.ApiEndpointRepo;
import com.devapix.api_catalog_service.repo.ApiRepo;
import com.devapix.api_catalog_service.service.ApiService;
import com.devapix.api_catalog_service.service.AuthServiceClient;
import com.devapix.api_catalog_service.service.SubscriptionServiceClient;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiController {

    private final ApiRepo apirepo;
    private final ApiService apiService;
    private final ApiEndpointRepo endpointRepo;
    private final MessageSource messageSource;
    private final AuthServiceClient authServiceClient;
    private final SubscriptionServiceClient subscriptionServiceClient;

    @PostMapping("/apis")
    public ApiResponse publishApi(@Valid @RequestBody CreateApiRequest request, @Parameter(hidden = true) @RequestHeader("X-User-Id") String userIdStr) {
        Integer ownerId = Integer.parseInt(userIdStr);
        validateUserExists(ownerId);
        log.info("Publishing API for userId: {}", ownerId);
        return apiService.publishApi(request, ownerId);
    }

    private void validateUserExists(Integer userId) {
        try {
            boolean valid = authServiceClient.isUserValid(userId);
            if (!valid) {
                throw new IllegalArgumentException(messageSource.getMessage("user.not.found", new Object[]{userId}, LocaleContextHolder.getLocale()));
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Could not validate user {} with auth-service: {}", userId, e.getMessage());
        }
    }

    @GetMapping("/internal/apis/{id}")
    public ApiResponse getApiById(@PathVariable Integer id) {
        ApiModel api = apirepo.findById(id).orElseThrow(() -> new ApiNotFoundException(messageSource.getMessage("api.not.found", new Object[]{id}, LocaleContextHolder.getLocale())));
        ApiResponse response = ApiMapper.toDTO(api);
        try {
            java.util.Map<Integer, com.devapix.api_catalog_service.dto.response.UserInfoResponse> users = 
                authServiceClient.getUsersBatch(java.util.List.of(api.getOwnerId()));
            if (users != null && users.containsKey(api.getOwnerId())) {
                response.setPublisherName(users.get(api.getOwnerId()).getName());
            }
        } catch (Exception e) {
            log.warn("Failed to fetch publisher name for owner {}: {}", api.getOwnerId(), e.getMessage());
        }
        return response;
    }

    @GetMapping("/apis/category/{categoryId}")
    public List<ApiResponse> getApisByCategoryId(@PathVariable Integer categoryId) {
        log.info("GET /apis/category/{}", categoryId);
        return apirepo.findByCategoryId(categoryId).stream().map(ApiMapper::toDTO).collect(Collectors.toList());
    }

    @PutMapping("/apis/{apiId}")
    public ResponseEntity<ApiResponse> updateApiById( @PathVariable Integer apiId, @RequestBody Map<String, Object> updates, @Parameter(hidden = true) @RequestHeader("X-User-Id") String userIdStr) {
        Integer userId = Integer.parseInt(userIdStr);
        ApiModel api = apirepo.findById(apiId).orElseThrow(() -> new ApiNotFoundException(messageSource.getMessage("api.not.found", new Object[]{apiId}, LocaleContextHolder.getLocale())));
        if (!api.getOwnerId().equals(userId)) {
            throw new IllegalArgumentException(messageSource.getMessage("api.unauthorized.update", null, LocaleContextHolder.getLocale()));
        }
        if (updates.containsKey("name")) api.setName((String) updates.get("name"));
        if (updates.containsKey("description")) api.setDescription((String) updates.get("description"));
        if (updates.containsKey("visibility")) api.setVisibility((String) updates.get("visibility"));
        return ResponseEntity.ok(ApiMapper.toDTO(apirepo.save(api)));
    }

    @DeleteMapping("/apis/{apiId}")
    public ResponseEntity<Void> deleteApiById(@PathVariable Integer apiId, @Parameter(hidden = true) @RequestHeader("X-User-Id") String userIdStr) {
        Integer userId = Integer.parseInt(userIdStr);
        ApiModel api = apirepo.findById(apiId).orElseThrow(() -> new ApiNotFoundException(messageSource.getMessage("api.not.found", new Object[]{apiId}, LocaleContextHolder.getLocale())));
        if (!api.getOwnerId().equals(userId)) {
            throw new IllegalArgumentException(messageSource.getMessage("api.unauthorized.delete", null, LocaleContextHolder.getLocale()));
        }
        try {
            boolean hasActive = subscriptionServiceClient.hasActiveSubscriptionsForApi(apiId);
            if (hasActive) {
                log.info("API {} has active subscriptions, marking as DEPRECATED instead of deleting", apiId);
                api.setStatus("DEPRECATED");
                apirepo.save(api);
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            log.warn("Failed to check active subscriptions for API {}: {}", apiId, e.getMessage());
        }
        try {
            subscriptionServiceClient.revokeSubscriptionsByApi(apiId);
            log.info("Revoked all subscriptions for API {} before deletion", apiId);
        } catch (Exception e) {
            log.warn("Failed to revoke subscriptions for API {}: {}", apiId, e.getMessage());
        }
        api.setStatus("DELETED");
        apirepo.save(api);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/apis/search")
    public Page<ApiResponse> searchApis(@RequestParam(required = false) String category, @RequestParam(required = false) String visibility,
                                        @RequestParam(required = false) String query, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction, @RequestParam(defaultValue = "false") boolean includeDeleted, @Parameter(hidden = true) @RequestHeader(value = "X-User-Id", required = false) String userIdStr) {
        ApiSearchRequest request = new ApiSearchRequest();
        request.setCategory(category);
        request.setVisibility(visibility);
        request.setQuery(query);
        request.setPage(page);
        request.setSize(size);
        request.setSortBy(sortBy);
        request.setDirection(direction);
        request.setIncludeDeleted(includeDeleted);
        if (userIdStr != null) {
            request.setOwnerId(Integer.parseInt(userIdStr));
        }
        return apiService.searchApis(request);
    }

    @GetMapping("/apis/{apiId}/endpoints")
    public List<ApiEndpoint> getEndpointsByApiId(@PathVariable Integer apiId) {
        log.info("GET /apis/{}/endpoints", apiId);
        if (!apirepo.existsById(apiId)) {
            throw new ApiNotFoundException(messageSource.getMessage("api.not.found", new Object[]{apiId}, LocaleContextHolder.getLocale()));
        }
        return endpointRepo.findByApiId(apiId);
    }

    @GetMapping("/internal/apis/{apiId}/endpoints/{endpointId}")
    public ApiExecutionDataDTO getExecutionData(@PathVariable Integer apiId, @PathVariable Integer endpointId) {
        ApiModel api = apirepo.findById(apiId).orElseThrow(() -> new ApiNotFoundException(messageSource.getMessage("api.not.found", new Object[]{apiId}, LocaleContextHolder.getLocale())));
        ApiEndpoint endpoint = endpointRepo.findById(endpointId).orElseThrow(() -> new EndpointNotFoundException(messageSource.getMessage("endpoint.not.found", new Object[]{endpointId}, LocaleContextHolder.getLocale())));
        if (!endpoint.getApiId().equals(apiId)) {
            throw new EndpointMismatchException(messageSource.getMessage("endpoint.mismatch", new Object[]{endpointId, apiId}, LocaleContextHolder.getLocale()));
        }
        return ApiExecutionDataDTO.builder().baseUrl(api.getBaseUrl()).endpoint(endpoint.getEndpoint()).method(endpoint.getMethod()).headersJson(endpoint.getHeadersJson()).paramsJson(endpoint.getParamsJson()).sampleRequest(endpoint.getSampleRequest()).build();
    }

}
