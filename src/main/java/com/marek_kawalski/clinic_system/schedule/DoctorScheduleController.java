package com.marek_kawalski.clinic_system.schedule;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;

import com.marek_kawalski.clinic_system.schedule.dto.CreateUpdateScheduleDTO;
import com.marek_kawalski.clinic_system.schedule.dto.DoctorScheduleDTO;
import com.marek_kawalski.clinic_system.schedule.mapper.DoctorScheduleMapper;
import com.marek_kawalski.clinic_system.schedule.enums.ScheduleType;



import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import com.marek_kawalski.clinic_system.schedule.exception.ScheduleNotFoundException;
import com.marek_kawalski.clinic_system.schedule.exception.ValidationException;
import com.marek_kawalski.clinic_system.schedule.exception.ScheduleValidationException;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(value = "/schedules")
@CrossOrigin(origins = "*")
public class DoctorScheduleController {
    private static final Logger logger = LoggerFactory.getLogger(DoctorScheduleController.class);
    
    @Autowired
    private DoctorScheduleService scheduleService;
    
    @Autowired
    private DoctorScheduleMapper scheduleMapper;

    @PostMapping
    public ResponseEntity<DoctorScheduleDTO> createSchedule(
            @Valid @RequestBody CreateUpdateScheduleDTO dto) {
        logger.info("Recibida solicitud POST para crear horario: {}", dto);
        try {
            DoctorSchedule schedule = scheduleService.createSchedule(dto);
            logger.info("Horario creado exitosamente con ID: {}", schedule.getId());
            return ResponseEntity.ok(scheduleMapper.mapToDTO(schedule));
        } catch (Exception e) {
            logger.error("Error al crear horario: ", e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorScheduleDTO> updateSchedule(
            @PathVariable String id,
            @Valid @RequestBody CreateUpdateScheduleDTO dto) 
            throws ScheduleNotFoundException, UserNotFoundException, ValidationException, ScheduleValidationException {
        logger.info("Recibida solicitud PUT para actualizar horario ID: {}", id);
        try {
            DoctorSchedule schedule = scheduleService.updateSchedule(id, dto);
            logger.info("Horario actualizado exitosamente");
            return ResponseEntity.ok(scheduleMapper.mapToDTO(schedule));
        } catch (ScheduleNotFoundException | UserNotFoundException | ValidationException | ScheduleValidationException e) {
            logger.error("Error al actualizar horario: ", e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String id) throws ScheduleNotFoundException {
        logger.info("Recibida solicitud DELETE para horario ID: {}", id);
        try {
            scheduleService.deleteSchedule(id);
            logger.info("Horario eliminado exitosamente");
            return ResponseEntity.noContent().build();
        } catch (ScheduleNotFoundException e) {
            logger.error("Error al eliminar horario: ", e);
            throw e;
        }
    }
    
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorScheduleDTO>> getDoctorSchedules(
            @PathVariable String doctorId,
            @RequestParam(required = false) ScheduleType scheduleType,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<DoctorSchedule> schedules;
        if (scheduleType != null) {
            schedules = scheduleService.getDoctorSchedulesByType(doctorId, scheduleType);
        } else if (startDate != null && endDate != null) {
            schedules = scheduleService.getDoctorSchedulesByDateRange(doctorId, startDate, endDate);
        } else {
            schedules = scheduleService.getDoctorSchedules(doctorId);
        }
        
        return ResponseEntity.ok(schedules.stream()
                .map(scheduleMapper::mapToDTO)
                .collect(Collectors.toList()));
    }

    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<String> handleScheduleNotFoundException(ScheduleNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ScheduleValidationException.class)
    public ResponseEntity<String> handleScheduleValidationException(ScheduleValidationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        logger.info("Test endpoint called");
        return ResponseEntity.ok("Controller is working");
    }
}
