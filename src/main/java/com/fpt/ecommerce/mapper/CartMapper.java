package com.fpt.ecommerce.mapper;

import com.fpt.ecommerce.dto.response.CartItemResponse;
import com.fpt.ecommerce.dto.response.CartResponse;
import com.fpt.ecommerce.entity.Cart;
import com.fpt.ecommerce.entity.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.imageUrl", target = "productImageUrl")
    @Mapping(source = "product.price", target = "productPrice")
    @Mapping(source = "product.stockQuantity", target = "stockRemaining")
    CartItemResponse toCartItemResponse(CartItem item);

    @Mapping(target = "items", source = "cartItems")
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "totalItems", ignore = true)
    CartResponse toCartResponse(Cart cart);

}
