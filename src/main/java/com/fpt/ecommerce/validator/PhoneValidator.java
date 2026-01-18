package com.fpt.ecommerce.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<PhoneNumber, String> {
    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNo, ConstraintValidatorContext context) {
        if (phoneNo == null || phoneNo.isEmpty()) {
            return true;
        }
        return phoneNo.matches("^0\\d{9}$");
    }
}
