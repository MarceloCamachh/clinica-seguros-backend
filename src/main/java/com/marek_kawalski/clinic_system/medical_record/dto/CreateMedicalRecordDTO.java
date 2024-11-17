package com.marek_kawalski.clinic_system.medical_record.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateMedicalRecordDTO(
    @NotBlank(message = "El ID del paciente es obligatorio")
    String patientId,
    
    String bloodType,
    
    List<String> allergies,
    
    List<String> chronicConditions,
    
    List<String> familyHistory,
    
    Double height,
    
    Double weight
) {}
