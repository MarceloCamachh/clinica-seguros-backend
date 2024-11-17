package com.marek_kawalski.clinic_system.triage.service;

import com.marek_kawalski.clinic_system.triage.domain.Triage;
import com.marek_kawalski.clinic_system.triage.dto.CreateTriageDTO;
import com.marek_kawalski.clinic_system.triage.dto.UpdateTriageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TriageService {
    Triage createTriage(CreateTriageDTO createTriageDTO);
    
    Optional<Triage> updateTriage(String id, UpdateTriageDTO updateTriageDTO);
    
    Optional<Triage> findById(String id);
    
    List<Triage> findByPatientId(String patientId);
    
    List<Triage> findByNurseId(String nurseId);
    
    List<Triage> findByTriageDateBetween(LocalDateTime start, LocalDateTime end);
    
    Page<Triage> findAll(Pageable pageable);
    
    boolean delete(String id);
}
