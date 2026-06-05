package com.devapix.api_catalog_service.repo;

import  java.util.*;
import com.devapix.api_catalog_service.model.ApiModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApiRepo extends JpaRepository<ApiModel,Integer> , JpaSpecificationExecutor<ApiModel> {
    @Query("SELECT a FROM ApiModel a WHERE a.categoryId = :id")
    ApiModel searchByKeyword(@Param("id") Integer id);

    @Query("""
        SELECT a FROM ApiModel a
        WHERE (:categoryId IS NULL OR a.categoryId = :categoryId)
        AND (:visibility IS NULL OR a.visibility = :visibility)
    """)
    Page<ApiModel> findApis(@Param("categoryId") Integer categoryId, @Param("visibility") String visibility, Pageable pageable);
    boolean existsByNameAndStatusNot(String name, String status);
    boolean existsByBaseUrlAndStatusNot(String baseUrl, String status);
    List<ApiModel> findByCategoryId(Integer categoryId);
   List<ApiModel> findByStatus(String status);
}
