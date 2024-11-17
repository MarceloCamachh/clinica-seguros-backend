package com.marek_kawalski.clinic_system.medical_record.repository;

import com.marek_kawalski.clinic_system.medical_record.domain.MedicalRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class MedicalRecordRepositoryCustomImpl implements MedicalRecordRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Override
    public Page<MedicalRecord> findAllActive(Pageable pageable) {
        Query query = new Query().addCriteria(Criteria.where("isActive").is(true));
        query.with(pageable);
        
        var records = mongoTemplate.find(query, MedicalRecord.class);
        return PageableExecutionUtils.getPage(records, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), MedicalRecord.class));
    }

    @Override
    public Page<MedicalRecord> findByPatientName(String patientName, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("patient.name").regex(patientName, "i"));
        query.with(pageable);
        
        var records = mongoTemplate.find(query, MedicalRecord.class);
        return PageableExecutionUtils.getPage(records, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), MedicalRecord.class));
    }

    @Override
    public Page<MedicalRecord> findByDoctorId(String doctorId, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("appointmentIds").in(doctorId));
        query.with(pageable);
        
        var records = mongoTemplate.find(query, MedicalRecord.class);
        return PageableExecutionUtils.getPage(records, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), MedicalRecord.class));
    }
}