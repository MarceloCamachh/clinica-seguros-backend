package com.marek_kawalski.clinic_system.examination;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ExaminationRepositoryCustom {
    Page<Examination> getPagedExaminations(final ExaminationRequestParams examinationRequestParams);

    List<Examination> findAllByDoctorsId(final ObjectId doctorId);
}
