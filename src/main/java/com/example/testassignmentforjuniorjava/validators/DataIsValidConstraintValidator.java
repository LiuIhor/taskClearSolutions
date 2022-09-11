package com.example.testassignmentforjuniorjava.validators;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

public class DataIsValidConstraintValidator implements ConstraintValidator<DataIsValid, Date> {

    @Value("${app.minAge}")
    int minAge;

    @Override
    public void initialize(DataIsValid dataIsValid) {
    }

    @Override
    public boolean isValid(Date dateField, ConstraintValidatorContext constraintValidatorContext) {
        if (dateField == null) {
            return false;
        }
        Date currentDate = new Date();
        long milliseconds = currentDate.getTime() - dateField.getTime();
        int millisecondsInDay = 24 * 3600 * 1000;
        int daysInYear = 365;
        double years = (double) (milliseconds / (millisecondsInDay)) / daysInYear;
        return years >= minAge;
    }
}