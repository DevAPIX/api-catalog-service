package com.devapix.api_catalog_service.task;

import com.devapix.api_catalog_service.model.ApiModel;
import com.devapix.api_catalog_service.repo.ApiRepo;
import com.devapix.api_catalog_service.service.SubscriptionServiceClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiCleanupScheduler {

    private final ApiRepo apiRepo;
    private final SubscriptionServiceClient subscriptionServiceClient;

    @Scheduled(cron = "0 0 * * * *")
    public void cleanupDeprecatedApis() {
        log.info("Running API cleanup scheduler...");
        List<ApiModel> deprecatedApis = apiRepo.findByStatus("DEPRECATED");
        for (ApiModel api : deprecatedApis) {
            try {
                boolean hasActive = subscriptionServiceClient.hasActiveSubscriptionsForApi(api.getId());
                if (!hasActive) {
                    log.info("API {} is DEPRECATED and has no active subscriptions. Soft deleting...", api.getId());
                    api.setStatus("DELETED");
                    apiRepo.save(api);
                    try {
                        subscriptionServiceClient.revokeSubscriptionsByApi(api.getId());
                        log.info("Revoked subscriptions for auto-deleted API {}", api.getId());
                    } catch (Exception e) {
                        log.warn("Failed to revoke subscriptions for auto-deleted API {}: {}", api.getId(), e.getMessage());
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to process cleanup for API {}: {}", api.getId(), e.getMessage());
            }
        }
        log.info("Finished API cleanup scheduler.");
    }
}
