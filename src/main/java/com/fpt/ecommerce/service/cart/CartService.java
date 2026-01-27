package com.fpt.ecommerce.service;

import com.fpt.ecommerce.dto.request.CartRequest;
import com.fpt.ecommerce.dto.response.CartResponse;
import com.fpt.ecommerce.entity.Cart;
import com.fpt.ecommerce.entity.Product;
import com.fpt.ecommerce.exception.AppException;
import com.fpt.ecommerce.exception.ErrorCode;
import com.fpt.ecommerce.repository.CartItemRepository;
import com.fpt.ecommerce.repository.CartRepository;
import com.fpt.ecommerce.repository.MemberRepository;
import com.fpt.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartService {
    CartRepository cartRepository;
    ProductRepository productRepository;
    MemberRepository memberRepository;
    CartItemRepository cartItemRepository;

    public void addToCart(Long memberId, CartRequest cartRequest) {
        log.info("Request add to cart: memberId={}, productId={}, quantity={}",
                memberId, cartRequest.getProductId(), cartRequest.getQuantity());
        //1. Kiem tra product co ton tai va con hang khong
        Product product = productRepository.findById(cartRequest.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getStockQuantity() < cartRequest.getQuantity()) {
            throw new AppException(ErrorCode.OUT_OF_STOCK);
        }

        //2. Lay Cart or create
        Cart cart = cartRepository.findByMemberId(memberId)
                .orElse

    }

}
