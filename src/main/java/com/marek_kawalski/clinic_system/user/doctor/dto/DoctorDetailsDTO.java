package com.marek_kawalski.clinic_system.user.doctor.dto;

import com.marek_kawalski.clinic_system.user.doctor.schedule.Schedule;
import lombok.Builder;

@Builder
public record DoctorDetailsDTO(
        String specialization,
        String education,
        String description,
        Schedule schedule
) {
}
