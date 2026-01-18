package com.fpt.ecommerce.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportDetailResponse {
    Long productId;
    String productName;
    Integer quantity;
    BigDecimal price;
}
