package com.fpt.ecommerce.controller;

import com.fpt.ecommerce.dto.request.LoginRequest;
import com.fpt.ecommerce.dto.request.LogoutRequest;
import com.fpt.ecommerce.dto.request.RefreshTokenRequest;
import com.fpt.ecommerce.dto.request.RegisterRequest;
import com.fpt.ecommerce.dto.response.ApiResponse;
import com.fpt.ecommerce.dto.response.AuthResponse;
import com.fpt.ecommerce.dto.response.MemberResponse;
import com.fpt.ecommerce.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;
    private final RestClient.Builder builder;

    @Operation(summary = "Register new account", description = "Create new user with USER role default")
    @PostMapping("/register")
    public ApiResponse<MemberResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ApiResponse.<MemberResponse>builder()
                .message("User registered successfully!")
                .result(authService.register(request))
                .build();
    }

    @Operation(summary = "login to system", description = "return access token and user's information")
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ApiResponse.<AuthResponse>builder()
                .message("Login successfully!")
                .result(authService.login(request))
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ApiResponse.<AuthResponse>builder()
                .result(authService.refreshToken(request))
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout
            (HttpServletRequest request,
             LogoutRequest logoutRequest) {
        authService.logout(request, logoutRequest);
        return ResponseEntity.ok(
          ApiResponse.<Void>builder()
                  .message("Logout successfully!")
                  .build()
        );
    }
}
