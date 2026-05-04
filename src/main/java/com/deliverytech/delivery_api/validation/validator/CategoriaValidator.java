package com.deliverytech.delivery_api.validation.validator;

import com.deliverytech.delivery_api.enums.CategoriaRestaurante;
import com.deliverytech.delivery_api.validation.ValidCategoria;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CategoriaValidator implements ConstraintValidator<ValidCategoria, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context){
        if (value == null || value.isBlank()){
            return false;
        }
        try {
            CategoriaRestaurante.valueOf(value.toUpperCase());
            return true;
        }catch (IllegalArgumentException e){
            return false;
        }
    }
}
