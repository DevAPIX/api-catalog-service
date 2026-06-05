package com.devapix.api_catalog_service.controller;


import com.devapix.api_catalog_service.dto.PlanRequest;
import com.devapix.api_catalog_service.dto.response.PlanResponse;
import com.devapix.api_catalog_service.service.ApiPlanService;
import com.devapix.api_catalog_service.service.AuthServiceClient;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiPlanController {

    private final ApiPlanService planService;
    private final AuthServiceClient authServiceClient;
    private final MessageSource messageSource;

    @PostMapping("/api/{apiId}/plans")
    public ResponseEntity<PlanResponse> createPlan(@PathVariable Integer apiId, @Valid @RequestBody PlanRequest request, @Parameter(hidden = true) @RequestHeader("X-User-Id") String userIdStr) {
        Integer ownerId = Integer.parseInt(userIdStr);
        validateUserExists(ownerId);
        log.info("API called: POST /api/{}/plans by user {}", apiId, ownerId);
        PlanResponse response = planService.createPlan(apiId, request, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private void validateUserExists(Integer userId) {
        try {
            boolean valid = authServiceClient.isUserValid(userId);
            if (!valid) {
                throw new IllegalArgumentException(messageSource.getMessage("user.not.found", new Object[]{userId}, LocaleContextHolder.getLocale()));
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Could not validate user {} with auth-service: {}", userId, e.getMessage());
        }
    }

    @PutMapping("/api/{apiId}/plans/{planId}")
    public ResponseEntity<PlanResponse> updatePlan(@PathVariable Integer apiId, @PathVariable Integer planId, @Valid @RequestBody PlanRequest request, @Parameter(hidden = true) @RequestHeader("X-User-Id") String userIdStr) {
        Integer ownerId = Integer.parseInt(userIdStr);
        validateUserExists(ownerId);
        log.info("API called: PUT /api/{}/plans/{} by user {}", apiId, planId, ownerId);
        PlanResponse response = planService.updatePlan(apiId, planId, request, ownerId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/{apiId}/plans/{planId}")
    public ResponseEntity<Void> deletePlan(@PathVariable Integer apiId, @PathVariable Integer planId, @Parameter(hidden = true) @RequestHeader("X-User-Id") String userIdStr) {
        Integer ownerId = Integer.parseInt(userIdStr);
        validateUserExists(ownerId);
        log.info("API called: DELETE /api/{}/plans/{} by user {}", apiId, planId, ownerId);
        planService.deletePlan(apiId, planId, ownerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/{apiId}/plans")
    public ResponseEntity<List<PlanResponse>> getPlansByApiId(@PathVariable Integer apiId) {
        log.info("API called: GET /api/{}/plans", apiId);
        List<PlanResponse> plans = planService.getPlansByApiId(apiId);
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/api/{apiId}/plans/{planId}")
    public ResponseEntity<PlanResponse> getPlanById(@PathVariable Integer apiId, @PathVariable Integer planId) {
        log.info("API called: GET /api/{}/plans/{}", apiId, planId);
        PlanResponse plan = planService.getPlanById(planId);
        if (!plan.getApiId().equals(apiId)) {
            throw new IllegalArgumentException(messageSource.getMessage("plan.not.found", new Object[]{planId}, LocaleContextHolder.getLocale()));
        }
        return ResponseEntity.ok(plan);
    }

    @GetMapping("/internal/plans/{planId}")
    public ResponseEntity<PlanResponse> getPlanByIdInternal(@PathVariable Integer planId) {
        log.info("Internal API called: GET /internal/plans/{}", planId);
        PlanResponse plan = planService.getPlanById(planId);
        return ResponseEntity.ok(plan);
    }
}
