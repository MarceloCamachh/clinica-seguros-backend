package com.marek_kawalski.clinic_system.examination;

import com.marek_kawalski.clinic_system.examination.dto.CreateUpdateExaminationDTO;
import com.marek_kawalski.clinic_system.examination.dto.ExaminationDTO;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ExaminationController {
    private final ExaminationService examinationService;
    private final ExaminationMapper examinationMapper;

    @GetMapping("/doctors/{doctorId}/examinations")
    public ResponseEntity<List<ExaminationDTO>> getDoctorsExaminations(@PathVariable("doctorId") final ObjectId doctorId) {
        final List<Examination> examinations = examinationService.getDoctorsExaminations(doctorId);
        return examinations.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null) :
                ResponseEntity.status(HttpStatus.OK).body(examinations.stream().map(examinationMapper::mapExaminationToExaminationDTO).toList());
    }

    @GetMapping("/examinations/paged")
    public ResponseEntity<Page<ExaminationDTO>> getPagedExaminations(
            @RequestParam(value = "sort", defaultValue = "name") final String sortField,
            @RequestParam(value = "sort-dir", defaultValue = "ASC") final Sort.Direction sortDirection,
            @RequestParam(value = "page-size", defaultValue = "10") final Integer pageSize,
            @RequestParam(value = "page-num", defaultValue = "0") final Integer pageNum,
            @RequestParam(value = "search", required = false) final String search,
            @RequestParam(value = "doctor-ids", required = false) final List<ObjectId> doctorIds
    ) {
        final Page<Examination> pagedExaminations = examinationService.getPagedExaminations(ExaminationRequestParams.builder()
                .sortField(sortField)
                .sortDirection(sortDirection)
                .pageSize(pageSize)
                .pageNum(pageNum)
                .search(search)
                .doctorIds(doctorIds)
                .build());

        return pagedExaminations.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null) :
                ResponseEntity.status(HttpStatus.OK).body(pagedExaminations.map(examinationMapper::mapExaminationToExaminationDTO));

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/examinations")
    public ResponseEntity<ExaminationDTO> create(@RequestBody final CreateUpdateExaminationDTO examinationDTO) {
        return
                examinationService.createUpdateExamination(null, examinationDTO)
                        .map(examination -> ResponseEntity.status(HttpStatus.CREATED).body(examinationMapper.mapExaminationToExaminationDTO(examination)))
                        .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/examinations/{id}")
    public ResponseEntity<ExaminationDTO> update(@PathVariable("id") final String id, @RequestBody final CreateUpdateExaminationDTO examinationDTO) {
        return
                examinationService.createUpdateExamination(id, examinationDTO)
                        .map(examination -> ResponseEntity.status(HttpStatus.OK).body(examinationMapper.mapExaminationToExaminationDTO(examination)))
                        .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }


}
