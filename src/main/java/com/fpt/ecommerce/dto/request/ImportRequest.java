package com.fpt.ecommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ImportRequest {
    @NotNull(message = "SUPPLIER_REQUIRED")
    private Long supplierId;

    @NotEmpty(message = "ITEMS_REQUIRED")
    private List<ImportItemRequest> items;

    @Data
    public static class ImportItemRequest {
        @NotNull
        private Long productId;
        @Min(value = 1, message="QUANTITY_MIN_1")
        private Integer quantity;
        @Min(value = 0, message = "PRICE_MIN_0")
        private Double importPrice;
    }
}
