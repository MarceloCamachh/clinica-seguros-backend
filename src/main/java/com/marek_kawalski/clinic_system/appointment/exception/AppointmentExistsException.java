package com.marek_kawalski.clinic_system.appointment.exception;

public class AppointmentExistsException extends Exception {
    public AppointmentExistsException(final String message) {
        super(message);
    }
}
