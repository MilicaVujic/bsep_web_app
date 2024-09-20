package com.example.security.repository;

import com.example.security.model.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest,Long> {
}
