package com.eco.environet.education.model.validators;

import com.eco.environet.education.model.constraints.MinMaxAgeConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class MinMaxAgeValidator implements ConstraintValidator<MinMaxAgeConstraint, Object> {

    private String minField;
    private String maxField;

    @Override
    public void initialize(MinMaxAgeConstraint constraintAnnotation) {
        this.minField = constraintAnnotation.minField();
        this.maxField = constraintAnnotation.maxField();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        Object minFieldValue = new BeanWrapperImpl(o).getPropertyValue(minField);
        Object maxFieldValue = new BeanWrapperImpl(o).getPropertyValue(maxField);

        return (int)minFieldValue <= (int)maxFieldValue;
    }
}
