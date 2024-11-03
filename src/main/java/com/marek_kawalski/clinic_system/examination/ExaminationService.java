package com.marek_kawalski.clinic_system.examination;

import com.marek_kawalski.clinic_system.examination.dto.CreateUpdateExaminationDTO;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ExaminationService {
    Page<Examination> getPagedExaminations(final ExaminationRequestParams examinationRequestParams);

    Optional<Examination> createUpdateExamination(final String id, final CreateUpdateExaminationDTO examinationDTO);

    List<Examination> getDoctorsExaminations(final ObjectId doctorId);
}
