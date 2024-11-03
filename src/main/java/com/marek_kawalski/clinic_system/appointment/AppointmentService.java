package com.marek_kawalski.clinic_system.appointment;

import com.marek_kawalski.clinic_system.appointment.dto.CreateUpdateAppointmentDTO;
import com.marek_kawalski.clinic_system.appointment.exception.AppointmentExistsException;
import com.marek_kawalski.clinic_system.appointment.exception.AppointmentNotFoundException;
import com.marek_kawalski.clinic_system.examination.exception.ExaminationNotFoundException;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface AppointmentService {

    Optional<Appointment> createUpdateAppointment(final String id, final CreateUpdateAppointmentDTO createUpdateAppointmentDTO) throws UserNotFoundException, ExaminationNotFoundException, AppointmentNotFoundException, AppointmentExistsException;

    Optional<Appointment> findById(final String id);

    Page<Appointment> findAllDoctorsAppointments(final String doctorId, final AppointmentRequestParams appointmentRequestParams);

    Page<Appointment> findAllPatientsAppointments(final String patientId, final AppointmentRequestParams patientAppointmentRequestParams);
}
