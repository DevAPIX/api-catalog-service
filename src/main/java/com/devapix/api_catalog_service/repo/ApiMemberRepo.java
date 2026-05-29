package com.devapix.api_catalog_service.repo;

import com.devapix.api_catalog_service.model.ApiMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiMemberRepo extends JpaRepository<ApiMember, Integer> {
}
