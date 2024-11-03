package com.marek_kawalski.clinic_system.appointment.dto;

import com.marek_kawalski.clinic_system.appointment.AppointmentStatus;
import com.marek_kawalski.clinic_system.appointment.Medicine;
import com.marek_kawalski.clinic_system.utils.CustomDateTimeFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CreateUpdateAppointmentDTO(
        @CustomDateTimeFormat
        LocalDateTime date,

        AppointmentStatus status,

        String description,

        String doctorId,

        String patientId,

        String examinationId,
        List<Medicine> medicines
) {
}
