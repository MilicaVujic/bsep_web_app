package com.example.security.repository;

import com.example.security.model.CriticalEventFailedLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CriticalEventFailedLoginRepository extends JpaRepository<CriticalEventFailedLogin,Long> {

    @Query("SELECT c.email " +
            "FROM CriticalEventFailedLogin c " +
            "WHERE c.time >= :startTime " +
            "GROUP BY c.email " +
            "HAVING COUNT(c.email) >= 5")
    List<String> findUsersWithFiveOrMoreFailedLoginsWithinLastMinute(@Param("startTime") LocalDateTime startTime);
}
