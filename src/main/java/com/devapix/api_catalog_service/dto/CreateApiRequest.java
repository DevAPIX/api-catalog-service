package com.devapix.api_catalog_service.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateApiRequest {

    @NotBlank(message = "{api.name.empty}")
    private String name;
    
    @NotBlank(message = "{api.description.empty}")
    private String description;
    
    @NotBlank(message = "{api.baseUrl.empty}")
    private String baseUrl;
    
    @NotNull(message = "{api.categoryId.null}")
    private Integer categoryId;
    
    private String visibility = "PUBLIC";
}
