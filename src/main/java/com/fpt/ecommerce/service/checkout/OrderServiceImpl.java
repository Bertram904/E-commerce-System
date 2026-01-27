package com.fpt.ecommerce.service.checkout;

import com.fpt.ecommerce.constant.OrderStatus;
import com.fpt.ecommerce.dto.request.OrderRequest;
import com.fpt.ecommerce.dto.response.OrderResponse;
import com.fpt.ecommerce.entity.CartItem;
import com.fpt.ecommerce.entity.Order;
import com.fpt.ecommerce.entity.OrderDetail;
import com.fpt.ecommerce.entity.Product;
import com.fpt.ecommerce.exception.AppException;
import com.fpt.ecommerce.exception.ErrorCode;
import com.fpt.ecommerce.mapper.OrderMapper;
import com.fpt.ecommerce.repository.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderServiceImpl implements OrderService{

    CartRepository cartRepository;
    OrderRepository orderRepository;
    ProductRepository productRepository;
    CartItemRepository cartItemRepository;
    MemberRepository memberRepository;
    OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse checkout(Long memberId, OrderRequest orderRequest) {
        log.info("Checkout process started for user: {}", memberId);

        //1.Validate User and cart
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var cart = cartRepository.findByMemberId(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new AppException(ErrorCode.CART_EMPTY);
        }

        //2. Create order
        var order = Order.builder()
                .member(member)
                .receiverName(orderRequest.getReceiverName())
                .phoneNumber(orderRequest.getPhoneNumber())
                .shippingAddress(orderRequest.getShippingAddress())
                .status(OrderStatus.PENDING)
                .orderDate(Instant.now())
                .totalAmount(BigDecimal.ZERO)
                .orderDetails(new HashSet<>())
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        //3. for CartItem
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = productRepository.findByIdAndLock(cartItem.getProduct().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new AppException(ErrorCode.OUT_OF_STOCK);
            }

            BigDecimal currentPrice = product.getPrice();
            totalAmount = totalAmount.add(currentPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity())));

            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(currentPrice) // luu gia cung
                    .build();

            order.getOrderDetails().add(orderDetail);
        }
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteAllInBatch(cart.getCartItems());

        log.info("Check out success. Order ID: {}", savedOrder.getId());
        return orderMapper.toOrderResponse(savedOrder);
    }
}
