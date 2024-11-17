package com.marek_kawalski.clinic_system.triage.mapper;

import com.marek_kawalski.clinic_system.triage.domain.Triage;
import com.marek_kawalski.clinic_system.triage.dto.TriageDTO;
import com.marek_kawalski.clinic_system.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TriageMapper {
    
    private final UserMapper userMapper;
    
    public TriageDTO mapTriageToTriageDTO(Triage triage) {
        if (triage == null) {
            return null;
        }
        
        return TriageDTO.builder()
                .id(triage.getId())
                .patient(userMapper.mapUserToUserDTO(triage.getPatient()))
                .nurse(userMapper.mapUserToUserDTO(triage.getNurse()))
                .vitalSigns(triage.getVitalSigns())
                .priority(triage.getPriority())
                .chiefComplaint(triage.getChiefComplaint())
                .observations(triage.getObservations())
                .triageDate(triage.getTriageDate())
                .createdAt(triage.getCreatedAt())
                .updatedAt(triage.getUpdatedAt())
                .isActive(triage.isActive())
                .build();
    }
}
