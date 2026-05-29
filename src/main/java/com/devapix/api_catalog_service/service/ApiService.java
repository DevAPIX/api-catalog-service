package com.devapix.service;

import com.devapix.dto.ApiSearchRequest;
import com.devapix.dto.CreateApiRequest;
import com.devapix.dto.response.ApiResponse;
import org.springframework.data.domain.Page;

public interface ApiService {
    Page<ApiResponse> searchApis(ApiSearchRequest request);
    ApiResponse publishApi(CreateApiRequest request, Integer ownerId);
}
