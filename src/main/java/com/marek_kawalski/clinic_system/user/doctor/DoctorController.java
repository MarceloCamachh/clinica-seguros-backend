package com.marek_kawalski.clinic_system.user.doctor;

import com.marek_kawalski.clinic_system.appointment.exception.AppointmentIllegalStateException;
import com.marek_kawalski.clinic_system.examination.exception.ExaminationNotFoundException;
import com.marek_kawalski.clinic_system.user.User;
import com.marek_kawalski.clinic_system.user.UserRequestParams;
import com.marek_kawalski.clinic_system.user.UserRole;
import com.marek_kawalski.clinic_system.user.doctor.dto.AvailableAppointmentsDTO;
import com.marek_kawalski.clinic_system.user.doctor.dto.DoctorDTO;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import com.marek_kawalski.clinic_system.utils.CustomDateTimeFormat;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
// ... existing imports ...
import com.marek_kawalski.clinic_system.user.doctor.dto.UpdateDoctorDTO;
import com.marek_kawalski.clinic_system.user.doctor.dto.CreateDoctorDTO;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/doctors")
@AllArgsConstructor
public class DoctorController {
    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final AvailableAppointmentsMapper availableAppointmentsMapper;

    @GetMapping("/email/{email}")
    public ResponseEntity<DoctorDTO> getDoctorByEmail(@PathVariable final String email) {
        final User doctor = doctorService.getDoctorByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor with email: " + email + " not found"));
        return ResponseEntity.status(HttpStatus.OK).body(doctorMapper.mapDoctorToDoctorDTO(doctor));
    }


    @GetMapping("/{id}/examinations/{examinationId}/available-appointments/date/{date}")
    public ResponseEntity<List<AvailableAppointmentsDTO>> getAvailableAppointments(@PathVariable final String id, @PathVariable final String examinationId,
                                                                                   @CustomDateTimeFormat @PathVariable final LocalDateTime date) {
        try {
            final List<LocalDateTime> availableAppointments = doctorService.getAvailableAppointments(id, examinationId, date);
            return availableAppointments.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
                    : ResponseEntity.status(HttpStatus.OK).body(
                    availableAppointmentsMapper.mapToAvailableAppointmentsDTOList(availableAppointments));
        } catch (UserNotFoundException | ExaminationNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e.getCause());
        } catch (AppointmentIllegalStateException e) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, e.getMessage(), e.getCause());
        }
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<DoctorDTO>> getPagedDoctors(@RequestParam(value = "sort", defaultValue = "name") final String sortField,
                                                           @RequestParam(value = "sort-dir", defaultValue = "ASC") final Sort.Direction sortDirection,
                                                           @RequestParam(value = "page-size", defaultValue = "10") final Integer pageSize,
                                                           @RequestParam(value = "page-num", defaultValue = "0") final Integer pageNum,
                                                           @RequestParam(value = "search", required = false) final String search) {
        final Page<User> pagedUsers = doctorService.getPagedDoctors(UserRequestParams.builder()
                .sortField(sortField)
                .sortDirection(sortDirection)
                .pageSize(pageSize)
                .pageNumber(pageNum)
                .search(search)
                .roles(List.of(UserRole.ROLE_DOCTOR))
                .build());

        return pagedUsers.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null) :
                ResponseEntity.status(HttpStatus.OK).body(pagedUsers.map(doctorMapper::mapDoctorToDoctorDTO));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody CreateDoctorDTO createDoctorDTO) {
        logger.info("Recibiendo solicitud POST para crear doctor: {}", createDoctorDTO);
        try {
            User newDoctor = doctorService.createDoctor(createDoctorDTO);
            logger.info("Doctor creado exitosamente: {}", newDoctor.getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(doctorMapper.mapDoctorToDoctorDTO(newDoctor));
        } catch (Exception e) {
            logger.error("Error al crear doctor: ", e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(
            @PathVariable String id,
            @Valid @RequestBody UpdateDoctorDTO updateDoctorDTO) {
        try {
            User updatedDoctor = doctorService.updateDoctor(id, updateDoctorDTO);
            return ResponseEntity.ok(doctorMapper.mapDoctorToDoctorDTO(updatedDoctor));
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable String id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
