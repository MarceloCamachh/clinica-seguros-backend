package com.marek_kawalski.clinic_system.logapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.marek_kawalski.clinic_system.logapi.model.Log;
@Repository
public interface LogRepository extends MongoRepository<Log, String> {
    
}
