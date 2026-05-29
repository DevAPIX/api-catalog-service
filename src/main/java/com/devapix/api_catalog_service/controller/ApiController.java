package com.devapix.controller;
import com.devapix.dto.ApiExecutionDataDTO;
import com.devapix.dto.response.ApiResponse;
import com.devapix.dto.CreateApiRequest;
import com.devapix.dto.ApiSearchRequest;
import com.devapix.exception.*;
import com.devapix.mapper.ApiMapper;
import com.devapix.model.ApiEndpoint;
import com.devapix.model.ApiModel;
import com.devapix.repo.ApiRepo;
import com.devapix.repo.ApiEndpointRepo;
import com.devapix.service.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiController {

    private final ApiRepo apirepo;
    private final ApiService apiService;
    private final ApiEndpointRepo endpointRepo;
    private final MessageSource messageSource;

    @PostMapping("/apis")
    public ApiResponse publishApi(@Valid @RequestBody CreateApiRequest request, @Parameter(hidden = true) @RequestHeader("X-User-Id") String userIdStr) {
        Integer ownerId = Integer.parseInt(userIdStr);
        log.info("Publishing API for userId: {}", ownerId);
        return apiService.publishApi(request, ownerId);
    }

    @GetMapping("/internal/apis/{id}")
    public ApiResponse getApiById(@PathVariable Integer id) {
        ApiModel api = apirepo.findById(id).orElseThrow(() -> new ApiNotFoundException(messageSource.getMessage("api.not.found", new Object[]{id}, LocaleContextHolder.getLocale())));
        return ApiMapper.toDTO(api);
    }

    @GetMapping("/apis/search")
    public Page<ApiResponse> searchApis(@RequestParam(required = false) String category, @RequestParam(required = false) String visibility,
                                        @RequestParam(required = false) String query, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction) {
        ApiSearchRequest request = new ApiSearchRequest();
        request.setCategory(category);
        request.setVisibility(visibility);
        request.setQuery(query);
        request.setPage(page);
        request.setSize(size);
        request.setSortBy(sortBy);
        request.setDirection(direction);
        return apiService.searchApis(request);
    }

    @GetMapping("/internal/apis/{apiId}/endpoints/{endpointId}")
    public ApiExecutionDataDTO getExecutionData(@PathVariable Integer apiId, @PathVariable Integer endpointId) {
        ApiModel api = apirepo.findById(apiId).orElseThrow(() -> new ApiNotFoundException(messageSource.getMessage("api.not.found", new Object[]{apiId}, LocaleContextHolder.getLocale())));
        ApiEndpoint endpoint = endpointRepo.findById(endpointId).orElseThrow(() -> new EndpointNotFoundException(messageSource.getMessage("endpoint.not.found", new Object[]{endpointId}, LocaleContextHolder.getLocale())));
        if (!endpoint.getApiId().equals(apiId)) {
            throw new EndpointMismatchException(messageSource.getMessage("endpoint.mismatch", new Object[]{endpointId, apiId}, LocaleContextHolder.getLocale()));
        }
        return ApiExecutionDataDTO.builder().baseUrl(api.getBaseUrl()).endpoint(endpoint.getEndpoint())
                .method(endpoint.getMethod()).headersJson(endpoint.getHeadersJson()).paramsJson(endpoint.getParamsJson())
                .sampleRequest(endpoint.getSampleRequest()).build();
    }
}
