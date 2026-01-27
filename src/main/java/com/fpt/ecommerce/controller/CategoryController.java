package com.fpt.ecommerce.controller;

import com.fpt.ecommerce.dto.request.CategoryRequest;
import com.fpt.ecommerce.dto.response.ApiResponse;
import com.fpt.ecommerce.entity.Category;
import com.fpt.ecommerce.service.product.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Category Management", description = "Quản lý danh mục sản phẩm")
public class CategoryController {

    CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Lấy toàn bộ danh mục")
    public ApiResponse<List<Category>> getAll() {
        return ApiResponse.<List<Category>>builder()
                .code(1000)
                .result(categoryService.getAllCategories())
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @Operation(summary = "Tạo danh mục mới")
    public ApiResponse<Category> create(@RequestBody @Valid CategoryRequest request) {
        return ApiResponse.<Category>builder()
                .code(1000)
                .message("Tạo danh mục thành công")
                .result(categoryService.createCategory(request))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @Operation(summary = "Cập nhật danh mục")
    public ApiResponse<Category> update(@PathVariable Long id, @RequestBody @Valid CategoryRequest request) {
        return ApiResponse.<Category>builder()
                .code(1000)
                .result(categoryService.updateCategory(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Xóa danh mục")
    public ApiResponse<String> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.<String>builder()
                .code(1000)
                .result("Xóa danh mục thành công")
                .build();
    }
}