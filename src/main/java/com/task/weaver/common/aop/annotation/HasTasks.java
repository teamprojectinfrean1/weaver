package com.task.weaver.common.aop.annotation;

import com.task.weaver.common.aop.validator.HasTasksValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = HasTasksValidator.class)
@Target({ ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasTasks {
    String message() default "Project has no tasks";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
