package com.marek_kawalski.clinic_system.appointment;

import com.marek_kawalski.clinic_system.appointment.dto.AppointmentDTO;
import com.marek_kawalski.clinic_system.examination.ExaminationMapper;
import com.marek_kawalski.clinic_system.user.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AppointmentMapper {
    private final UserMapper userMapper;
    private final ExaminationMapper examinationMapper;

    public AppointmentDTO mapAppoitnemntToAppointmentDTO(final Appointment appointment) {
        return AppointmentDTO.builder()
                .id(appointment.getId())
                .date(appointment.getDate())
                .status(appointment.getStatus())
                .description(appointment.getDescription())
                .doctor(userMapper.mapUserToUserDTO(appointment.getDoctor()))
                .patient(userMapper.mapUserToUserDTO(appointment.getPatient()))
                .examination(examinationMapper.mapExaminationToExaminationDTO(appointment.getExamination()))
                .medicines(appointment.getMedicines())
                .build();
    }
}
