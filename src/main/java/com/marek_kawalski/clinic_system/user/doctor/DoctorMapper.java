package com.marek_kawalski.clinic_system.user.doctor;

import com.marek_kawalski.clinic_system.user.User;
import com.marek_kawalski.clinic_system.user.doctor.dto.DoctorDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DoctorMapper {
    private final DoctorDetailsMapper doctorDetailsMapper;

    public DoctorDTO mapDoctorToDoctorDTO(final User doctor) {
        return DoctorDTO.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .surname(doctor.getSurname())
                .email(doctor.getEmail())
                .phoneNumber(doctor.getPhoneNumber())
                .doctorDetails(doctorDetailsMapper.mapDoctorDetailsToDoctorDetailsDTO(doctor.getDoctorDetails()))
                .build();
    }
}