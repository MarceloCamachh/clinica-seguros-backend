package com.marek_kawalski.clinic_system.examination.dto;

import com.marek_kawalski.clinic_system.examination.ExaminationStatus;

public record ExaminationDTO(
        String id,

        String name,

        String description,

        double price,

        int duration,

        ExaminationStatus status
) {
}
