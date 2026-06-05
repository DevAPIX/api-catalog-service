package com.devapix.api_catalog_service.mapper;

import com.devapix.api_catalog_service.dto.response.ApiResponse;
import com.devapix.api_catalog_service.model.ApiModel;

public class ApiMapper {

    public static ApiResponse toDTO(ApiModel api) {
        ApiResponse dto = new ApiResponse();
        dto.setId(api.getId());
        dto.setOwnerId(api.getOwnerId());
        dto.setCategoryId(api.getCategoryId());
        dto.setName(api.getName());
        dto.setDescription(api.getDescription());
        dto.setBaseUrl(api.getBaseUrl());
        dto.setVisibility(api.getVisibility());
        dto.setStatus(api.getStatus());
        dto.setCreatedAt(api.getCreatedAt());
        dto.setUpdatedAt(api.getUpdatedAt());
        return dto;
    }
}
