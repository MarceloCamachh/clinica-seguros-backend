package com.marek_kawalski.clinic_system.appointment;

import com.marek_kawalski.clinic_system.examination.Examination;
import com.marek_kawalski.clinic_system.user.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "appointments")
public class Appointment {

    @Id
    private String id;

    private LocalDateTime creationDate;

    private LocalDateTime lastUpdateDate;

    private LocalDateTime date;

    private AppointmentStatus status;

    private String description;

    private List<Medicine> medicines;

    @DocumentReference(lazy = true)
    private User doctor;

    @DocumentReference(lazy = true)
    private User patient;

    @DocumentReference(lazy = true)
    private Examination examination;
}
