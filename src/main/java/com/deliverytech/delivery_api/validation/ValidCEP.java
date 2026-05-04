package com.deliverytech.delivery_api.validation;

import com.deliverytech.delivery_api.validation.validator.CepValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CepValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCEP {

    String message() default "CEP Inválido";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default{};
}
