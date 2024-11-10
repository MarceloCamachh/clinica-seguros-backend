package com.marek_kawalski.clinic_system.appointment;

import com.marek_kawalski.clinic_system.appointment.dto.CreateUpdateAppointmentDTO;
import com.marek_kawalski.clinic_system.appointment.exception.AppointmentExistsException;
import com.marek_kawalski.clinic_system.appointment.exception.AppointmentNotFoundException;
import com.marek_kawalski.clinic_system.examination.ExaminationRepository;
import com.marek_kawalski.clinic_system.examination.exception.ExaminationNotFoundException;
import com.marek_kawalski.clinic_system.user.User;
import com.marek_kawalski.clinic_system.user.UserRepository;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final ExaminationRepository examinationRepository;


    @Override
    public Optional<Appointment> createUpdateAppointment(final String id, final CreateUpdateAppointmentDTO createUpdateAppointmentDTO) throws UserNotFoundException, ExaminationNotFoundException, AppointmentNotFoundException, AppointmentExistsException {
        final Optional<User> doctor = userRepository.findById(createUpdateAppointmentDTO.doctorId());
        if (doctor.isEmpty()) {
            throw new UserNotFoundException("¡Doctor no encontrado!");
        }
        Appointment appointment;
        if (id != null) {
            appointment = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException("¡Cita no encontrada!"));
            appointment.setLastUpdateDate(LocalDateTime.now());
        } else {
            if (appointmentRepository.findByDoctorAndDateAndStatus(doctor.get(), createUpdateAppointmentDTO.date(), createUpdateAppointmentDTO.status()).isPresent()) {
                throw new AppointmentExistsException("¡La cita ya existe!");
            }
            appointment = new Appointment();
            appointment.setCreationDate(LocalDateTime.now());
        }

        updateAppointmentDetails(appointment, createUpdateAppointmentDTO, doctor.get());

        return Optional.of(appointmentRepository.save(appointment));
    }

    @Override
    public Optional<Appointment> findById(final String id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public Page<Appointment> findAllDoctorsAppointments(final String doctorId, final AppointmentRequestParams appointmentRequestParams) {
        return appointmentRepository.findAllDoctorsAppointments(doctorId, appointmentRequestParams);
    }

    @Override
    public Page<Appointment> findAllPatientsAppointments(final String patientId, final AppointmentRequestParams patientAppointmentRequestParams) {
        return appointmentRepository.findAllPatientsAppointments(patientId, patientAppointmentRequestParams);
    }

    private void updateAppointmentDetails(final Appointment appointment, final CreateUpdateAppointmentDTO createUpdateAppointmentDTO,
                                          final User doctor) throws UserNotFoundException, ExaminationNotFoundException {
        appointment.setDate(createUpdateAppointmentDTO.date());
        appointment.setDescription(createUpdateAppointmentDTO.description());
        appointment.setStatus(createUpdateAppointmentDTO.status());
        appointment.setMedicines(createUpdateAppointmentDTO.medicines());
        appointment.setDoctor(doctor);
        appointment.setPatient(userRepository.findById(createUpdateAppointmentDTO.patientId())
            .orElseThrow(() -> new UserNotFoundException("¡Paciente no encontrado!")));
        appointment.setExamination(examinationRepository.findById(createUpdateAppointmentDTO.examinationId())
            .orElseThrow(() -> new ExaminationNotFoundException("¡Examen no encontrado!")));
    }
}
