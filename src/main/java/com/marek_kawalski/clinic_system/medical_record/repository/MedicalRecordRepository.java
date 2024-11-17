package com.marek_kawalski.clinic_system.medical_record.repository;

import com.marek_kawalski.clinic_system.medical_record.domain.MedicalRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends MongoRepository<MedicalRecord, String>, MedicalRecordRepositoryCustom {
    Optional<MedicalRecord> findByPatientId(String patientId);
}
