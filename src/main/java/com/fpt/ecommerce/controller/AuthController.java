package com.fpt.ecommerce.controller;

import com.fpt.ecommerce.dto.request.LoginRequest;
import com.fpt.ecommerce.dto.request.RegisterRequest;
import com.fpt.ecommerce.dto.response.ApiResponse;
import com.fpt.ecommerce.dto.response.AuthResponse;
import com.fpt.ecommerce.entity.Member;
import com.fpt.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<Member> register(@RequestBody @Valid RegisterRequest request) {
        return ApiResponse.<Member>builder()
                .result(authService.register(request))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ApiResponse.<AuthResponse>builder()
                .result(authService.login(request))
                .build();
    }
}
