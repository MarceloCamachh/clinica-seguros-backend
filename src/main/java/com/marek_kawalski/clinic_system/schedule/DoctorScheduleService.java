package com.marek_kawalski.clinic_system.schedule;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.marek_kawalski.clinic_system.schedule.dto.CreateUpdateScheduleDTO;
import com.marek_kawalski.clinic_system.user.User;
import com.marek_kawalski.clinic_system.user.UserRepository;
import com.marek_kawalski.clinic_system.schedule.exception.ScheduleValidationException;
import com.marek_kawalski.clinic_system.schedule.exception.ValidationException;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import com.marek_kawalski.clinic_system.schedule.exception.ScheduleNotFoundException;
import com.marek_kawalski.clinic_system.schedule.enums.ScheduleType;

@Service
public class DoctorScheduleService {
    private static final Logger logger = LoggerFactory.getLogger(DoctorScheduleService.class);

    @Autowired
    private DoctorScheduleRepository scheduleRepository;
    @Autowired
    private UserRepository userRepository;

    public DoctorSchedule createSchedule(CreateUpdateScheduleDTO dto) throws ValidationException {
        validateScheduleDTO(dto);
        validateTimeRange(dto.startTime(), dto.endTime());
        validateScheduleConflicts(dto);
        
        User doctor = userRepository.findById(dto.doctorId())
            .orElseThrow(() -> new UserNotFoundException("¡Doctor no encontrado!"));
            
        DoctorSchedule schedule = DoctorSchedule.builder()
            .doctor(doctor)
            .scheduleType(dto.scheduleType())
            .dayOfWeek(dto.dayOfWeek())
            .specificDate(dto.specificDate())
            .dayOfMonth(dto.dayOfMonth())
            .startTime(dto.startTime())
            .endTime(dto.endTime())
            .isActive(dto.isActive())
            .createdAt(LocalDateTime.now())
            .build();
            
        return scheduleRepository.save(schedule);
    }
    
    public DoctorSchedule updateSchedule(String id, CreateUpdateScheduleDTO dto) 
            throws ScheduleNotFoundException, UserNotFoundException {
        validateScheduleDTO(dto);
        validateTimeRange(dto.startTime(), dto.endTime());
        validateScheduleConflicts(dto);
        
        DoctorSchedule schedule = scheduleRepository.findById(id)
            .orElseThrow(() -> new ScheduleNotFoundException("¡Horario no encontrado!"));
            
        User doctor = userRepository.findById(dto.doctorId())
            .orElseThrow(() -> new UserNotFoundException("¡Doctor no encontrado!"));
            
        schedule.setDoctor(doctor);
        schedule.setScheduleType(dto.scheduleType());
        schedule.setDayOfWeek(dto.dayOfWeek());
        schedule.setSpecificDate(dto.specificDate());
        schedule.setDayOfMonth(dto.dayOfMonth());
        schedule.setStartTime(dto.startTime());
        schedule.setEndTime(dto.endTime());
        schedule.setActive(dto.isActive());
        schedule.setUpdatedAt(LocalDateTime.now());
        
        return scheduleRepository.save(schedule);
    }
    
    public void deleteSchedule(String id) throws ScheduleNotFoundException {
        if (!scheduleRepository.existsById(id)) {
            throw new ScheduleNotFoundException("¡Horario no encontrado!");
        }
        scheduleRepository.deleteById(id);
    }
    
    public List<DoctorSchedule> getDoctorSchedules(String doctorId) {
        logger.info("Buscando horarios para el doctor con ID: {}", doctorId);
        try {
            List<DoctorSchedule> schedules = scheduleRepository.findByDoctorId(doctorId);
            logger.info("Horarios encontrados: {}", schedules.size());
            logger.debug("Detalles de horarios: {}", schedules);
            return schedules;
        } catch (Exception e) {
            logger.error("Error al buscar horarios: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    private void validateScheduleDTO(CreateUpdateScheduleDTO dto) throws ValidationException {
        switch (dto.scheduleType()) {
            case DAILY -> {
                if (dto.specificDate() == null) {
                    throw new ValidationException("La fecha específica es requerida para programación diaria");
                }
            }
            case WEEKLY -> {
                if (dto.dayOfWeek() == null) {
                    throw new ValidationException("El día de la semana es requerido para programación semanal");
                }
            }
            case MONTHLY -> {
                if (dto.dayOfMonth() == null || dto.dayOfMonth() < 1 || dto.dayOfMonth() > 31) {
                    throw new ValidationException("El día del mes debe estar entre 1 y 31");
                }
            }
        }
    }
    
    private void validateTimeRange(LocalTime startTime, LocalTime endTime) throws ValidationException {
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new ValidationException("La hora de inicio debe ser anterior a la hora de fin");
        }
        
        // Mínimo 30 minutos de consulta
        if (startTime.plusMinutes(30).isAfter(endTime)) {
            throw new ValidationException("El horario debe tener al menos 30 minutos de duración");
        }
    }
    
    private void validateScheduleConflicts(CreateUpdateScheduleDTO dto) {
        List<DoctorSchedule> existingSchedules = scheduleRepository.findByDoctorId(dto.doctorId());
        
        for (DoctorSchedule existing : existingSchedules) {
            if (!existing.isActive()) continue;
            
            boolean hasConflict = false;
            
            switch (dto.scheduleType()) {
                case DAILY -> {
                    hasConflict = existing.getSpecificDate() != null && 
                        existing.getSpecificDate().equals(dto.specificDate()) &&
                        hasTimeOverlap(dto.startTime(), dto.endTime(), 
                            existing.getStartTime(), existing.getEndTime());
                }
                case WEEKLY -> {
                    hasConflict = existing.getDayOfWeek() != null && 
                        existing.getDayOfWeek().equals(dto.dayOfWeek()) &&
                        hasTimeOverlap(dto.startTime(), dto.endTime(), 
                            existing.getStartTime(), existing.getEndTime());
                }
                case MONTHLY -> {
                    hasConflict = existing.getDayOfMonth() != null && 
                        existing.getDayOfMonth().equals(dto.dayOfMonth()) &&
                        hasTimeOverlap(dto.startTime(), dto.endTime(), 
                            existing.getStartTime(), existing.getEndTime());
                }
            }
            
            if (hasConflict) {
                throw new ScheduleValidationException("Existe un conflicto con otro horario programado");
            }
        }
    }

    private boolean hasTimeOverlap(LocalTime start1, LocalTime end1, 
                                 LocalTime start2, LocalTime end2) {
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }

    public List<DoctorSchedule> getDoctorSchedulesByType(String doctorId, ScheduleType scheduleType) {
        return scheduleRepository.findByDoctorIdAndScheduleType(doctorId, scheduleType);
    }

    public List<DoctorSchedule> getDoctorSchedulesByDateRange(String doctorId, 
                                                             LocalDate startDate, 
                                                             LocalDate endDate) {
        return scheduleRepository.findByDoctorIdAndSpecificDateBetween(doctorId, startDate, endDate);
    }
}