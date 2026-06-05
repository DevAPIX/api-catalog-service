package com.devapix.api_catalog_service.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "api_plans",
        uniqueConstraints = @UniqueConstraint(columnNames = {"api_id", "name"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "api_id", nullable = false)
    private Integer apiId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "request_limit", nullable = false)
    private Integer requestLimit;

    @Column(name = "duration_days")
    private Integer durationDays;

    @Column(name = "is_custom_pricing", nullable = false)
    private Boolean isCustomPricing = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}
