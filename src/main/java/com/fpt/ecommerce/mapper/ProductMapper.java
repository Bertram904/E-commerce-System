package com.fpt.ecommerce.mapper;

import com.fpt.ecommerce.dto.request.ProductRequest;
import com.fpt.ecommerce.dto.response.ProductResponse;
import com.fpt.ecommerce.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryName", source = "category.name")
    ProductResponse toProductResponse(Product product);

    @Mapping(target="category", ignore = true)
    Product toProduct(ProductRequest request);
}
