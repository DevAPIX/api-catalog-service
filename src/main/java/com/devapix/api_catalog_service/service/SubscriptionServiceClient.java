package com.devapix.api_catalog_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "API-SUBSCRIPTION-SERVICE")
public interface SubscriptionServiceClient {

    @DeleteMapping("/subscriptions/internal/api/{apiId}")
    void revokeSubscriptionsByApi(@PathVariable("apiId") Integer apiId);

    @GetMapping("/subscriptions/internal/api/{apiId}/has-active")
    boolean hasActiveSubscriptionsForApi(@PathVariable("apiId") Integer apiId);
}
