package com.devapix.api_catalog_service.dto.response;


import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanResponse {

    private Integer id;
    private Integer apiId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer requestLimit;
    private Integer durationDays;
    private Boolean isCustomPricing;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;
}
