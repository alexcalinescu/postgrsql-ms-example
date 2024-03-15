package com.papel.orders.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListNotEmptyValidator implements ConstraintValidator<ListNotEmpty, List<?>> {

    @Override
    public boolean isValid(List<?> values, ConstraintValidatorContext context) {
        return values != null && values.size() > 0;
    }
}