package com.marek_kawalski.clinic_system.triage.controller;

import com.marek_kawalski.clinic_system.triage.domain.Triage;
import com.marek_kawalski.clinic_system.triage.dto.CreateTriageDTO;
import com.marek_kawalski.clinic_system.triage.dto.TriageDTO;
import com.marek_kawalski.clinic_system.triage.dto.UpdateTriageDTO;
import com.marek_kawalski.clinic_system.triage.mapper.TriageMapper;
import com.marek_kawalski.clinic_system.triage.service.TriageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/triages")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TriageController {
    private static final Logger logger = LoggerFactory.getLogger(TriageController.class);
    
    private final TriageService triageService;
    private final TriageMapper triageMapper;

    @PostMapping
    public ResponseEntity<TriageDTO> createTriage(@Valid @RequestBody CreateTriageDTO createTriageDTO) {
        logger.info("Recibiendo solicitud POST para crear triaje: {}", createTriageDTO);
        try {
            Triage newTriage = triageService.createTriage(createTriageDTO);
            logger.info("Triaje creado exitosamente con ID: {}", newTriage.getId());
            return ResponseEntity.ok(triageMapper.mapTriageToTriageDTO(newTriage));
        } catch (Exception e) {
            logger.error("Error al crear triaje: ", e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TriageDTO> updateTriage(
            @PathVariable String id,
            @Valid @RequestBody UpdateTriageDTO updateTriageDTO) {
        logger.info("Recibiendo solicitud PUT para actualizar triaje ID: {}", id);
        return triageService.updateTriage(id, updateTriageDTO)
                .map(triage -> {
                    logger.info("Triaje actualizado exitosamente");
                    return ResponseEntity.ok(triageMapper.mapTriageToTriageDTO(triage));
                })
                .orElseThrow(() -> new RuntimeException("Triaje no encontrado con ID: " + id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TriageDTO> getTriageById(@PathVariable String id) {
        logger.info("Buscando triaje con ID: {}", id);
        return triageService.findById(id)
                .map(triage -> ResponseEntity.ok(triageMapper.mapTriageToTriageDTO(triage)))
                .orElseThrow(() -> new RuntimeException("Triaje no encontrado con ID: " + id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<TriageDTO>> getTriagesByPatientId(@PathVariable String patientId) {
        List<Triage> triages = triageService.findByPatientId(patientId);
        return ResponseEntity.ok(triages.stream()
                .map(triageMapper::mapTriageToTriageDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/nurse/{nurseId}")
    public ResponseEntity<List<TriageDTO>> getTriagesByNurseId(@PathVariable String nurseId) {
        List<Triage> triages = triageService.findByNurseId(nurseId);
        return ResponseEntity.ok(triages.stream()
                .map(triageMapper::mapTriageToTriageDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping
    public ResponseEntity<Page<TriageDTO>> getAllTriages(Pageable pageable) {
        Page<Triage> triages = triageService.findAll(pageable);
        return ResponseEntity.ok(triages.map(triageMapper::mapTriageToTriageDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTriage(@PathVariable String id) {
        logger.info("Recibiendo solicitud DELETE para triaje ID: {}", id);
        if (triageService.delete(id)) {
            logger.info("Triaje eliminado exitosamente");
            return ResponseEntity.noContent().build();
        }
        throw new RuntimeException("Triaje no encontrado con ID: " + id);
    }
}
