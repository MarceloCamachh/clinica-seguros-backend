package com.marek_kawalski.clinic_system.user.doctor;

import com.marek_kawalski.clinic_system.user.doctor.dto.DoctorDetailsDTO;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class DoctorDetailsMapper {

    public DoctorDetailsDTO mapDoctorDetailsToDoctorDetailsDTO(@Nullable final DoctorDetails doctorDetails) {
        if (doctorDetails == null) {
            return null;
        }
        return DoctorDetailsDTO.builder()
                .specialization(doctorDetails.getSpecialization())
                .education(doctorDetails.getEducation())
                .description(doctorDetails.getDescription())
                .schedule(doctorDetails.getSchedule())
                .build();
    }
}
