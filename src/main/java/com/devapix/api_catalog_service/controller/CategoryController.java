package com.devapix.api_catalog_service.controller;

import com.devapix.api_catalog_service.dto.CategoryUpdateRequest;
import com.devapix.api_catalog_service.model.CategoryModel;
import com.devapix.api_catalog_service.service.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<String> addCategory(@Valid @RequestBody CategoryModel category) {
        String response = service.addCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryModel>> getCategories() {
        return ResponseEntity.ok(service.getAllCategories());
    }

    @PutMapping
    public ResponseEntity<CategoryModel> updateCategory(@Valid @RequestBody CategoryUpdateRequest request) {
        CategoryModel updated = service.updateCategory(request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer id) {
        String response = service.deleteCategory(id);
        return ResponseEntity.ok(response);
    }
}
