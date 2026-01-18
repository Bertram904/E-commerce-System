package com.fpt.ecommerce.controller;

import com.fpt.ecommerce.constant.PredefinedPermission; // Import Constant
import com.fpt.ecommerce.dto.request.SupplierRequest;
import com.fpt.ecommerce.dto.response.ApiResponse;
import com.fpt.ecommerce.dto.response.SupplierResponse;
import com.fpt.ecommerce.service.SupplierService;
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
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Supplier Management", description = "Quản lý nhà cung cấp")
public class SupplierController {

    SupplierService supplierService;

    // 1. GET ALL
    @Operation(summary = "Lấy danh sách nhà cung cấp", description = "Cần quyền VIEW_SUPPLIER (Admin, Staff)")
    @GetMapping
    @PreAuthorize("hasAuthority('" + PredefinedPermission.VIEW_SUPPLIER + "')")
    public ApiResponse<List<SupplierResponse>> getAllSuppliers() {
        return ApiResponse.<List<SupplierResponse>>builder()
                .result(supplierService.getAllSuppliers())
                .build();
    }

    // 2. GET BY ID
    @Operation(summary = "Xem chi tiết nhà cung cấp", description = "Cần quyền VIEW_SUPPLIER")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + PredefinedPermission.VIEW_SUPPLIER + "')")
    public ApiResponse<SupplierResponse> getSupplier(@PathVariable Long id) {
        return ApiResponse.<SupplierResponse>builder()
                .result(supplierService.getSupplierById(id))
                .build();
    }

    // 3. CREATE
    @Operation(summary = "Tạo nhà cung cấp mới", description = "Cần quyền CREATE_SUPPLIER")
    @PostMapping
    @PreAuthorize("hasAuthority('" + PredefinedPermission.CREATE_SUPPLIER + "')")
    public ApiResponse<SupplierResponse> createSupplier(@RequestBody @Valid SupplierRequest request) {
        return ApiResponse.<SupplierResponse>builder()
                .message("supplier.create.success")
                .result(supplierService.createSupplier(request))
                .build();
    }

    // 4. UPDATE
    @Operation(summary = "Cập nhật thông tin", description = "Cần quyền UPDATE_SUPPLIER")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + PredefinedPermission.UPDATE_SUPPLIER + "')")
    public ApiResponse<SupplierResponse> updateSupplier(@PathVariable Long id, @RequestBody @Valid SupplierRequest request) {
        return ApiResponse.<SupplierResponse>builder()
                .message("supplier.update.success")
                .result(supplierService.updateSupplier(id, request))
                .build();
    }

    // 5. DELETE
    @Operation(summary = "Xóa nhà cung cấp", description = "Cần quyền DELETE_SUPPLIER")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + PredefinedPermission.DELETE_SUPPLIER + "')")
    public ApiResponse<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ApiResponse.<Void>builder()
                .message("supplier.delete.success")
                .build();
    }
}