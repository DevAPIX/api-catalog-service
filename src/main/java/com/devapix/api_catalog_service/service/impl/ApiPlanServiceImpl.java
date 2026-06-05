package com.devapix.api_catalog_service.service.impl;

import com.devapix.api_catalog_service.dto.PlanRequest;
import com.devapix.api_catalog_service.dto.response.PlanResponse;
import com.devapix.api_catalog_service.exception.ApiNotFoundException;
import com.devapix.api_catalog_service.exception.ResourceNotFoundException;
import com.devapix.api_catalog_service.model.ApiModel;
import com.devapix.api_catalog_service.model.ApiPlan;
import com.devapix.api_catalog_service.repo.ApiPlanRepo;
import com.devapix.api_catalog_service.repo.ApiRepo;
import com.devapix.api_catalog_service.service.ApiPlanService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiPlanServiceImpl implements ApiPlanService {

    private final ApiPlanRepo planRepo;
    private final ApiRepo apiRepo;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public PlanResponse createPlan(Integer apiId, PlanRequest request, Integer ownerId) {
        log.info("Creating plan for API {} by owner {}", apiId, ownerId);
        ApiModel api = apiRepo.findById(apiId).orElseThrow(() -> new ApiNotFoundException(messageSource.getMessage("api.not.found", new Object[]{apiId}, LocaleContextHolder.getLocale())));
        if (api.getOwnerId() == null || !api.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException(messageSource.getMessage("plan.unauthorized", null, LocaleContextHolder.getLocale()));
        }
        if (planRepo.existsByApiIdAndName(apiId, request.getName())) {
            throw new IllegalArgumentException(
                    messageSource.getMessage("plan.name.exists", new Object[]{request.getName()}, LocaleContextHolder.getLocale()));
        }
        ApiPlan plan = ApiPlan.builder().apiId(apiId).name(request.getName()).description(request.getDescription()).price(request.getPrice()).requestLimit(request.getRequestLimit()).durationDays(request.getDurationDays()).isCustomPricing(request.getIsCustomPricing()).isActive(true).build();
        ApiPlan saved = planRepo.save(plan);
        log.info("Plan created successfully: id={}, name={}", saved.getId(), saved.getName());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public PlanResponse updatePlan(Integer apiId, Integer planId, PlanRequest request, Integer ownerId) {
        log.info("Updating plan {} for API {} by owner {}", planId, apiId, ownerId);
        ApiModel api = apiRepo.findById(apiId).orElseThrow(() -> new ApiNotFoundException(messageSource.getMessage("api.not.found", new Object[]{apiId}, LocaleContextHolder.getLocale())));

        if (api.getOwnerId() == null || !api.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException(messageSource.getMessage("plan.unauthorized", null, LocaleContextHolder.getLocale()));
        }

        ApiPlan plan = planRepo.findByIdAndApiId(planId, apiId).orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("plan.not.found", new Object[]{planId}, LocaleContextHolder.getLocale())));

        if (!plan.getName().equals(request.getName()) && planRepo.existsByApiIdAndName(apiId, request.getName())) {
            throw new IllegalArgumentException(messageSource.getMessage("plan.name.exists", new Object[]{request.getName()}, LocaleContextHolder.getLocale()));
        }
        plan.setName(request.getName());
        plan.setDescription(request.getDescription());
        plan.setPrice(request.getPrice());
        plan.setRequestLimit(request.getRequestLimit());
        plan.setDurationDays(request.getDurationDays());
        plan.setIsCustomPricing(request.getIsCustomPricing());
        ApiPlan saved = planRepo.save(plan);
        log.info("Plan updated successfully: id={}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deletePlan(Integer apiId, Integer planId, Integer ownerId) {
        log.info("Deleting plan {} for API {} by owner {}", planId, apiId, ownerId);
        ApiModel api = apiRepo.findById(apiId).orElseThrow(() -> new ApiNotFoundException(messageSource.getMessage("api.not.found", new Object[]{apiId}, LocaleContextHolder.getLocale())));
        if (api.getOwnerId() == null || !api.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException(messageSource.getMessage("plan.unauthorized", null, LocaleContextHolder.getLocale()));
        }
        ApiPlan plan = planRepo.findByIdAndApiId(planId, apiId).orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("plan.not.found", new Object[]{planId}, LocaleContextHolder.getLocale())));
        plan.setIsActive(false);
        planRepo.save(plan);
        log.info("Plan soft-deleted: id={}", planId);
    }

    @Override
    public List<PlanResponse> getPlansByApiId(Integer apiId) {
        log.debug("Fetching plans for API {}", apiId);
        if (!apiRepo.existsById(apiId)) {
            throw new ApiNotFoundException(messageSource.getMessage("api.not.found", new Object[]{apiId}, LocaleContextHolder.getLocale()));
        }
        return planRepo.findByApiIdAndIsActiveTrue(apiId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public PlanResponse getPlanById(Integer planId) {
        log.debug("Fetching plan {}", planId);
        ApiPlan plan = planRepo.findById(planId).orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("plan.not.found", new Object[]{planId}, LocaleContextHolder.getLocale())));
        return toResponse(plan);
    }

    @Override
    public PlanResponse getDefaultPlan(Integer apiId) {
        log.debug("Fetching default plan for API {}", apiId);
        List<ApiPlan> plans = planRepo.findByApiIdAndIsActiveTrue(apiId);
        if (plans.isEmpty()) {
            return null;
        }

        return plans.stream().filter(p -> p.getName().equalsIgnoreCase("Free")).findFirst().map(this::toResponse).orElse(toResponse(plans.get(0)));
    }

    private PlanResponse toResponse(ApiPlan plan) {
        return PlanResponse.builder().id(plan.getId()).apiId(plan.getApiId()).name(plan.getName()).description(plan.getDescription()).price(plan.getPrice()).requestLimit(plan.getRequestLimit()).durationDays(plan.getDurationDays()).isCustomPricing(plan.getIsCustomPricing()).isActive(plan.getIsActive()).createdAt(plan.getCreatedAt()).updatedAt(plan.getUpdatedAt()).build();
    }
}
