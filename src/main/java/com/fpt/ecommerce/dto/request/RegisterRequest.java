package com.fpt.ecommerce.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.ecommerce.validator.DobConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "USERNAME_INVALID")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 5, message = "INVALID_PASSWORD")
    String password;

    @NotNull(message = "DOB_REQUIRED")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DobConstraint(min = 16, message = "USER_MUST_BE_16_YEARS_OLD")
    LocalDate dob;
    String email;
    String address;
    String phoneNumber;
}
