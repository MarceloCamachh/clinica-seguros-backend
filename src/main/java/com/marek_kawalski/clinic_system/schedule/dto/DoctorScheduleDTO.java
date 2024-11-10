package com.marek_kawalski.clinic_system.schedule.dto;

import lombok.Builder;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.LocalDate;
import com.marek_kawalski.clinic_system.schedule.enums.ScheduleType;
import com.marek_kawalski.clinic_system.user.dto.UserDTO;

@Builder
public record DoctorScheduleDTO(
    String id,
    UserDTO doctor,
    ScheduleType scheduleType,
    DayOfWeek dayOfWeek,
    LocalDate specificDate,
    Integer dayOfMonth,
    LocalTime startTime,
    LocalTime endTime,
    boolean isActive
) {} 