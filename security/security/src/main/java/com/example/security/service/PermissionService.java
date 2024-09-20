package com.example.security.service;

import com.example.security.model.Permission;
import com.example.security.model.Role;
import com.example.security.model.User;
import com.example.security.repository.PermissionRepository;
import com.example.security.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionService implements IPermissionService{
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private RoleRepository roleRepository;
    Logger logger= LoggerFactory.getLogger(PermissionService.class);

    @Override
    public Permission findByName(String name) {
        return permissionRepository.findByName(name);
    }




    @Override
    public Boolean hasPermission(String permission) {
        try {
            logger.info("Checking permission '{}' for user", permission);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            Boolean hasPermission = false;
            User u = this.userService.findByEmail(username);
            for (Role r : u.getRoles()) {
                for (Permission p : r.getPermissions()) {
                    if (p.getName().equals(permission))
                        hasPermission = true;
                }
            }
            logger.info("Permission '{}' check for user '{}' completed. Result: {}", permission, username, hasPermission);
            return hasPermission;
        } catch (Exception e) {
            logger.error("An error occurred while checking permission for '{}': {}", permission, e.getMessage(), e);
            return false;
        }
    }



    @Override
    public List<Permission> findAll() {
        try {
            logger.info("Fetching all permissions.");
            List<Permission> permissions = this.permissionRepository.findAll();
            logger.info("Successfully fetched all permissions.");
            return permissions;
        } catch (Exception e) {
            logger.error("An error occurred while fetching all permissions: {}", e.getMessage(), e);
            return null;
        }
    }




    @Override
    public List<Permission> existingForRole(String role) {
        try {
            logger.info("Fetching permissions for role: {}", role);
            List<Permission> permissions = new ArrayList<>();
            List<Role> roles = this.roleService.findByName(role);
            for (Role r : roles) {
                if (r.getName().equals(role)) {
                    permissions = r.getPermissions();
                }
            }
            logger.info("Successfully fetched permissions for role: {}", role);
            return permissions;
        } catch (Exception e) {
            logger.error("An error occurred while fetching permissions for role {}: {}", role, e.getMessage(), e);
            return new ArrayList<>();
        }
    }



    @Override
    public List<Permission> unexistingForRole(String role) {
        try {
            logger.info("Fetching unexisting permissions for role: {}", role);
            List<Permission> permissions = existingForRole(role);
            List<Permission> storedPermissions = this.permissionRepository.findAll();
            List<Permission> unexistingPermissions = new ArrayList<>();
            boolean exists = false;
            for (Permission p : storedPermissions) {
                for (Permission p1 : permissions) {
                    if (p.getId().equals(p1.getId()))
                        exists = true;
                }
                if (!exists) {
                    unexistingPermissions.add(p);
                }
                exists = false;
            }
            logger.info("Successfully fetched unexisting permissions for role: {}", role);
            return unexistingPermissions;
        } catch (Exception e) {
            logger.error("An error occurred while fetching unexisting permissions for role {}: {}", role, e.getMessage(), e);
            return new ArrayList<>();
        }
    }



    @Override
    public Permission addPermision(String name, String role) {
        try {
            logger.info("Adding permission '{}' to role '{}'", name, role);
            List<Role> roles = roleService.findByName(role);
            Permission permission = permissionRepository.findByName(name);
            List<Permission> permissions = roles.get(0).getPermissions();
            permissions.add(permission);
            roles.get(0).setPermissions(permissions);
            this.roleRepository.save(roles.get(0));
            logger.info("Permission '{}' successfully added to role '{}'", name, role);
            return permission;
        } catch (Exception e) {
            logger.error("An error occurred while adding permission '{}' to role '{}': {}", name, role, e.getMessage(), e);
            return null;
        }
    }




    @Override
    public Permission removePermission(String name, String role) {
        try {
            logger.info("Removing permission '{}' from role '{}'", name, role);
            List<Role> roles = roleService.findByName(role);
            Permission permission = permissionRepository.findByName(name);
            List<Permission> permissions = roles.get(0).getPermissions();
            permissions.removeIf(permission1 -> permission1.getName().equals(name));
            roles.get(0).setPermissions(permissions);
            this.roleRepository.save(roles.get(0));
            logger.info("Permission '{}' successfully removed from role '{}'", name, role);
            return permission;
        } catch (Exception e) {
            logger.error("An error occurred while removing permission '{}' from role '{}': {}", name, role, e.getMessage(), e);
            return null;
        }
    }



}
