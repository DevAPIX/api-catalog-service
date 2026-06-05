package com.devapix.api_catalog_service.service;


import com.devapix.api_catalog_service.dto.PlanRequest;
import com.devapix.api_catalog_service.dto.response.PlanResponse;
import java.util.List;

public interface ApiPlanService {

    PlanResponse createPlan(Integer apiId, PlanRequest request, Integer ownerId);

    PlanResponse updatePlan(Integer apiId, Integer planId, PlanRequest request, Integer ownerId);

    void deletePlan(Integer apiId, Integer planId, Integer ownerId);

    List<PlanResponse> getPlansByApiId(Integer apiId);

    PlanResponse getPlanById(Integer planId);

    PlanResponse getDefaultPlan(Integer apiId);
}
