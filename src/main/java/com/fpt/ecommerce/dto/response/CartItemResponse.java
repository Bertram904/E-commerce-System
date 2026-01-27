package com.fpt.ecommerce.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    Long productId;
    String productName;
    Double productPrice;
    Integer quantity;
    String productImageUrl;
    Double subTotal;
    Integer stockRemaining;
}
