package com.fpt.ecommerce.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class OrderDetailResponse {
    Long productId;
    String productName;
    String productImage;
    Integer quantity;
    BigDecimal price;
    BigDecimal subTotal;
}