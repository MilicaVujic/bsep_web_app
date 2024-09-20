package com.example.security.repository;

import com.example.security.model.CriticalEventBlockedUserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CriticalEventBlockedUserLoginRepository extends JpaRepository<CriticalEventBlockedUserLogin,Long> {
    @Query("SELECT c.email " +
            "FROM CriticalEventBlockedUserLogin c " +
            "WHERE c.time >= :startTime " +
            "GROUP BY c.email " +
            "HAVING COUNT(c.email) >= 5")
    List<String> findUsersWithFiveOrMoreBlockedUserLoginsWithinLastMinute(@Param("startTime") LocalDateTime startTime);
}
