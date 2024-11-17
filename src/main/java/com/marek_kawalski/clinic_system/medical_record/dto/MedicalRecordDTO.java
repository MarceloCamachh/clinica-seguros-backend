package com.marek_kawalski.clinic_system.medical_record.dto;

import com.marek_kawalski.clinic_system.user.dto.UserDTO;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record MedicalRecordDTO(
    String id,
    UserDTO patient,
    String bloodType,
    List<String> allergies,
    List<String> chronicConditions,
    List<String> familyHistory,
    Double height,
    Double weight,
    List<String> triageIds,
    List<String> appointmentIds,
    List<String> prescriptionIds,
    List<String> labResultIds,
    List<String> imagingResultIds,
    List<String> surgeryIds,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String createdBy,
    String updatedBy,
    boolean isActive
) {}
