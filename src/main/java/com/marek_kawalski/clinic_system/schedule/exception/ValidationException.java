package com.marek_kawalski.clinic_system.schedule.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
} 