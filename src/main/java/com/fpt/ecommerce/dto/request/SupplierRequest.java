package com.fpt.ecommerce.dto.request;

import com.fpt.ecommerce.validator.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierRequest {
    @NotBlank(message = "SUPPLIER_NAME_REQUIRED")
    String name;

    @NotBlank(message = "CONTACT_PERSON_REQUIRED")
    String contactPerson;
    @NotBlank(message = "PHONE_REQUIRED")
    @PhoneNumber(message = "INVALID_PHONE_FORMAT")
    String phoneNumber;

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "INVALID_EMAIL_FORMAT")
    String email;

    String address;
}
