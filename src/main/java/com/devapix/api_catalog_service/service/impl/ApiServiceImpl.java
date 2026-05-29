package com.devapix.service.impl;

import com.devapix.dto.ApiSearchRequest;
import com.devapix.dto.CreateApiRequest;
import com.devapix.dto.response.ApiResponse;
import com.devapix.mapper.ApiMapper;
import com.devapix.model.ApiMember;
import com.devapix.model.ApiModel;
import com.devapix.model.CategoryModel;
import com.devapix.repo.ApiMemberRepo;
import com.devapix.repo.ApiRepo;
import com.devapix.repo.CategoryRepo;
import com.devapix.service.ApiService;
import com.devapix.spec.ApiSpecification;
import com.devapix.exception.BadRequestException;
import com.devapix.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiServiceImpl implements ApiService {

    private final ApiRepo apiRepo;
    private final CategoryRepo categoryRepo;
    private final ApiMemberRepo apiMemberRepo;
    private final MessageSource messageSource;

    @Value("${role.owner}")
    private String ownerRole;

    @Override
    @Transactional
    public ApiResponse publishApi(CreateApiRequest request, Integer ownerId) {

        if (apiRepo.existsByName(request.getName())) {
            throw new BadRequestException(messageSource.getMessage("api.name.exists", new Object[]{request.getName()}, LocaleContextHolder.getLocale()));
        }
        if (apiRepo.existsByBaseUrl(request.getBaseUrl())) {
            throw new BadRequestException(messageSource.getMessage("api.baseUrl.exists", new Object[]{request.getBaseUrl()}, LocaleContextHolder.getLocale()));
        }
        if (!categoryRepo.existsById(request.getCategoryId())) {
            throw new CategoryNotFoundException(
                    messageSource.getMessage("category.not.found", new Object[]{request.getCategoryId()}, LocaleContextHolder.getLocale()));
        }
        ApiModel api = new ApiModel();
        api.setOwnerId(ownerId);
        api.setCategoryId(request.getCategoryId());
        api.setName(request.getName());
        api.setDescription(request.getDescription());
        api.setBaseUrl(request.getBaseUrl());
        api.setVisibility(request.getVisibility());
        api.setPrice(request.getPrice());
        api.setRequestLimit(request.getRequestLimit());
        ApiModel savedApi = apiRepo.save(api);
        ApiMember apiMember = ApiMember.builder().apiId(savedApi.getId()).userId(ownerId).role(ownerRole).build();
        apiMemberRepo.save(apiMember);
        return ApiMapper.toDTO(savedApi);
    }

    @Override
    public Page<ApiResponse> searchApis(ApiSearchRequest request) {

        int page = request.getPage() < 0 ? 0 : request.getPage();
        int size = request.getSize() <= 0 ? 10 : request.getSize();
        String sortBy = (request.getSortBy() == null || request.getSortBy().isEmpty()) ? "id" : request.getSortBy();
        String direction = (request.getDirection() == null || request.getDirection().isEmpty()) ? "asc" : request.getDirection();
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Integer categoryId = null;

        if (request.getCategory() != null && !request.getCategory().trim().isEmpty()) {
            String categoryName = request.getCategory().trim();
            log.debug("Searching for category: '{}'", categoryName);
            List<CategoryModel> categories = categoryRepo.findByNameIgnoreCase(categoryName);
            if (categories.isEmpty()) {
                log.warn("No category found matching: '{}'", categoryName);
                return Page.empty(pageable);
            }
            categoryId = categories.get(0).getId();
            log.debug("Using Category ID: {} for search term: '{}'", categoryId, categoryName);
        }
        String visibility = (request.getVisibility() != null) ? request.getVisibility().trim() : null;
        Page<ApiModel> result = apiRepo.findAll(ApiSpecification.filterApis(categoryId, visibility, request.getQuery(), "%"), pageable);
        log.debug("Search returned {} results", result.getTotalElements());
        return result.map(ApiMapper::toDTO);
    }

}
