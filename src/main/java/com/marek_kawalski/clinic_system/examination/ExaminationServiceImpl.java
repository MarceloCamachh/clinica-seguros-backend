package com.marek_kawalski.clinic_system.examination;

import com.marek_kawalski.clinic_system.examination.dto.CreateUpdateExaminationDTO;
import com.marek_kawalski.clinic_system.user.User;
import com.marek_kawalski.clinic_system.user.UserRepository;
import com.marek_kawalski.clinic_system.medical_record.domain.MedicalRecord;
import com.marek_kawalski.clinic_system.medical_record.repository.MedicalRecordRepository;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import com.marek_kawalski.clinic_system.examination.exception.ExaminationNotFoundException;
@Service
@AllArgsConstructor
public class ExaminationServiceImpl implements ExaminationService {
    private final ExaminationRepository examinationRepository;
    private final UserRepository userRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    @Override
    public Page<Examination> getPagedExaminations(final ExaminationRequestParams examinationRequestParams) {
        return examinationRepository.getPagedExaminations(examinationRequestParams);
    }

    @Override
    public Optional<Examination> createUpdateExamination(final String id, final CreateUpdateExaminationDTO examinationDTO) {
        Examination examination;

        if (id != null) {
            examination = examinationRepository.findById(id)
                .orElseThrow(() -> new ExaminationNotFoundException("Examen no encontrado"));
        } else {
            examination = new Examination();
        }

        updateExaminationDetails(examination, examinationDTO);
        
        // Guardar el examen antes de actualizar las relaciones
        examination = examinationRepository.save(examination);

        // Actualizar doctores con el nuevo examen
        updateDoctorsExaminations(examination, examinationDTO);

        // Actualizar historial médico si hay paciente
        if (examinationDTO.patientId() != null) {
            User patient = userRepository.findById(examinationDTO.patientId())
                .orElseThrow(() -> new UserNotFoundException("Paciente no encontrado"));
            updateMedicalRecord(patient, examination);
        }

        return Optional.of(examination);
    }

    @Override
    public List<Examination> getDoctorsExaminations(final ObjectId doctorId) {
        return examinationRepository.findAllByDoctorsId(doctorId);
    }

    private void updateExaminationDetails(Examination examination, CreateUpdateExaminationDTO examinationDTO) {
        examination.setDescription(examinationDTO.description());
        examination.setDuration(examinationDTO.duration());
        examination.setName(examinationDTO.name());
        examination.setPrice(examinationDTO.price());
        examination.setStatus(examinationDTO.status());
        
        if (examination.getCreatedAt() == null) {
            examination.setCreatedAt(LocalDateTime.now());
            examination.setCreatedBy(examinationDTO.createdBy());
        }
        
        examination.setUpdatedAt(LocalDateTime.now());
        examination.setUpdatedBy(examinationDTO.updatedBy());
    }

    private void updateDoctorsExaminations(Examination examination, CreateUpdateExaminationDTO examinationDTO) {
        List<String> doctorIds = examinationDTO.doctorIds();
        if (doctorIds == null) {
            doctorIds = new ArrayList<>();
        }

        List<User> allDoctors = userRepository.findAll();
        List<User> tempDoctors = new ArrayList<>();

        for (User doctor : allDoctors) {
            if (doctor.getDoctorDetails() == null) continue;
            
            List<Examination> doctorExams = doctor.getDoctorDetails().getExaminations();
            if (doctorExams == null) {
                doctor.getDoctorDetails().setExaminations(new ArrayList<>());
                doctorExams = doctor.getDoctorDetails().getExaminations();
            }

            boolean hasExam = doctorExams.stream()
                .anyMatch(e -> e.getId().equals(examination.getId()));
            boolean shouldHaveExam = doctorIds.contains(doctor.getId());

            if (hasExam && !shouldHaveExam) {
                doctorExams.removeIf(e -> e.getId().equals(examination.getId()));
                userRepository.save(doctor);
            } else if (!hasExam && shouldHaveExam) {
                doctorExams.add(examination);
                userRepository.save(doctor);
                tempDoctors.add(doctor);
            }
        }

        examination.setDoctors(tempDoctors);
        examinationRepository.save(examination);
    }

    private void updateMedicalRecord(User patient, Examination examination) {
        MedicalRecord medicalRecord = medicalRecordRepository.findByPatientId(patient.getId())
            .orElseGet(() -> {
                MedicalRecord newRecord = MedicalRecord.builder()
                    .patient(patient)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
                return medicalRecordRepository.save(newRecord);
            });

        if (medicalRecord.getExaminationIds() == null) {
            medicalRecord.setExaminationIds(new ArrayList<>());
        }
        
        if (!medicalRecord.getExaminationIds().contains(examination.getId())) {
            medicalRecord.getExaminationIds().add(examination.getId());
            medicalRecord.setUpdatedAt(LocalDateTime.now());
            medicalRecordRepository.save(medicalRecord);
        }
    }

}
