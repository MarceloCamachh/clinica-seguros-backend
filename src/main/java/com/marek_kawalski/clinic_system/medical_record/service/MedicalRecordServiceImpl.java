package com.marek_kawalski.clinic_system.medical_record.service;

import com.marek_kawalski.clinic_system.medical_record.domain.MedicalRecord;
import com.marek_kawalski.clinic_system.medical_record.dto.CreateMedicalRecordDTO;
import com.marek_kawalski.clinic_system.medical_record.dto.UpdateMedicalRecordDTO;
import com.marek_kawalski.clinic_system.medical_record.repository.MedicalRecordRepository;
import com.marek_kawalski.clinic_system.user.UserRepository;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final UserRepository userRepository;

    @Override
    public MedicalRecord createMedicalRecord(CreateMedicalRecordDTO dto) {
        var patient = userRepository.findById(dto.patientId())
            .orElseThrow(() -> new UserNotFoundException("Paciente no encontrado"));

        var medicalRecord = MedicalRecord.builder()
                .patient(patient)
                .bloodType(dto.bloodType())
                .allergies(dto.allergies())
                .chronicConditions(dto.chronicConditions())
                .familyHistory(dto.familyHistory())
                .height(dto.height())
                .weight(dto.weight())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        return medicalRecordRepository.save(medicalRecord);
    }

    @Override
    public Optional<MedicalRecord> updateMedicalRecord(String id, UpdateMedicalRecordDTO dto) {
        return medicalRecordRepository.findById(id)
                .map(record -> {
                    if (dto.bloodType() != null) record.setBloodType(dto.bloodType());
                    if (dto.allergies() != null) record.setAllergies(dto.allergies());
                    if (dto.chronicConditions() != null) record.setChronicConditions(dto.chronicConditions());
                    if (dto.familyHistory() != null) record.setFamilyHistory(dto.familyHistory());
                    if (dto.height() != null) record.setHeight(dto.height());
                    if (dto.weight() != null) record.setWeight(dto.weight());
                    if (dto.isActive() != null) record.setActive(dto.isActive());
                    
                    record.setUpdatedAt(LocalDateTime.now());
                    return medicalRecordRepository.save(record);
                });
    }

    @Override
    public Optional<MedicalRecord> findById(String id) {
        return medicalRecordRepository.findById(id);
    }

    @Override
    public Optional<MedicalRecord> findByPatientId(String patientId) {
        return medicalRecordRepository.findByPatientId(patientId);
    }

    @Override
    public Page<MedicalRecord> findAll(Pageable pageable) {
        return medicalRecordRepository.findAll(pageable);
    }

    @Override
    public void deleteMedicalRecord(String id) {
        medicalRecordRepository.findById(id)
                .ifPresent(record -> {
                    record.setActive(false);
                    record.setUpdatedAt(LocalDateTime.now());
                    medicalRecordRepository.save(record);
                });
    }
}
