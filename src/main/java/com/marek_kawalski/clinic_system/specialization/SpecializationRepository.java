package com.marek_kawalski.clinic_system.specialization;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecializationRepository extends MongoRepository<Specialization, String> {
    Optional<Specialization> findByName(String name);
} 