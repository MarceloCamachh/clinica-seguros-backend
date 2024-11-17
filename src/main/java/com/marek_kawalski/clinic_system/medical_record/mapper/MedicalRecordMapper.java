package com.marek_kawalski.clinic_system.medical_record.mapper;

import com.marek_kawalski.clinic_system.medical_record.domain.MedicalRecord;
import com.marek_kawalski.clinic_system.medical_record.dto.MedicalRecordDTO;
import com.marek_kawalski.clinic_system.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MedicalRecordMapper {
    private final UserMapper userMapper;
    
    public MedicalRecordDTO toDTO(MedicalRecord record) {
        return MedicalRecordDTO.builder()
                .id(record.getId())
                .patient(userMapper.mapUserToUserDTO(record.getPatient()))
                .bloodType(record.getBloodType())
                .allergies(record.getAllergies())
                .chronicConditions(record.getChronicConditions())
                .familyHistory(record.getFamilyHistory())
                .height(record.getHeight())
                .weight(record.getWeight())
                .triageIds(record.getTriageIds())
                .appointmentIds(record.getAppointmentIds())
                .prescriptionIds(record.getPrescriptionIds())
                .labResultIds(record.getLabResultIds())
                .imagingResultIds(record.getImagingResultIds())
                .surgeryIds(record.getSurgeryIds())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .createdBy(record.getCreatedBy())
                .updatedBy(record.getUpdatedBy())
                .isActive(record.isActive())
                .build();
    }
}
