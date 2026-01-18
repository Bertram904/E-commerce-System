package com.fpt.ecommerce.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberResponse {
    Long id;
    String username;
    String email;
    String phoneNumber;
    String address;

    Set<String> roles;
    Set<String> permissions;

    Date createdAt;
    Date updatedAt;
}
