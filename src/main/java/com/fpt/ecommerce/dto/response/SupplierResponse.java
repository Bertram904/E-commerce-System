package com.fpt.ecommerce.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierResponse {
    Long id;
    String name;
    String contactPerson;
    String phoneNumber;
    String email;
    String address;
    Date createdAt;
}