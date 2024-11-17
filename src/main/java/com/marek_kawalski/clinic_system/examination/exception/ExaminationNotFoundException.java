package com.marek_kawalski.clinic_system.examination.exception;

public class ExaminationNotFoundException extends RuntimeException {

    public ExaminationNotFoundException(String message) {
        super(message);
    }
}
