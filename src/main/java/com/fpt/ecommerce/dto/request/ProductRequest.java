package com.fpt.ecommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    String name;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Giá phải lớn hơn 0")
    BigDecimal price;

    String description;
    String imageUrl;

    @Min(value = 0)
    Integer stockQuantity;

    @NotNull(message = "Phải chọn danh mục")
    Long categoryId;
}
