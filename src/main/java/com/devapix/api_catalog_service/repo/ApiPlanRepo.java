package com.devapix.api_catalog_service.repo;

import com.devapix.api_catalog_service.model.ApiPlan;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiPlanRepo extends JpaRepository<ApiPlan, Integer> {

    List<ApiPlan> findByApiIdAndIsActiveTrue(Integer apiId);

    Optional<ApiPlan> findByApiIdAndName(Integer apiId, String name);

    Optional<ApiPlan> findByIdAndApiId(Integer id, Integer apiId);

    boolean existsByApiIdAndName(Integer apiId, String name);

    List<ApiPlan> findByApiId(Integer apiId);
}
