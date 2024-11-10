package com.marek_kawalski.clinic_system.specialization;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "specializations")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Specialization {
    @Id
    private String id;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    
    private String description;
    
    @Builder.Default
    private boolean active = true;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
