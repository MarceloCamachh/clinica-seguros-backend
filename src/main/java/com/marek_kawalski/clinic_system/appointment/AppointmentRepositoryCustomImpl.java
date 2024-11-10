package com.marek_kawalski.clinic_system.appointment;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class AppointmentRepositoryCustomImpl implements AppointmentRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Appointment> findAllDoctorsAppointments(final String doctorId, final AppointmentRequestParams appointmentRequestParams) {
        return findAppointmentsByCriteria(doctorId, appointmentRequestParams, "doctor");
    }

    @Override
    public Page<Appointment> findAllPatientsAppointments(final String patientId, final AppointmentRequestParams appointmentRequestParams) {
        return findAppointmentsByCriteria(patientId, appointmentRequestParams, "patient");
    }

    private Page<Appointment> findAppointmentsByCriteria(String entityId, AppointmentRequestParams appointmentRequestParams, String entityFieldName) {
        final int pageNumber = appointmentRequestParams.pageNumber();
        final int pageSize = appointmentRequestParams.pageSize();
        final AppointmentStatus status = appointmentRequestParams.status();
        final String sortField = appointmentRequestParams.sortField();
        final String sortDirection = appointmentRequestParams.sortDirection().name();
        final LocalDateTime startDate = appointmentRequestParams.startDate();
        final LocalDateTime endDate = appointmentRequestParams.endDate();

        // Create pageable object
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.valueOf(sortDirection), sortField));

        // Create query object
        final Query query = new Query();

        // Add criteria to query object
        try {
            query.addCriteria(Criteria.where(entityFieldName).is(new ObjectId(entityId)));
        } catch (IllegalArgumentException e) {
            return Page.empty(pageable);
        }

        // Add status criteria to query object
        if (status != null) {
            query.addCriteria(Criteria.where("status").is(status));
        }

        // Add startDate criteria to query object
        if (startDate != null) {
            query.addCriteria(Criteria.where("date").gte(startDate));
        }

        // Add endDate criteria to query object
        if (endDate != null) {
            query.addCriteria(Criteria.where("date").lte(endDate));
        }

        final long totalCount = mongoTemplate.count(query, Appointment.class);
        query.with(pageable);

        final List<Appointment> appointments = mongoTemplate.find(query, Appointment.class);
        return PageableExecutionUtils.getPage(appointments, pageable, () -> totalCount);
    }

}
