package com.marek_kawalski.clinic_system.appointment;

import com.marek_kawalski.clinic_system.appointment.dto.AppointmentDTO;
import com.marek_kawalski.clinic_system.appointment.dto.CreateUpdateAppointmentDTO;
import com.marek_kawalski.clinic_system.appointment.exception.AppointmentExistsException;
import com.marek_kawalski.clinic_system.appointment.exception.AppointmentNotFoundException;
import com.marek_kawalski.clinic_system.examination.exception.ExaminationNotFoundException;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/appointments")
@AllArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable("id") final String id) {
        final Optional<Appointment> appointment = appointmentService.findById(id);
        return appointment.map(a -> ResponseEntity.status(HttpStatus.OK).body(appointmentMapper.mapAppoitnemntToAppointmentDTO(a)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PreAuthorize("hasAnyRole('ROLE_DOCTOR', 'ROLE_ADMIN', 'ROLE_REGISTRAR')")
    @GetMapping("/doctors/{id}")
    public ResponseEntity<Page<AppointmentDTO>> getPagedDoctorsAppointments(
            @PathVariable("id") final String id,
            @RequestParam(value = "sort", defaultValue = "date") final String sortField,
            @RequestParam(value = "sort-dir", defaultValue = "ASC") final Sort.Direction sortDirection,
            @RequestParam(value = "page-size", defaultValue = "10") final Integer pageSize,
            @RequestParam(value = "page-num", defaultValue = "0") final Integer pageNum,
            @RequestParam(value = "status", required = false) final AppointmentStatus status,
            @RequestParam(value = "start-date", required = false) final LocalDateTime startDate,
            @RequestParam(value = "end-date", required = false) final LocalDateTime endDate
    ) {

        final Page<Appointment> pagedAppointments = appointmentService.findAllDoctorsAppointments(id, AppointmentRequestParams.builder()
                .sortField(sortField)
                .sortDirection(sortDirection)
                .pageSize(pageSize)
                .pageNumber(pageNum)
                .status(status)
                .startDate(startDate)
                .endDate(endDate)
                .build());

        return pagedAppointments.isEmpty() ? ResponseEntity.noContent().build() :
                ResponseEntity.status(HttpStatus.OK).body(pagedAppointments.map(appointmentMapper::mapAppoitnemntToAppointmentDTO));

    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<Page<AppointmentDTO>> getPagedPatientAppointments(
            @PathVariable("id") final String id,
            @RequestParam(value = "sort", defaultValue = "date") final String sortField,
            @RequestParam(value = "sort-dir", defaultValue = "ASC") final Sort.Direction sortDirection,
            @RequestParam(value = "page-size", defaultValue = "10") final Integer pageSize,
            @RequestParam(value = "page-num", defaultValue = "0") final Integer pageNum,
            @RequestParam(value = "status", required = false) final AppointmentStatus status,
            @RequestParam(value = "start-date", required = false) final LocalDateTime startDate,
            @RequestParam(value = "end-date", required = false) final LocalDateTime endDate
    ) {

        final Page<Appointment> pagedAppointments = appointmentService.findAllPatientsAppointments(id, AppointmentRequestParams.builder()
                .sortField(sortField)
                .sortDirection(sortDirection)
                .pageSize(pageSize)
                .pageNumber(pageNum)
                .status(status)
                .startDate(startDate)
                .endDate(endDate)
                .build());

        return pagedAppointments.isEmpty() ? ResponseEntity.noContent().build() :
                ResponseEntity.status(HttpStatus.OK).body(pagedAppointments.map(appointmentMapper::mapAppoitnemntToAppointmentDTO));

    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable("id") final String id, @RequestBody @Valid final CreateUpdateAppointmentDTO createUpdateAppointmentDTO) {
        final Optional<Appointment> appointment;
        try {
            appointment = appointmentService.createUpdateAppointment(id, createUpdateAppointmentDTO);
            return appointment.map(a -> ResponseEntity.status(HttpStatus.OK).body(appointmentMapper.mapAppoitnemntToAppointmentDTO(a)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } catch (UserNotFoundException | ExaminationNotFoundException | AppointmentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e.getCause());
        } catch (AppointmentExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e.getCause());
        }
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody final CreateUpdateAppointmentDTO createUpdateAppointmentDTO) {
        final Optional<Appointment> appointment;
        try {
            appointment = appointmentService.createUpdateAppointment(null, createUpdateAppointmentDTO);
            return appointment.map(a -> ResponseEntity.status(HttpStatus.CREATED).body(appointmentMapper.mapAppoitnemntToAppointmentDTO(a)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } catch (UserNotFoundException | ExaminationNotFoundException | AppointmentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e.getCause());
        } catch (AppointmentExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e.getCause());
        }
    }
}
