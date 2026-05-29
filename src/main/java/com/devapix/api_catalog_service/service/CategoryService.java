package com.devapix.api_catalog_service.service;

import com.devapix.api_catalog_service.model.CategoryModel;

import java.util.List;

public interface CategoryService {
    String addCategory(CategoryModel category);
    List<CategoryModel> getAllCategories();
    CategoryModel updateCategory(CategoryModel category);
    String deleteCategory(Integer id);
}
