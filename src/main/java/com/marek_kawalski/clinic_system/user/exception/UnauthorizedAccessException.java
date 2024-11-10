package com.marek_kawalski.clinic_system.user.exception;

public class UnauthorizedAccessException extends Exception {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
