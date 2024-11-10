package com.marek_kawalski.clinic_system.schedule;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;
import com.marek_kawalski.clinic_system.schedule.enums.ScheduleType;

@Repository
public interface DoctorScheduleRepository extends MongoRepository<DoctorSchedule, String> {
    List<DoctorSchedule> findByDoctorId(String doctorId);
    List<DoctorSchedule> findByDoctorIdAndScheduleType(String doctorId, ScheduleType scheduleType);
    List<DoctorSchedule> findByDoctorIdAndSpecificDateBetween(String doctorId, LocalDate startDate, LocalDate endDate);
    List<DoctorSchedule> findByDoctorIdAndIsActiveTrue(String doctorId);
}