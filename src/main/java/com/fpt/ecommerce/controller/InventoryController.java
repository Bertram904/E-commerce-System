package com.fpt.ecommerce.controller;

import com.fpt.ecommerce.constant.PredefinedPermission;
import com.fpt.ecommerce.dto.request.ImportRequest;
import com.fpt.ecommerce.dto.response.ApiResponse;
import com.fpt.ecommerce.dto.response.ImportInvoiceResponse;
import com.fpt.ecommerce.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/inventory")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Inventory Management", description = "Inventory Management & Import Goods")
public class InventoryController {

    InventoryService inventoryService;

    @PostMapping("/import")
    @PreAuthorize("hasAuthority('"+ PredefinedPermission.ROLE_STAFF + "')")
    public ApiResponse<ImportInvoiceResponse> importGoods(@RequestBody @Valid ImportRequest request) {
        String currentStaffUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        ImportInvoiceResponse result = inventoryService.importGoods(request, currentStaffUsername);

        return ApiResponse.<ImportInvoiceResponse>builder()
                .message("Import goods successfully")
                .result(result)
                .build();
    }

}
