package com.marek_kawalski.clinic_system.triage.dto;

import com.marek_kawalski.clinic_system.triage.domain.VitalSigns;
import com.marek_kawalski.clinic_system.triage.domain.NeurologicalAssessment;
import com.marek_kawalski.clinic_system.triage.domain.enums.TriagePriority;
import com.marek_kawalski.clinic_system.user.dto.UserDTO;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TriageDTO(
    String id,
    UserDTO patient,
    UserDTO nurse,
    VitalSigns vitalSigns,
    TriagePriority priority,
    String chiefComplaint,
    String medicalHistory,
    String allergies,
    NeurologicalAssessment neurologicalAssessment,
    String observations,
    LocalDateTime triageDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    boolean isActive
) {}
