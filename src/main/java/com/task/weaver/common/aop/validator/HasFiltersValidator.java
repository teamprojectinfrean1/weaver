package com.task.weaver.common.aop.validator;

import com.task.weaver.common.aop.annotation.HasFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HasFiltersValidator implements ConstraintValidator<HasFilter, String> {
    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (!value.equalsIgnoreCase("MANAGER") && !value.equalsIgnoreCase("TASK") && !value.equalsIgnoreCase("ISSUE")) {
            throw new IllegalArgumentException("Invalid Filter : " + value);
        }
        return true;
    }
}
