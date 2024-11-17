package com.marek_kawalski.clinic_system.medical_record.controller;

import com.marek_kawalski.clinic_system.medical_record.dto.CreateMedicalRecordDTO;
import com.marek_kawalski.clinic_system.medical_record.dto.MedicalRecordDTO;
import com.marek_kawalski.clinic_system.medical_record.dto.UpdateMedicalRecordDTO;
import com.marek_kawalski.clinic_system.medical_record.mapper.MedicalRecordMapper;
import com.marek_kawalski.clinic_system.medical_record.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medical-records")
@AllArgsConstructor
public class MedicalRecordController {
    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);
    private final MedicalRecordService medicalRecordService;
    private final MedicalRecordMapper medicalRecordMapper;

    @PostMapping
    public ResponseEntity<MedicalRecordDTO> createMedicalRecord(@Valid @RequestBody CreateMedicalRecordDTO createMedicalRecordDTO) {
        logger.info("Recibiendo solicitud POST para crear historial médico");
        var medicalRecord = medicalRecordService.createMedicalRecord(createMedicalRecordDTO);
        return ResponseEntity.ok(medicalRecordMapper.toDTO(medicalRecord));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> updateMedicalRecord(
            @PathVariable String id,
            @Valid @RequestBody UpdateMedicalRecordDTO updateMedicalRecordDTO) {
        return medicalRecordService.updateMedicalRecord(id, updateMedicalRecordDTO)
                .map(record -> ResponseEntity.ok(medicalRecordMapper.toDTO(record)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> getMedicalRecord(@PathVariable String id) {
        return medicalRecordService.findById(id)
                .map(record -> ResponseEntity.ok(medicalRecordMapper.toDTO(record)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<MedicalRecordDTO> getPatientMedicalRecord(@PathVariable String patientId) {
        return medicalRecordService.findByPatientId(patientId)
                .map(record -> ResponseEntity.ok(medicalRecordMapper.toDTO(record)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<MedicalRecordDTO>> getAllMedicalRecords(Pageable pageable) {
        var records = medicalRecordService.findAll(pageable)
                .map(medicalRecordMapper::toDTO);
        return ResponseEntity.ok(records);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable String id) {
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.noContent().build();
    }
}
