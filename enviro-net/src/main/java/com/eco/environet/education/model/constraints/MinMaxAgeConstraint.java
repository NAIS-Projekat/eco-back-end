package com.eco.environet.education.model.constraints;

import com.eco.environet.education.model.validators.MinMaxAgeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinMaxAgeValidator.class)
@Documented
public @interface MinMaxAgeConstraint {
    String message() default "Invalid age range: Min age must be less than or equal to max age";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String minField();
    String maxField();
}
