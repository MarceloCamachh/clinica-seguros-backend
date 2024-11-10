package com.marek_kawalski.clinic_system.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomTimeFormatValidator.class)
@Documented
public @interface CustomTimeFormat {
    String message() default "Formato de hora inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
