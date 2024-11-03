package com.marek_kawalski.clinic_system.examination.dto;

import com.marek_kawalski.clinic_system.examination.ExaminationStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateUpdateExaminationDTO(
        String name,

        String description,

        double price,

        int duration,

        ExaminationStatus status,
        List<String> doctorIds
) {
}
