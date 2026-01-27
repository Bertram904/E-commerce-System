package com.fpt.ecommerce.service;

import com.fpt.ecommerce.dto.request.CategoryRequest;
import com.fpt.ecommerce.entity.Category;
import com.fpt.ecommerce.exception.AppException;
import com.fpt.ecommerce.exception.ErrorCode;
import com.fpt.ecommerce.mapper.CategoryMapper;
import com.fpt.ecommerce.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Transactional
    public Category createCategory(CategoryRequest request) {
        log.info("Creating new category: {}", request.getName());
        Category category = categoryMapper.toCategory(request);
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long id, CategoryRequest request) {
        log.info("Updating category ID: {}", id);
        Category category = getCategoryById(id);
        categoryMapper.updateCategory(category, request);
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        log.info("Deleting category ID: {}", id);
        Category category = getCategoryById(id);
        if (!category.getProducts().isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_HAS_PRODUCTS);
        }

        categoryRepository.delete(category);
    }
}