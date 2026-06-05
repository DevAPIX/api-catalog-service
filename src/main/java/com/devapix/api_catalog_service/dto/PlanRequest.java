package com.devapix.api_catalog_service.dto;


import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class PlanRequest {

    @NotBlank(message = "{plan.name.required}")
    @Size(max = 50, message = "{plan.name.size}")
    private String name;

    @Size(max = 255, message = "{plan.description.size}")
    private String description;

    @NotNull(message = "{plan.price.required}")
    @DecimalMin(value = "0.00", message = "{plan.price.min}")
    @DecimalMax(value = "999999.99", message = "{plan.price.max}")
    private BigDecimal price;

    @NotNull(message = "{plan.limit.required}")
    @Min(value = -1, message = "{plan.limit.min}")
    private Integer requestLimit;

    @Min(value = 1, message = "{plan.duration.min}")
    private Integer durationDays;

    private Boolean isCustomPricing = false;
}
