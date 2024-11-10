package com.marek_kawalski.clinic_system.specialization;

import com.marek_kawalski.clinic_system.specialization.dto.CreateUpdateSpecializationDTO;
import com.marek_kawalski.clinic_system.specialization.dto.SpecializationDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/specializations")
@AllArgsConstructor
public class SpecializationController {
    private final SpecializationService specializationService;
    private final SpecializationMapper specializationMapper;
    private static final Logger logger = LoggerFactory.getLogger(SpecializationController.class);

    @GetMapping
    public ResponseEntity<List<SpecializationDTO>> getAllSpecializations() {
        List<Specialization> specializations = specializationService.findAll();
        return ResponseEntity.ok(specializations.stream()
                .map(specializationMapper::toDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecializationDTO> getSpecialization(@PathVariable String id) {
        return specializationService.findById(id)
                .map(spec -> ResponseEntity.ok(specializationMapper.toDTO(spec)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SpecializationDTO> createSpecialization(
            @Valid @RequestBody CreateUpdateSpecializationDTO dto) {
        Specialization specialization = specializationService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(specializationMapper.toDTO(specialization));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SpecializationDTO> updateSpecialization(
            @PathVariable String id,
            @Valid @RequestBody CreateUpdateSpecializationDTO dto) {
        return specializationService.update(id, dto)
                .map(spec -> ResponseEntity.ok(specializationMapper.toDTO(spec)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteSpecialization(@PathVariable String id) {
        if (specializationService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
