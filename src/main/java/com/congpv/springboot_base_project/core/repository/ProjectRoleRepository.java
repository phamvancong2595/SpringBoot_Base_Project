package com.congpv.springboot_base_project.core.repository;

import com.congpv.springboot_base_project.core.entity.ProjectRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRoleRepository extends JpaRepository<ProjectRole, Long> {
    ProjectRole findByCode(String code);
}
