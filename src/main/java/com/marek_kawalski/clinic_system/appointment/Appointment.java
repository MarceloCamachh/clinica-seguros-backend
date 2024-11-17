package com.marek_kawalski.clinic_system.appointment;

import com.marek_kawalski.clinic_system.examination.Examination;
import com.marek_kawalski.clinic_system.triage.domain.Triage;
import com.marek_kawalski.clinic_system.user.User;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotBlank(message = "El diagnóstico es obligatorio")
    private String diagnosis;

    private String treatment;

    private List<Medicine> medicines;

    private List<String> attachmentUrls;

    @DocumentReference(lazy = true)
    private User doctor;

    @DocumentReference(lazy = true)
    private User patient;

    @DocumentReference(lazy = true)
    private Examination examination;

    @DocumentReference(lazy = true)
    private Triage triage;

    private String medicalNotes;

    private List<String> procedures;

    private List<String> labResults;

    @Builder.Default
    private boolean isActive = true;

    private LocalDateTime nextAppointmentDate;

    private String followUpNotes;

    private AppointmentType appointmentType;

    private List<String> relatedAppointmentIds;

    private String specialtyArea;
}
