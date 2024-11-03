package com.marek_kawalski.clinic_system.user.doctor;

import com.marek_kawalski.clinic_system.appointment.Appointment;
import com.marek_kawalski.clinic_system.appointment.AppointmentRepository;
import com.marek_kawalski.clinic_system.appointment.AppointmentStatus;
import com.marek_kawalski.clinic_system.appointment.exception.AppointmentIllegalStateException;
import com.marek_kawalski.clinic_system.examination.Examination;
import com.marek_kawalski.clinic_system.examination.ExaminationRepository;
import com.marek_kawalski.clinic_system.examination.exception.ExaminationNotFoundException;
import com.marek_kawalski.clinic_system.user.User;
import com.marek_kawalski.clinic_system.user.UserRepository;
import com.marek_kawalski.clinic_system.user.UserRequestParams;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import com.marek_kawalski.clinic_system.user.doctor.dto.CreateDoctorDTO;
import com.marek_kawalski.clinic_system.user.doctor.dto.UpdateDoctorDTO;
import com.marek_kawalski.clinic_system.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ExaminationRepository examinationRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public List<LocalDateTime> getAvailableAppointments(final String doctorId, final String examinationId, final LocalDateTime date) throws UserNotFoundException, ExaminationNotFoundException, AppointmentIllegalStateException {
        final Optional<User> optionalUser = userRepository.findById(doctorId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Doctor with id: " + doctorId + " not found");
        }
        final Optional<Examination> optionalExamination = examinationRepository.findById(examinationId);
        if (optionalExamination.isEmpty()) {
            throw new ExaminationNotFoundException("Examination with id: " + examinationId + " not found");
        }

        if (date.isBefore(LocalDateTime.now())) {
            throw new AppointmentIllegalStateException("Date cannot be in the past");
        }

        final User doctor = optionalUser.get();
        final Examination examination = optionalExamination.get();

        final DayOfWeek dayOfWeek = date.getDayOfWeek();
        final LocalTime startTime = doctor.getDoctorDetails().getSchedule().getDailySchedules().get(dayOfWeek).getStartTime();
        final LocalTime endTime = doctor.getDoctorDetails().getSchedule().getDailySchedules().get(dayOfWeek).getEndTime();

        final List<Appointment> bookedAppointments = appointmentRepository.findByDoctorAndDateBetweenAndStatus(doctor, date.with(LocalTime.MIN), date.with(LocalTime.MAX), AppointmentStatus.BOOKED);


        return getAvailableAppointmentsSlots(LocalDateTime.of(LocalDate.from(date), startTime), LocalDateTime.of(LocalDate.from(date), endTime), bookedAppointments, examination);
    }

    @Override
    public Page<User> getPagedDoctors(final UserRequestParams doctorRequestParams) {
        return userRepository.getPagedUsers(doctorRequestParams);
    }

    @Override
    public Optional<User> getDoctorByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User createDoctor(CreateDoctorDTO createDoctorDTO) {
        // Verificar si ya existe un usuario con ese email
        if (userRepository.findByEmail(createDoctorDTO.email()).isPresent()) {
            throw new IllegalStateException("Ya existe un usuario con el email: " + createDoctorDTO.email());
        }

        DoctorDetails doctorDetails = DoctorDetails.builder()
                .specialization(createDoctorDTO.specialization())
                .education(createDoctorDTO.education())
                .description(createDoctorDTO.description())
                .build();

        User newDoctor = User.builder()
                .name(createDoctorDTO.name())
                .surname(createDoctorDTO.surname())
                .email(createDoctorDTO.email())
                .phoneNumber(createDoctorDTO.phoneNumber())
                .password(passwordEncoder.encode("defaultPassword"))
                .userRole(UserRole.ROLE_DOCTOR)
                .isEnabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .doctorDetails(doctorDetails)
                .build();

        return userRepository.save(newDoctor);
    }

    @Override
    public User updateDoctor(String doctorId, UpdateDoctorDTO updateDoctorDTO) throws UserNotFoundException {
        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new UserNotFoundException("Doctor no encontrado con ID: " + doctorId));

        // Actualizar solo los campos no nulos
        if (updateDoctorDTO.name() != null) {
            doctor.setName(updateDoctorDTO.name());
        }
        if (updateDoctorDTO.surname() != null) {
            doctor.setSurname(updateDoctorDTO.surname());
        }
        if (updateDoctorDTO.email() != null) {
            doctor.setEmail(updateDoctorDTO.email());
        }
        if (updateDoctorDTO.phoneNumber() != null) {
            doctor.setPhoneNumber(updateDoctorDTO.phoneNumber());
        }
        
        // Actualizar detalles del doctor
        DoctorDetails details = doctor.getDoctorDetails();
        if (details == null) {
            details = DoctorDetails.builder()
                    .build();
            doctor.setDoctorDetails(details);
        }
        
        if (updateDoctorDTO.specialization() != null) {
            details.setSpecialization(updateDoctorDTO.specialization());
        }
        if (updateDoctorDTO.education() != null) {
            details.setEducation(updateDoctorDTO.education());
        }
        if (updateDoctorDTO.description() != null) {
            details.setDescription(updateDoctorDTO.description());
        }

        return userRepository.save(doctor);
    }

    @Override
    public void deleteDoctor(String doctorId) throws UserNotFoundException {
        if (!userRepository.existsById(doctorId)) {
            throw new UserNotFoundException("Doctor no encontrado con ID: " + doctorId);
        }
        userRepository.deleteById(doctorId);
    }

    private List<LocalDateTime> getAvailableAppointmentsSlots(final LocalDateTime startTime, final LocalDateTime endTime,
                                                              final List<Appointment> bookedAppointments, final Examination examination) {
        final List<LocalDateTime> availableAppointmentsSlots = new ArrayList<>();

        LocalDateTime currentTime = startTime;
        while (currentTime.plusMinutes(examination.getDuration()).isBefore(endTime)
               || currentTime.plusMinutes(examination.getDuration()).isEqual(endTime)) {
            boolean isAvailable = true;

            for (Appointment appointment : bookedAppointments) {
                if ((currentTime.isAfter(appointment.getDate())
                     || currentTime.isEqual(appointment.getDate()))
                    && (currentTime.isBefore(appointment.getDate())
                        || currentTime.isEqual(appointment.getDate()))
                ) {
                    currentTime = appointment.getDate().plusMinutes(appointment.getExamination().getDuration());
                    isAvailable = false;
                    break;
                }
            }
            if (isAvailable) {
                availableAppointmentsSlots.add(currentTime);
                currentTime = currentTime.plusMinutes(examination.getDuration());
            }
        }

        return availableAppointmentsSlots;
    }
}