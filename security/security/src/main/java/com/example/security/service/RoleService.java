package com.example.security.service;

import com.example.security.model.Role;
import com.example.security.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleService implements IRoleService{
    @Autowired
    private RoleRepository roleRepository;
    Logger logger= LoggerFactory.getLogger(RoleService.class);

    @Override
    public Role findById(Long id) {
        logger.info("findById method started.");

        Role auth = this.roleRepository.getOne(id);
        return auth;
    }

    @Override
    public List<Role> findByName(String name) {
        logger.info("findByName method started.");

        List<Role> roles = this.roleRepository.findByName(name);
        return roles;
    }

    @Override
    public List<Role> findRolesByUserId(Long userId) {
        logger.info("findRolesByUserId method started.");
        List<Role> roles = this.roleRepository.findRolesByUserId(userId);
        return roles;
    }
}
