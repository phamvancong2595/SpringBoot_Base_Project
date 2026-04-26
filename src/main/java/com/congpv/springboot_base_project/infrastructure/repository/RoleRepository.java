package com.congpv.springboot_base_project.infrastructure.repository;

import com.congpv.springboot_base_project.core.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByCode(String code);
}
