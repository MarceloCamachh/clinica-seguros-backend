package com.marek_kawalski.clinic_system.appointment;

import lombok.Builder;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

@Builder
public record AppointmentRequestParams(
        String sortField,
        Sort.Direction sortDirection,
        Integer pageSize,
        Integer pageNumber,
        AppointmentStatus status,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
