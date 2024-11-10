package com.marek_kawalski.clinic_system.user.doctor;

import com.marek_kawalski.clinic_system.examination.Examination;
import com.marek_kawalski.clinic_system.user.doctor.schedule.Schedule;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Getter
@Setter
@Builder
public class DoctorDetails {
    private String specialization;
    private String education;
    private String description;
    private Schedule schedule;

    @DocumentReference(lazy = true)
    private List<Examination> examinations;
}
