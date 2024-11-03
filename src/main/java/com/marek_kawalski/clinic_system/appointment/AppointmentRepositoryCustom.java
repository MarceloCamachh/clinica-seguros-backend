package com.marek_kawalski.clinic_system.appointment;

import org.springframework.data.domain.Page;

public interface AppointmentRepositoryCustom {
    Page<Appointment> findAllDoctorsAppointments(final String doctorId, final AppointmentRequestParams appointmentRequestParams);

    Page<Appointment> findAllPatientsAppointments(final String patientId, final AppointmentRequestParams patientAppointmentRequestParams);
}
