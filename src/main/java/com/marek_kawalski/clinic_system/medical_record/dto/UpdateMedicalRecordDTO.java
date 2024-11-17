package com.marek_kawalski.clinic_system.medical_record.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record UpdateMedicalRecordDTO(
    String bloodType,
    List<String> allergies,
    List<String> chronicConditions,
    List<String> familyHistory,
    Double height,
    Double weight,
    Boolean isActive
) {}
