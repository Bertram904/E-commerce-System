package com.fpt.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OrderRequest {
    @NotBlank(message = "Receiver name is required")
    private String receiverName;

    @Pattern(regexp = "^0\\d{9}$", message = "Phone number invalid")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String shippingAddress;
}
