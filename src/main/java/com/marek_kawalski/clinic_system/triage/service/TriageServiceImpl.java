package com.marek_kawalski.clinic_system.triage.service;

import com.marek_kawalski.clinic_system.triage.domain.Triage;
import com.marek_kawalski.clinic_system.triage.dto.CreateTriageDTO;
import com.marek_kawalski.clinic_system.triage.dto.UpdateTriageDTO;
import com.marek_kawalski.clinic_system.triage.repository.TriageRepository;
import com.marek_kawalski.clinic_system.user.UserRepository;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import com.marek_kawalski.clinic_system.medical_record.domain.MedicalRecord;
import com.marek_kawalski.clinic_system.medical_record.repository.MedicalRecordRepository;
import com.marek_kawalski.clinic_system.medical_record.exception.MedicalRecordUpdateException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TriageServiceImpl implements TriageService {
    
    private final TriageRepository triageRepository;
    private final UserRepository userRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    
    @Override
    public Triage createTriage(CreateTriageDTO dto) {
        var patient = userRepository.findById(dto.patientId())
            .orElseThrow(() -> new UserNotFoundException("Paciente no encontrado"));
            
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
        
        // Crear triaje
        var triage = Triage.builder()
                .patient(patient)
                .nurse(userRepository.findById(dto.nurseId())
                    .orElseThrow(() -> new UserNotFoundException("Enfermero no encontrado")))
                .vitalSigns(dto.vitalSigns())
                .priority(dto.priority())
                .chiefComplaint(dto.chiefComplaint())
                .medicalHistory(dto.medicalHistory())
                .allergies(dto.allergies())
                .neurologicalAssessment(dto.neurologicalAssessment())
                .observations(dto.observations())
                .triageDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isActive(true)
                .build();
                
        var savedTriage = triageRepository.save(triage);
        
        // Actualizar historial médico
        try {
            medicalRecord.getTriageIds().add(savedTriage.getId());
            medicalRecord.setUpdatedAt(LocalDateTime.now());
            medicalRecordRepository.save(medicalRecord);
        } catch (Exception e) {
            throw new MedicalRecordUpdateException("Error al actualizar el historial médico con el nuevo triaje: " + e.getMessage());
        }
        
        return savedTriage;
    }
    
    @Override
    public Optional<Triage> updateTriage(String id, UpdateTriageDTO dto) {
        return triageRepository.findById(id)
                .map(triage -> {
                    if (dto.vitalSigns() != null) {
                        triage.setVitalSigns(dto.vitalSigns());
                    }
                    if (dto.priority() != null) {
                        triage.setPriority(dto.priority());
                    }
                    if (dto.chiefComplaint() != null) {
                        triage.setChiefComplaint(dto.chiefComplaint());
                    }
                    if (dto.medicalHistory() != null) {
                        triage.setMedicalHistory(dto.medicalHistory());
                    }
                    if (dto.allergies() != null) {
                        triage.setAllergies(dto.allergies());
                    }
                    if (dto.neurologicalAssessment() != null) {
                        triage.setNeurologicalAssessment(dto.neurologicalAssessment());
                    }
                    if (dto.observations() != null) {
                        triage.setObservations(dto.observations());
                    }
                    triage.setActive(dto.isActive());
                    triage.setUpdatedAt(LocalDateTime.now());
                    return triageRepository.save(triage);
                });
    }
    
    @Override
    public Optional<Triage> findById(String id) {
        return triageRepository.findById(id);
    }
    
    @Override
    public List<Triage> findByPatientId(String patientId) {
        return triageRepository.findByPatientId(patientId);
    }
    
    @Override
    public List<Triage> findByNurseId(String nurseId) {
        return triageRepository.findByNurseId(nurseId);
    }
    
    @Override
    public List<Triage> findByTriageDateBetween(LocalDateTime start, LocalDateTime end) {
        return triageRepository.findByTriageDateBetween(start, end);
    }
    
    @Override
    public Page<Triage> findAll(Pageable pageable) {
        return triageRepository.findAll(pageable);
    }
    
    @Override
    public boolean delete(String id) {
        return triageRepository.findById(id)
                .map(triage -> {
                    triageRepository.delete(triage);
                    return true;
                })
                .orElse(false);
    }
}
