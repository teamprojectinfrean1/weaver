package com.task.weaver.domain.mail.validator;

import com.task.weaver.domain.mail.annotation.CustomEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomEmailValidator implements ConstraintValidator<CustomEmail, String> {
    @Override
    public boolean isValid(final String email, final ConstraintValidatorContext context) {
        return email != null && !email.isEmpty();
    }
}
