package com.fpt.ecommerce.controller;

import com.fpt.ecommerce.constant.PredefinedPermission; // Import file hằng số
import com.fpt.ecommerce.dto.response.ApiResponse;
import com.fpt.ecommerce.dto.response.MemberResponse;
import com.fpt.ecommerce.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User Management", description = "Quản lý người dùng & Thông tin cá nhân")
public class UserController {

    MemberService memberService;

    // 1. GET ALL USERS
    @Operation(summary = "Lấy danh sách tất cả người dùng", description = "Yêu cầu quyền MANAGE_USERS (Admin)")
    @GetMapping
    @PreAuthorize("hasAuthority('" + PredefinedPermission.MANAGE_USERS + "')")
    public ApiResponse<List<MemberResponse>> getUsers() {
        return ApiResponse.<List<MemberResponse>>builder()
                .result(memberService.getUsers())
                .build();
    }

    // 2. GET USER BY ID
    @Operation(summary = "Lấy chi tiết user theo ID", description = "Yêu cầu quyền MANAGE_USERS (Admin)")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + PredefinedPermission.MANAGE_USERS + "')")
    public ApiResponse<MemberResponse> getUser(@PathVariable Long id) {
        return ApiResponse.<MemberResponse>builder()
                .result(memberService.getUser(id))
                .build();
    }

    // 3. GET MY INFO
    @Operation(summary = "Lấy thông tin cá nhân", description = "Dành cho user đang đăng nhập (dựa trên Token)")
    @GetMapping("/my-info")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<MemberResponse> getMyInfo() {
        return ApiResponse.<MemberResponse>builder()
                .result(memberService.getMyInfo())
                .build();
    }
}