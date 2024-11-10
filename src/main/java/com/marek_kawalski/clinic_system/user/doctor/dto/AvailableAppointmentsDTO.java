package com.marek_kawalski.clinic_system.user.doctor.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.marek_kawalski.clinic_system.utils.CustomDateTimeFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AvailableAppointmentsDTO(
        @CustomDateTimeFormat
        @JsonUnwrapped
        LocalDateTime date
) {
}
