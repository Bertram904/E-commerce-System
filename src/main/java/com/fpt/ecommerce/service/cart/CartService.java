package com.fpt.ecommerce.service.cart;

import com.fpt.ecommerce.dto.request.CartRequest;
import com.fpt.ecommerce.dto.response.CartResponse;
import com.fpt.ecommerce.entity.Product;

public interface CartService {
    /**
     * Them san pham vao gio hang
     * Lay danh sach san pham trong gio cua User
     * Cap nhat so luong item
     * Xoa 1 item
     * Xoa ca gio hang sau khi CheckOut thanh cong
     */
    void addToCart(Long memberId, CartRequest cartRequest);
    CartResponse getMyCart(Long memberId);
    void updateItemQuantity(Long memberId, Long productId, Integer quantity);
    void removeItemFromCart(Long memberId, Long cartItemId);
    void clearCart(Long memberId);
}