package com.marek_kawalski.clinic_system.triage.domain.exception;

public class TriageNotFoundException extends RuntimeException {
    
    public TriageNotFoundException(String message) {
        super(message);
    }
    
    public TriageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
