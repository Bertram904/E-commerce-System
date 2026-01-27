
package com.fpt.ecommerce.mapper;

import com.fpt.ecommerce.dto.request.ProductRequest;
import com.fpt.ecommerce.dto.response.ProductResponse;
import com.fpt.ecommerce.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "category", ignore = true)
    Product toProduct(ProductRequest request);

    @Mapping(target = "categoryName", source = "category.name")
    ProductResponse toProductResponse(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateProduct(@MappingTarget Product product, ProductRequest request);
}
