package com.example.security.repository;

import com.example.security.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
    Address findByAddressId(Long id);
}
