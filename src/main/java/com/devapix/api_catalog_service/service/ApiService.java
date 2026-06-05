package com.devapix.api_catalog_service.service;


import com.devapix.api_catalog_service.dto.ApiSearchRequest;
import com.devapix.api_catalog_service.dto.CreateApiRequest;
import com.devapix.api_catalog_service.dto.response.ApiResponse;
import org.springframework.data.domain.Page;

public interface ApiService {
    Page<ApiResponse> searchApis(ApiSearchRequest request);
    ApiResponse publishApi(CreateApiRequest request, Integer ownerId);
}
