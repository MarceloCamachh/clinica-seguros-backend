package com.marek_kawalski.clinic_system.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class CustomTimeFormatValidator implements ConstraintValidator<CustomTimeFormat, LocalTime> {
    @Override
    public void initialize(CustomTimeFormat constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        try {
            // Validar que la hora esté en formato HH:mm
            String timeStr = value.toString();
            return timeStr.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]");
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
