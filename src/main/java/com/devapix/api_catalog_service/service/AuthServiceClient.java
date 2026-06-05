package com.devapix.api_catalog_service.service;

import com.devapix.api_catalog_service.dto.response.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.*;

@FeignClient(name = "AUTH-SERVICE")
public interface AuthServiceClient {

    @GetMapping("/auth/internal/users/{userId}/validate")
    boolean isUserValid(@PathVariable("userId") Integer userId);

    @GetMapping("/auth/internal/users/batch")
    Map<Integer, UserInfoResponse> getUsersBatch(@RequestParam("ids") List<Integer> ids);
}
