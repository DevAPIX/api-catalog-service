package com.devapix.repo;

import com.devapix.model.ApiEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiEndpointRepo extends JpaRepository<ApiEndpoint, Integer> {
}
