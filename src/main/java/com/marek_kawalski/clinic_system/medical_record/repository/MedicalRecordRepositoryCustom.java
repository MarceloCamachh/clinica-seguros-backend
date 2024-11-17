package com.marek_kawalski.clinic_system.medical_record.repository;

import com.marek_kawalski.clinic_system.medical_record.domain.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicalRecordRepositoryCustom {
    Page<MedicalRecord> findAllActive(Pageable pageable);
    Page<MedicalRecord> findByPatientName(String patientName, Pageable pageable);
    Page<MedicalRecord> findByDoctorId(String doctorId, Pageable pageable);
}
