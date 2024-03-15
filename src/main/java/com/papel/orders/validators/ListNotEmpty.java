package com.papel.orders.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = ListNotEmptyValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListNotEmpty {
    String message() default "list cannot be empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}