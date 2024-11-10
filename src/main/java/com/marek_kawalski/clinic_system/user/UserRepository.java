package com.marek_kawalski.clinic_system.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {
    Optional<User> findByEmail(String username);

    List<User> findAllByUserRole(UserRole userRole);
}
