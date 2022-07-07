package com.hadroy.authapp.service;

import com.hadroy.authapp.entity.Role;
import com.hadroy.authapp.error.NotFoundException;
import com.hadroy.authapp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hadroy.authapp.entity.RoleApp.ROLE_ADMIN;
import static com.hadroy.authapp.entity.RoleApp.ROLE_USER;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoles(String roleName) {

        switch (roleName) {
            case "admin" : return roleRepository.findByName(ROLE_ADMIN)
                    .orElseThrow(() -> {
                        throw new NotFoundException("Role "+ roleName + " is not found!!");
                    });

            case "user" : return roleRepository.findByName(ROLE_USER)
                    .orElseThrow(() -> {
                        throw new NotFoundException("Role "+ roleName + " is not found!!");
                    });
        }

        throw new NotFoundException(roleName + " is not found!!!");
    }

    public HttpStatus deleteRole(String roleName) {

        Role role = new Role();

        switch (roleName) {
            case "admin" : role = roleRepository.findByName(ROLE_ADMIN)
                    .orElseThrow(() -> {
                        throw new NotFoundException("Role "+ roleName + " is not found!!");
                    });

            case "user" : role = roleRepository.findByName(ROLE_USER)
                    .orElseThrow(() -> {
                        throw new NotFoundException("Role "+ roleName + " is not found!!");
                    });
        }
        roleRepository.delete(role);
        return HttpStatus.OK;
    }
}
