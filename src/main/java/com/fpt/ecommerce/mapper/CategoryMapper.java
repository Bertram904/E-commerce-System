package com.fpt.ecommerce.mapper;

import com.fpt.ecommerce.dto.request.CategoryRequest;
import com.fpt.ecommerce.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(CategoryRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    void updateCategory(@MappingTarget Category category, CategoryRequest request);
}