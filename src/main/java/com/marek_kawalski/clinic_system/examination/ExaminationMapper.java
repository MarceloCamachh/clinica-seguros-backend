package com.marek_kawalski.clinic_system.examination;

import com.marek_kawalski.clinic_system.examination.dto.ExaminationDTO;
import org.springframework.stereotype.Component;

@Component
public class ExaminationMapper {
    public ExaminationDTO mapExaminationToExaminationDTO(final Examination examination) {
        return new ExaminationDTO(
                examination.getId(),
                examination.getName(),
                examination.getDescription(),
                examination.getPrice(),
                examination.getDuration(),
                examination.getStatus()
        );
    }
}
