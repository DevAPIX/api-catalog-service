package com.devapix.repo;

import com.devapix.model.ApiMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiMemberRepo extends JpaRepository<ApiMember, Integer> {
}
