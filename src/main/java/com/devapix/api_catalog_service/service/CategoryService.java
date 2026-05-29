package com.devapix.service;

import com.devapix.model.CategoryModel;

import java.util.List;

public interface CategoryService {
    String addCategory(CategoryModel category);
    List<CategoryModel> getAllCategories();
    CategoryModel updateCategory(CategoryModel category);
    String deleteCategory(Integer id);
}
