package com.congpv.springboot_base_project.core.repository;

import com.congpv.springboot_base_project.core.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByCode(String code);

    Set<Role> findByCodeIn(List<String> codes);
}
