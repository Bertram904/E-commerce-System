package com.fpt.ecommerce.controller;

import com.fpt.ecommerce.dto.request.ProductRequest;
import com.fpt.ecommerce.dto.response.ApiResponse;
import com.fpt.ecommerce.dto.response.ProductResponse;
import com.fpt.ecommerce.service.product.ProductService;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Product Management", description = "Quản lý sản phẩm (Public & Admin)")
public class ProductController {

    ProductService productService;

    @GetMapping
    @Operation(summary = "Lấy danh sách sản phẩm", description = "API Public cho trang chủ/trang danh sách")
    public ApiResponse<List<ProductResponse>> getAllProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .code(1000)
                .result(productService.getAllProducts())
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Xem chi tiết sản phẩm", description = "API Public xem chi tiết")
    public ApiResponse<ProductResponse> getProductById(@PathVariable Long id) {
        return ApiResponse.<ProductResponse>builder()
                .code(1000)
                .result(productService.getProductById(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')") // Bảo mật
    @Operation(summary = "Tạo sản phẩm mới", description = "Chỉ dành cho Admin/Staff")
    public ApiResponse<ProductResponse> createProduct(@RequestBody @Valid ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .code(1000)
                .message("Tạo sản phẩm thành công")
                .result(productService.createProduct(request))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @Operation(summary = "Cập nhật sản phẩm", description = "Cập nhật thông tin, giá, tồn kho")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .code(1000)
                .message("Cập nhật thành công")
                .result(productService.updateProduct(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Xóa sản phẩm (Soft Delete)", description = "Sản phẩm sẽ bị ẩn đi, không xóa khỏi DB")
    public ApiResponse<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ApiResponse.<String>builder()
                .code(1000)
                .result("Đã xóa sản phẩm thành công")
                .build();
    }
}
