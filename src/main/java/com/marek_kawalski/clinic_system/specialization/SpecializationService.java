package com.marek_kawalski.clinic_system.specialization;

import com.marek_kawalski.clinic_system.specialization.dto.CreateUpdateSpecializationDTO;
import java.util.List;
import java.util.Optional;

public interface SpecializationService {
    List<Specialization> findAll();
    Optional<Specialization> findById(String id);
    Specialization create(CreateUpdateSpecializationDTO dto);
    Optional<Specialization> update(String id, CreateUpdateSpecializationDTO dto);
    boolean delete(String id);
} 