package com.marek_kawalski.clinic_system.user.doctor.schedule;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.Map;

@Getter
@Setter
@Builder
public class Schedule {
    private Map<DayOfWeek, DailySchedule> dailySchedules;
}

