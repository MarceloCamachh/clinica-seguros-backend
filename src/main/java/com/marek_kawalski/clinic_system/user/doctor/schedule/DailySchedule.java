package com.marek_kawalski.clinic_system.user.doctor.schedule;

import com.marek_kawalski.clinic_system.utils.CustomTimeFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Builder
public class DailySchedule {
    @CustomTimeFormat
    private LocalTime startTime;

    @CustomTimeFormat
    private LocalTime endTime;
}
