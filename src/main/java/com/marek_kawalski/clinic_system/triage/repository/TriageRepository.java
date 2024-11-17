package com.marek_kawalski.clinic_system.triage.repository;

import com.marek_kawalski.clinic_system.triage.domain.Triage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TriageRepository extends MongoRepository<Triage, String> {
    
    List<Triage> findByPatientId(String patientId);
    
    List<Triage> findByNurseId(String nurseId);
    
    @Query("{'triageDate': {$gte: ?0, $lte: ?1}}")
    List<Triage> findByTriageDateBetween(LocalDateTime start, LocalDateTime end);
    
    List<Triage> findByPriority(String priority);
    
    Optional<Triage> findByPatientIdAndTriageDate(String patientId, LocalDateTime triageDate);
    
    List<Triage> findByIsActiveTrue();
}
