package com.fpt.ecommerce.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductResponse {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private String image;
    private String categoryName;
}
