package com.fpt.ecommerce.controller;

import com.fpt.ecommerce.dto.request.CartRequest;
import com.fpt.ecommerce.dto.response.ApiResponse;
import com.fpt.ecommerce.dto.response.CartResponse;
import com.fpt.ecommerce.exception.AppException;
import com.fpt.ecommerce.exception.ErrorCode;
import com.fpt.ecommerce.security.jwt.JwtUtils;
import com.fpt.ecommerce.service.cart.CartService;
import io.jsonwebtoken.Jwt;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Cart Management", description = "Quan ly gio hang ca nhan")
@Slf4j
public class CartController {

    CartService cartService;
    JwtUtils jwtUtils;
    HttpServletRequest request;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<CartResponse> getMyCart() {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.getMyCart(getCurrentUserId()))
                .build();
    }

    //Them san pham vao gio
    @PostMapping("/items")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> addToCart(@RequestBody @Valid CartRequest cartRequest) {
        cartService.addToCart(getCurrentUserId(), cartRequest);
        return ApiResponse.<Void>builder()
                .message("add to cart success!")
                .build();
    }

    //Ca[ nhat so luong item
    @PutMapping("/items/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> updateCartItem(@PathVariable Long itemId, @RequestParam Integer quantity) {
        cartService.updateItemQuantity(getCurrentUserId(), itemId, quantity);

        return ApiResponse.<Void>builder()
                .message("update cart success!")
                .build();
    }

    //Helper
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
