package com.fpt.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank(message = "PRODUCT_NAME_REQUIRED")
    private String name;
    private Double price;
    private String description;
    private String image;
    private Integer quantity;
    private Long categoryId;
}
