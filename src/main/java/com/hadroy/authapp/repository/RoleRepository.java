package com.hadroy.authapp.repository;

import com.hadroy.authapp.entity.Role;
import com.hadroy.authapp.entity.RoleApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleApp name);
}
