package com.example.security.repository;

import java.util.List;

import com.example.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RoleRepository extends JpaRepository<Role, Long> {
	List<Role> findByName(String name);
	@Query("SELECT r FROM User u JOIN u.roles r WHERE u.id = :userId")
	List<Role> findRolesByUserId(@Param("userId") Long userId);
}
