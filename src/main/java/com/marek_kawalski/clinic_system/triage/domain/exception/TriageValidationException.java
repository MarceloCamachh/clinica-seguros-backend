package com.marek_kawalski.clinic_system.triage.domain.exception;

public class TriageValidationException extends RuntimeException {
    
    public TriageValidationException(String message) {
        super(message);
    }
    
    public TriageValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
