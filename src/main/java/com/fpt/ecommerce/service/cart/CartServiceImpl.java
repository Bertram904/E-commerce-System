package com.fpt.ecommerce.service.cart;

import com.fpt.ecommerce.dto.request.CartRequest;
import com.fpt.ecommerce.dto.response.CartResponse;
import com.fpt.ecommerce.entity.Cart;
import com.fpt.ecommerce.entity.CartItem;
import com.fpt.ecommerce.entity.Member;
import com.fpt.ecommerce.entity.Product;
import com.fpt.ecommerce.exception.AppException;
import com.fpt.ecommerce.exception.ErrorCode;
import com.fpt.ecommerce.mapper.CartMapper;
import com.fpt.ecommerce.repository.CartItemRepository;
import com.fpt.ecommerce.repository.CartRepository;
import com.fpt.ecommerce.repository.MemberRepository;
import com.fpt.ecommerce.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartServiceImpl implements CartService {

    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    ProductRepository productRepository;
    MemberRepository memberRepository;
    CartMapper cartMapper;

    @Override
    @Transactional
    public void addToCart(Long memberId, CartRequest cartRequest) {
        log.info("Member {} adding product {} to cart", memberId, cartRequest.getProductId());

        Product product = productRepository.findById(cartRequest.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getStockQuantity() < cartRequest.getQuantity()) {
            throw new AppException(ErrorCode.OUT_OF_STOCK);
        }

        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseGet(() -> createNewCart(memberId));
        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        if (existingItemOpt.isPresent()) {
            // CASE A: Đã có -> Cộng dồn số lượng
            CartItem existingItem = existingItemOpt.get();
            int newQuantity = existingItem.getQuantity() + cartRequest.getQuantity();

            if (product.getStockQuantity() < newQuantity) {
                throw new AppException(ErrorCode.OUT_OF_STOCK);
            }
            existingItem.setQuantity(newQuantity);
            cartItemRepository.save(existingItem);
        } else {
            // CASE B: Chưa có -> Tạo item mới
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(cartRequest.getQuantity())
                    .build();
            cartItemRepository.save(newItem);
        }
    }

    @Override
    public CartResponse getMyCart(Long memberId) {
        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseGet(() -> createNewCart(memberId));

        CartResponse cartResponse = cartMapper.toCartResponse(cart);

        calculateCartTotals(cart, cartResponse);

        return cartResponse;
    }

    @Override
    @Transactional
    public void updateItemQuantity(Long memberId, Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND));

        validateCartOwnership(memberId, cartItem.getCart());

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            // Check tồn kho
            if (cartItem.getProduct().getStockQuantity() < quantity) {
                throw new AppException(ErrorCode.OUT_OF_STOCK);
            }
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    @Transactional
    public void removeItemFromCart(Long memberId, Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND));

        validateCartOwnership(memberId, item.getCart());

        cartItemRepository.delete(item);
    }

    @Override
    @Transactional
    public void clearCart(Long memberId) {
        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        cartItemRepository.deleteAllInBatch(cart.getCartItems());
    }

    private Cart createNewCart(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return cartRepository.save(Cart.builder()
                .member(member)
                .cartItems(new HashSet<>())
                .build());
    }

    private void validateCartOwnership(Long memberId, Cart cart) {
        if (!cart.getMember().getId().equals(memberId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    private void calculateCartTotals(Cart cart, CartResponse cartResponse) {
        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItems == null || cartItems.isEmpty()) {
            cartResponse.setTotalPrice(BigDecimal.ZERO);
            cartResponse.setTotalItems(0);
            return;
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalItems = 0;

        for (CartItem cartItem : cartItems) {
            BigDecimal price = cartItem.getProduct().getPrice();
            BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity());

            totalPrice = totalPrice.add(price.multiply(quantity));
            totalItems += cartItem.getQuantity();
        }

        cartResponse.setTotalPrice(totalPrice);
        cartResponse.setTotalItems(totalItems);
    }
}