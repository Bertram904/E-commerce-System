package com.fpt.ecommerce.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    Long id;
    String status;
    BigDecimal totalAmount;

    String receiverName;
    String phoneNumber;
    String shippingAddress;
    String note;

    List<OrderDetailResponse> orderDetails;
}
