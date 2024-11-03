package com.marek_kawalski.clinic_system.user.doctor;

import com.marek_kawalski.clinic_system.user.doctor.dto.AvailableAppointmentsDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AvailableAppointmentsMapper {

    public List<AvailableAppointmentsDTO> mapToAvailableAppointmentsDTOList(final List<LocalDateTime> availableAppointments) {
        return availableAppointments.stream()
                .map(date -> AvailableAppointmentsDTO.builder()
                        .date(date)
                        .build())
                .toList();
    }
}
