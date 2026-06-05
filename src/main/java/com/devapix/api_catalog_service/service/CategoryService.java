package com.devapix.api_catalog_service.service;

import com.devapix.api_catalog_service.dto.CategoryUpdateRequest;
import com.devapix.api_catalog_service.model.CategoryModel;
import java.util.List;

public interface CategoryService {
    String addCategory(CategoryModel category);
    List<CategoryModel> getAllCategories();
    CategoryModel updateCategory(CategoryUpdateRequest request);
    String deleteCategory(Integer id);
}
