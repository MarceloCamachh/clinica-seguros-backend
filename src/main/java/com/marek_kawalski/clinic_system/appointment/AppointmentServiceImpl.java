package com.marek_kawalski.clinic_system.appointment;

import com.marek_kawalski.clinic_system.appointment.dto.CreateUpdateAppointmentDTO;
import com.marek_kawalski.clinic_system.appointment.exception.AppointmentExistsException;
import com.marek_kawalski.clinic_system.appointment.exception.AppointmentNotFoundException;
import com.marek_kawalski.clinic_system.examination.ExaminationRepository;
import com.marek_kawalski.clinic_system.examination.exception.ExaminationNotFoundException;
import com.marek_kawalski.clinic_system.medical_record.domain.MedicalRecord;
import com.marek_kawalski.clinic_system.medical_record.repository.MedicalRecordRepository;
import com.marek_kawalski.clinic_system.user.User;
import com.marek_kawalski.clinic_system.user.UserRepository;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import com.marek_kawalski.clinic_system.triage.domain.exception.TriageNotFoundException;
import com.marek_kawalski.clinic_system.triage.repository.TriageRepository;

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
    private final TriageRepository triageRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    @Override
    public Optional<Appointment> createUpdateAppointment(String id, CreateUpdateAppointmentDTO dto) 
            throws UserNotFoundException, ExaminationNotFoundException, AppointmentNotFoundException, AppointmentExistsException {
        
        User doctor = userRepository.findById(dto.doctorId())
            .orElseThrow(() -> new UserNotFoundException("¡Doctor no encontrado!"));
            
        User patient = userRepository.findById(dto.patientId())
            .orElseThrow(() -> new UserNotFoundException("¡Paciente no encontrado!"));
        
        // Verificar o crear historial médico
        MedicalRecord medicalRecord = medicalRecordRepository.findByPatientId(patient.getId())
            .orElseGet(() -> {
                MedicalRecord newRecord = MedicalRecord.builder()
                    .patient(patient)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
                return medicalRecordRepository.save(newRecord);
            });
        
        Appointment appointment = createOrUpdateAppointment(id, dto, doctor, patient);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        
        // Actualizar historial médico
        updateMedicalRecord(medicalRecord, savedAppointment);
        
        return Optional.of(savedAppointment);
    }
    
    private void updateMedicalRecord(MedicalRecord medicalRecord, Appointment appointment) {
        medicalRecord.getAppointmentIds().add(appointment.getId());
        medicalRecord.setUpdatedAt(LocalDateTime.now());
        medicalRecordRepository.save(medicalRecord);
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

    private Appointment createOrUpdateAppointment(String id, CreateUpdateAppointmentDTO dto, User doctor, User patient) 
            throws UserNotFoundException, ExaminationNotFoundException, AppointmentNotFoundException {
        
        Appointment appointment;
        if (id != null) {
            appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Cita no encontrada"));
        } else {
            appointment = new Appointment();
            appointment.setCreationDate(LocalDateTime.now());
        }
        
        // Actualizar campos básicos
        appointment.setLastUpdateDate(LocalDateTime.now());
        appointment.setDate(dto.date());
        appointment.setDescription(dto.description());
        appointment.setDiagnosis(dto.diagnosis());
        appointment.setTreatment(dto.treatment());
        appointment.setMedicines(dto.medicines());
        appointment.setAttachmentUrls(dto.attachmentUrls());
        appointment.setMedicalNotes(dto.medicalNotes());
        appointment.setProcedures(dto.procedures());
        appointment.setLabResults(dto.labResults());
        appointment.setStatus(dto.status());
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        
        // Campos adicionales
        appointment.setAppointmentType(dto.appointmentType());
        appointment.setNextAppointmentDate(dto.nextAppointmentDate());
        appointment.setFollowUpNotes(dto.followUpNotes());
        appointment.setRelatedAppointmentIds(dto.relatedAppointmentIds());
        appointment.setSpecialtyArea(dto.specialtyArea());
        
        // Referencias
        appointment.setExamination(examinationRepository.findById(dto.examinationId())
            .orElseThrow(() -> new ExaminationNotFoundException("Examen no encontrado")));
        
        if (dto.triageId() != null) {
            appointment.setTriage(triageRepository.findById(dto.triageId())
                .orElseThrow(() -> new TriageNotFoundException("Triaje no encontrado")));
        }
        
        return appointment;
    }
}
