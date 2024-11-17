package com.marek_kawalski.clinic_system.appointment.dto;

import com.marek_kawalski.clinic_system.appointment.AppointmentStatus;
import com.marek_kawalski.clinic_system.appointment.Medicine;
import com.marek_kawalski.clinic_system.appointment.AppointmentType;
import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CreateUpdateAppointmentDTO(
        @NotBlank(message = "El ID del doctor es obligatorio")
        String doctorId,
        
        @NotBlank(message = "El ID del paciente es obligatorio")
        String patientId,
        
        @NotBlank(message = "El ID del examen es obligatorio")
        String examinationId,
        
        String triageId,  // Opcional, solo si viene de un triaje
        
        @NotNull(message = "La fecha es obligatoria")
        LocalDateTime date,
        
        String description,
        
        String diagnosis,
        
        String treatment,
        
        List<Medicine> medicines,
        
        List<String> attachmentUrls,
        
        String medicalNotes,
        
        List<String> procedures,
        
        List<String> labResults,
        
        @NotNull(message = "El estado es obligatorio")
        AppointmentStatus status,
        
        AppointmentType appointmentType,
        LocalDateTime nextAppointmentDate,
        String followUpNotes,
        List<String> relatedAppointmentIds,
        String specialtyArea
) {
}
