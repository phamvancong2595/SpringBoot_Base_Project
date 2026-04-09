package com.congpv.springboot_base_project.infrastructure.repository;

import com.congpv.springboot_base_project.core.entity.ProjectMember;
import com.congpv.springboot_base_project.shared.enums.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    Optional<ProjectMember> findByProjectIdAndUserUsername(Long projectId, String userName);

    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

    boolean existsByProjectIdAndRole(Long projectId, ProjectRole role);

    List<ProjectMember> findByProjectId(Long projectId);
}
