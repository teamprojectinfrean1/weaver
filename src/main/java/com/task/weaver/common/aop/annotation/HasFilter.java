package com.task.weaver.common.aop.annotation;

import com.task.weaver.common.aop.validator.HasFiltersValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = HasFiltersValidator.class)
@Target({ ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasFilter {
    String message() default "Filters not provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
