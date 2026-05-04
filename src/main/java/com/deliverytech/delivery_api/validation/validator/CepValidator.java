package com.deliverytech.delivery_api.validation.validator;

import com.deliverytech.delivery_api.validation.ValidCEP;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CepValidator implements ConstraintValidator<ValidCEP, String> {

    private static final String regex = "^\\d{5}\\[-?]\\d{3}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) return false;
        return value.matches(regex);

    }
}
