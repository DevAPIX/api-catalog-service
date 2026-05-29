package com.devapix.repo;

import com.devapix.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CategoryRepo extends JpaRepository<CategoryModel, Integer> {

    @Query("SELECT c FROM CategoryModel c WHERE LOWER(TRIM(c.name)) = LOWER(TRIM(:name))")
    List<CategoryModel> findByNameIgnoreCase(@Param("name") String name);

    boolean existsByNameIgnoreCase(String name);
}
