package com.devapix.api_catalog_service.service.impl;

import com.devapix.api_catalog_service.dto.CategoryUpdateRequest;
import com.devapix.api_catalog_service.exception.*;
import com.devapix.api_catalog_service.model.CategoryModel;
import com.devapix.api_catalog_service.repo.CategoryRepo;
import com.devapix.api_catalog_service.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo repo;
    private final MessageSource messageSource;

    @Override
    public String addCategory(CategoryModel category) {
        String trimmedName = (category.getName() != null) ? category.getName().trim() : null;
        category.setName(trimmedName);
        if (repo.existsByNameIgnoreCase(trimmedName)) {throw new BadRequestException(messageSource.getMessage("category.already.exists", new Object[]{trimmedName}, LocaleContextHolder.getLocale()));}
        repo.save(category);
        return messageSource.getMessage("category.save.success", null, LocaleContextHolder.getLocale());
    }

    @Override
    public List<CategoryModel> getAllCategories() {
        return repo.findAll();
    }

    @Override
    public CategoryModel updateCategory(CategoryUpdateRequest request) {
        CategoryModel existing = repo.findById(request.getId()).orElseThrow(() -> new CategoryNotFoundException(messageSource.getMessage("category.not.found", new Object[]{request.getId()}, LocaleContextHolder.getLocale())));
        existing.setName(request.getName().trim());
        existing.setDescription(request.getDescription().trim());
        return repo.save(existing);
    }

    @Override
    public String deleteCategory(Integer id) {
        if (!repo.existsById(id)) {
            throw new CategoryNotFoundException(messageSource.getMessage("category.not.found", new Object[]{id}, LocaleContextHolder.getLocale()));
        }
        repo.deleteById(id);
        return messageSource.getMessage("category.delete.success", null, LocaleContextHolder.getLocale());
    }
}
