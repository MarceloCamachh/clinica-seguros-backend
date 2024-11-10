package com.marek_kawalski.clinic_system.specialization;

import com.marek_kawalski.clinic_system.specialization.dto.CreateUpdateSpecializationDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SpecializationServiceImpl implements SpecializationService {
    
    private final SpecializationRepository specializationRepository;

    @Override
    public List<Specialization> findAll() {
        return specializationRepository.findAll();
    }

    @Override
    public Optional<Specialization> findById(String id) {
        return specializationRepository.findById(id);
    }

    @Override
    public Specialization create(CreateUpdateSpecializationDTO dto) {
        if (specializationRepository.findByName(dto.name()).isPresent()) {
            throw new IllegalStateException("Ya existe una especialización con ese nombre");
        }

        Specialization specialization = Specialization.builder()
                .name(dto.name())
                .description(dto.description())
                .active(dto.active())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return specializationRepository.save(specialization);
    }

    @Override
    public Optional<Specialization> update(String id, CreateUpdateSpecializationDTO dto) {
        return specializationRepository.findById(id)
                .map(spec -> {
                    spec.setName(dto.name());
                    spec.setDescription(dto.description());
                    spec.setActive(dto.active());
                    spec.setUpdatedAt(LocalDateTime.now());
                    return specializationRepository.save(spec);
                });
    }

    @Override
    public boolean delete(String id) {
        return specializationRepository.findById(id)
                .map(spec -> {
                    specializationRepository.delete(spec);
                    return true;
                })
                .orElse(false);
    }
} 