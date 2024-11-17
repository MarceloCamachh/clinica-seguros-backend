package com.marek_kawalski.clinic_system.triage.domain;

import com.marek_kawalski.clinic_system.triage.domain.enums.TriagePriority;
import com.marek_kawalski.clinic_system.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Document(collection = "triages")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Triage {
    @Id
    private String id;
    
    @DocumentReference(lazy = true)
    private User patient;
    
    @DocumentReference(lazy = true)
    private User nurse;
    
    @NotNull(message = "Los signos vitales son obligatorios")
    private VitalSigns vitalSigns;
    
    @NotNull(message = "La prioridad es obligatoria")
    private TriagePriority priority;
    
    @NotBlank(message = "El motivo de consulta es obligatorio")
    private String chiefComplaint;
    
    private String medicalHistory;
    
    private String allergies;
    
    @NotNull(message = "La evaluación neurológica es obligatoria")
    private NeurologicalAssessment neurologicalAssessment;
    
    private String observations;
    
    private LocalDateTime triageDate;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @Builder.Default
    private boolean isActive = true;
}
