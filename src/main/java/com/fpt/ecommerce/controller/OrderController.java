package com.fpt.ecommerce.controller;

import com.fpt.ecommerce.dto.request.OrderRequest;
import com.fpt.ecommerce.dto.response.ApiResponse;
import com.fpt.ecommerce.dto.response.OrderResponse;
import com.fpt.ecommerce.exception.AppException;
import com.fpt.ecommerce.exception.ErrorCode;
import com.fpt.ecommerce.security.jwt.JwtUtils;
import com.fpt.ecommerce.service.checkout.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "OrderManagement")
public class OrderController {

    OrderService orderService;
    JwtUtils jwtUtils;
    HttpServletRequest request;

    @Operation(summary = "Checkout")
    @PostMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<OrderResponse> checkout(@RequestBody @Valid OrderRequest orderRequest) {
        Long userId = getCurrentUserId();

        return ApiResponse.<OrderResponse>builder()
                .code(1000)
                .message("Order successfully!")
                .result(orderService.checkout(userId, orderRequest))
                .build();
    }

    //helper
    private Long getCurrentUserId() {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Long userId = jwtUtils.extractUserId(token);
            if (userId != null) {
                return userId;
            }
        }
        throw new AppException(ErrorCode.UNAUTHORIZED);
    }
}
