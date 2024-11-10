package com.marek_kawalski.clinic_system.schedule.mapper;

import com.marek_kawalski.clinic_system.schedule.DoctorSchedule;
import com.marek_kawalski.clinic_system.schedule.dto.DoctorScheduleDTO;
import com.marek_kawalski.clinic_system.user.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DoctorScheduleMapper {
    private final UserMapper userMapper;

    public DoctorScheduleDTO mapToDTO(DoctorSchedule schedule) {
        return DoctorScheduleDTO.builder()
            .id(schedule.getId())
            .doctor(userMapper.mapUserToUserDTO(schedule.getDoctor()))
            .scheduleType(schedule.getScheduleType())
            .dayOfWeek(schedule.getDayOfWeek())
            .specificDate(schedule.getSpecificDate())
            .dayOfMonth(schedule.getDayOfMonth())
            .startTime(schedule.getStartTime())
            .endTime(schedule.getEndTime())
            .isActive(schedule.isActive())
            .build();
    }
}