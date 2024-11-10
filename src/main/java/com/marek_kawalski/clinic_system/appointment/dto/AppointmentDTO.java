package com.marek_kawalski.clinic_system.appointment.dto;

import com.marek_kawalski.clinic_system.appointment.AppointmentStatus;
import com.marek_kawalski.clinic_system.appointment.Medicine;
import com.marek_kawalski.clinic_system.examination.dto.ExaminationDTO;
import com.marek_kawalski.clinic_system.user.dto.UserDTO;
import com.marek_kawalski.clinic_system.utils.CustomDateTimeFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record AppointmentDTO(
        String id,
        @CustomDateTimeFormat
        LocalDateTime date,
        AppointmentStatus status,

        String description,

        UserDTO doctor,

        UserDTO patient,

        ExaminationDTO examination,

        List<Medicine> medicines
) {
}
