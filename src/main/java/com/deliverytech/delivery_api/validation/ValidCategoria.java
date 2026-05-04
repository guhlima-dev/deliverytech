package com.deliverytech.delivery_api.validation;

import com.deliverytech.delivery_api.validation.validator.CategoriaValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CategoriaValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCategoria {

    String message() default "Categoria inválida";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default{};
}



