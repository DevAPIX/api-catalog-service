package com.devapix.api_catalog_service.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryUpdateRequest {

    @NotNull(message = "{category.id.null}")
    private Integer id;

    @NotBlank(message = "{category.name.blank}")
    private String name;

    @NotBlank(message = "{category.description.blank}")
    private String description;
}
