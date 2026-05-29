package com.devapix.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "${feign.auth-service.url}")
public interface AuthServiceClient {

    @GetMapping("/auth/internal/users/{userId}/validate")
    boolean isUserValid(@PathVariable("userId") Integer userId);

}
