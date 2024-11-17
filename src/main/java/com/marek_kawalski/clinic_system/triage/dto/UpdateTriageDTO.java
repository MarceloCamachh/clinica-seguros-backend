package com.marek_kawalski.clinic_system.triage.dto;

import com.marek_kawalski.clinic_system.triage.domain.VitalSigns;
import com.marek_kawalski.clinic_system.triage.domain.NeurologicalAssessment;
import com.marek_kawalski.clinic_system.triage.domain.enums.TriagePriority;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateTriageDTO(
    VitalSigns vitalSigns,
    
    TriagePriority priority,
    
    String chiefComplaint,
    
    String medicalHistory,
    
    String allergies,
    
    NeurologicalAssessment neurologicalAssessment,
    
    String observations,
    
    @NotNull(message = "El estado activo/inactivo es obligatorio")
    Boolean isActive
) {}
