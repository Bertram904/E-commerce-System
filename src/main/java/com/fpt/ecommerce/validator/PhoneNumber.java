package com.fpt.ecommerce.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {
    String message() default "INVALID_PHONE_FORMAT";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
