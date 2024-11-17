package com.marek_kawalski.clinic_system.medical_record.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MedicalRecordNotFoundException extends RuntimeException {
    public MedicalRecordNotFoundException(String message) {
        super(message);
    }
    
    public MedicalRecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
