package com.marek_kawalski.clinic_system.schedule;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import com.marek_kawalski.clinic_system.user.User;
import com.marek_kawalski.clinic_system.schedule.enums.ScheduleType;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "doctor_schedules")
public class DoctorSchedule {
    @Id
    private String id;
    
    @DocumentReference(lazy = true)
    @Field("doctorId")
    private User doctor;
    
    private ScheduleType scheduleType;
    
    // Para programación recurrente
    private DayOfWeek dayOfWeek;
    
    // Para programación diaria específica
    private LocalDate specificDate;
    
    // Para programación mensual
    private Integer dayOfMonth;
    
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    
    private boolean isActive;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
