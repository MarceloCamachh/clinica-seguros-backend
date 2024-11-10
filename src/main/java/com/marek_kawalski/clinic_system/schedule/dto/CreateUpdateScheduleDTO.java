package com.marek_kawalski.clinic_system.schedule.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Builder;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.LocalDate;
import com.marek_kawalski.clinic_system.utils.CustomTimeFormat;
import com.marek_kawalski.clinic_system.schedule.enums.ScheduleType;

@Builder
public record CreateUpdateScheduleDTO(
    @NotNull(message = "El ID del doctor es obligatorio")
    String doctorId,
    
    @NotNull(message = "El tipo de programación es obligatorio")
    ScheduleType scheduleType,
    
    DayOfWeek dayOfWeek,
    
    LocalDate specificDate,
    
    @Min(1)
    @Max(31)
    Integer dayOfMonth,
    
    @NotNull(message = "La hora de inicio es obligatoria")
    @CustomTimeFormat
    LocalTime startTime,
    
    @NotNull(message = "La hora de fin es obligatoria")
    @CustomTimeFormat
    LocalTime endTime,
    
    boolean isActive
) {}
