package com.marek_kawalski.clinic_system.medical_record.service;

import com.marek_kawalski.clinic_system.medical_record.domain.MedicalRecord;
import com.marek_kawalski.clinic_system.medical_record.dto.CreateMedicalRecordDTO;
import com.marek_kawalski.clinic_system.medical_record.dto.UpdateMedicalRecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MedicalRecordService {
    MedicalRecord createMedicalRecord(CreateMedicalRecordDTO createMedicalRecordDTO);
    
    Optional<MedicalRecord> updateMedicalRecord(String id, UpdateMedicalRecordDTO updateMedicalRecordDTO);
    
    Optional<MedicalRecord> findById(String id);
    
    Optional<MedicalRecord> findByPatientId(String patientId);
    
    Page<MedicalRecord> findAll(Pageable pageable);
    
    void deleteMedicalRecord(String id);
}
