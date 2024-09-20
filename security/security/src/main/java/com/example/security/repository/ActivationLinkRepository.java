package com.example.security.repository;
import com.example.security.model.ActivationLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivationLinkRepository extends JpaRepository<ActivationLink, Long> {

    Optional<ActivationLink> findByToken(String token);
}
