package com.marek_kawalski.clinic_system.examination;

import com.marek_kawalski.clinic_system.examination.dto.ExaminationDTO;
import org.springframework.stereotype.Component;

@Component
public class ExaminationMapper {
    public ExaminationDTO mapExaminationToExaminationDTO(final Examination examination) {
        return ExaminationDTO.builder()
                .id(examination.getId())
                .name(examination.getName())
                .description(examination.getDescription())
                .price(examination.getPrice())
                .duration(examination.getDuration())
                .status(examination.getStatus())
                .createdAt(examination.getCreatedAt())
                .updatedAt(examination.getUpdatedAt())
                .createdBy(examination.getCreatedBy())
                .updatedBy(examination.getUpdatedBy())
                .isActive(examination.isActive())
                .build();
    }
}
