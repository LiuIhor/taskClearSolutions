package com.example.testassignmentforjuniorjava.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Custom annotation to check if the date is 18 years old
 */
@Documented
@Constraint(validatedBy = DataIsValidConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataIsValid {

    String message() default "{Data is invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}