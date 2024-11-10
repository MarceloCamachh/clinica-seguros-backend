package com.marek_kawalski.clinic_system.examination;

import lombok.Builder;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;

import java.util.List;

@Builder
public record ExaminationRequestParams(
        String sortField,
        Sort.Direction sortDirection,
        Integer pageSize,
        Integer pageNum,
        String search,
        List<ObjectId> doctorIds
) {
}
