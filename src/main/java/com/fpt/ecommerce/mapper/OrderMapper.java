package com.fpt.ecommerce.mapper;

import com.fpt.ecommerce.dto.response.OrderDetailResponse;
import com.fpt.ecommerce.dto.response.OrderResponse;
import com.fpt.ecommerce.entity.Order;
import com.fpt.ecommerce.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "status", source = "status")
    @Mapping(target = "orderDetails", source = "orderDetails")
    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productImage", source = "product.imageUrl")
    @Mapping(target = "subTotal", source = ".", qualifiedByName = "calcSubTotal")
    OrderDetailResponse toDetailResponse(OrderDetail detail);

    @Named("calcSubTotal")
    default BigDecimal calcSubTotal(OrderDetail detail) {
        if (detail.getPrice() == null || detail.getQuantity() == null) return BigDecimal.ZERO;
        return detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
    }
}