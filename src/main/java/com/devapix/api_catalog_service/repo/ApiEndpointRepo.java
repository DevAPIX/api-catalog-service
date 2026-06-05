package com.devapix.api_catalog_service.repo;

import com.devapix.api_catalog_service.model.ApiEndpoint;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiEndpointRepo extends JpaRepository<ApiEndpoint, Integer> {
    List<ApiEndpoint> findByApiId(Integer apiId);
}
