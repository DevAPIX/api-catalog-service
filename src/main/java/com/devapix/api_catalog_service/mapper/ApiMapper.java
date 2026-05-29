package com.devapix.mapper;
import com.devapix.dto.response.ApiResponse;
import com.devapix.model.ApiModel;

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
        dto.setPrice(api.getPrice());
        dto.setRequestLimit(api.getRequestLimit());
        dto.setCreatedAt(api.getCreatedAt());
        dto.setUpdatedAt(api.getUpdatedAt());
        return dto;
    }
}
