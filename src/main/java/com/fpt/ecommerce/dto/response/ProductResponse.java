package com.fpt.ecommerce.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class ProductResponse {
    Long id;
    String name;
    BigDecimal price;
    String description;
    String imageUrl;
    Integer stockQuantity;
    String categoryName;
}
