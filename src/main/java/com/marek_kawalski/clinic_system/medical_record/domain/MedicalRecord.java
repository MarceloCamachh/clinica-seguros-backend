package com.marek_kawalski.clinic_system.medical_record.domain;

import com.marek_kawalski.clinic_system.user.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "medical_records")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecord {
    @Id
    private String id;
    
    @DocumentReference(lazy = true)
    private User patient;
    
    private String bloodType;
    private List<String> allergies;
    private List<String> chronicConditions;
    private List<String> familyHistory;
    private Double height;
    private Double weight;
    
    @Builder.Default
    private List<String> triageIds = new ArrayList<>();
    
    @Builder.Default
    private List<String> appointmentIds = new ArrayList<>();
    
    @Builder.Default
    private List<String> prescriptionIds = new ArrayList<>();
    
    @Builder.Default
    private List<String> labResultIds = new ArrayList<>();
    
    @Builder.Default
    private List<String> imagingResultIds = new ArrayList<>();
    
    @Builder.Default
    private List<String> surgeryIds = new ArrayList<>();
    
    @Builder.Default
    private List<String> examinationIds = new ArrayList<>();
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    
    @Builder.Default
    private boolean isActive = true;
}
