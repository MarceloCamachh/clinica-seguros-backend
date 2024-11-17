package com.marek_kawalski.clinic_system.triage.dto;

import com.marek_kawalski.clinic_system.triage.domain.VitalSigns;
import com.marek_kawalski.clinic_system.triage.domain.enums.TriagePriority;
import com.marek_kawalski.clinic_system.triage.domain.NeurologicalAssessment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateTriageDTO(
    @NotBlank(message = "El ID del paciente es obligatorio")
    String patientId,
    
    @NotBlank(message = "El ID del enfermero o doctor es obligatorio")
    String nurseId,
    
    @NotNull(message = "Los signos vitales son obligatorios")
    VitalSigns vitalSigns,
    
    @NotNull(message = "La prioridad es obligatoria")
    TriagePriority priority,
    
    @NotBlank(message = "El motivo de consulta es obligatorio")
    String chiefComplaint,
    
    String medicalHistory,
    
    String allergies,
    
    @NotNull(message = "La evaluación neurológica es obligatoria")
    NeurologicalAssessment neurologicalAssessment,
    
    String observations
) {}
