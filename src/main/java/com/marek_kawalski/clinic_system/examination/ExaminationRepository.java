package com.marek_kawalski.clinic_system.examination;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExaminationRepository extends MongoRepository<Examination, String>, ExaminationRepositoryCustom {
}
