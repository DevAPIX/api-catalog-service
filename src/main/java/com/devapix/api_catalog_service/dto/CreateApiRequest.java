package com.devapix.api_catalog_service.dto;

import jakarta.validation.constraints.Min;
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
    
    @NotBlank(message = "{api.visibility.empty}")
    private String visibility;
    
    @Min(value = 0, message = "{api.price.negative}")
    private int price;
    
    @Min(value = 1, message = "{api.limit.invalid}")
    private int requestLimit;
}
