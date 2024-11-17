package com.marek_kawalski.clinic_system.examination.dto;

import com.marek_kawalski.clinic_system.examination.ExaminationStatus;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ExaminationDTO(
        String id,

        String name,

        String description,

        double price,

        int duration,

        ExaminationStatus status,

        LocalDateTime createdAt,

        LocalDateTime updatedAt,

        String createdBy,

        String updatedBy,

        boolean isActive
) {
}
