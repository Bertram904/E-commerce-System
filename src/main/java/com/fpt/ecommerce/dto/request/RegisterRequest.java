package com.fpt.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "USERNAME_INVALID")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 5, message = "INVALID_PASSWORD")
    private String password;

    private String email;
    private String address;
    private String phoneNumber;
}
