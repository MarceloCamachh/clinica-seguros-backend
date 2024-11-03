package com.marek_kawalski.clinic_system.user.doctor.dto;

import lombok.Builder;

@Builder
public record DoctorDTO(
        String id,
        String name,
        String surname,
        String email,
        String phoneNumber,
        DoctorDetailsDTO doctorDetails
) {
}
