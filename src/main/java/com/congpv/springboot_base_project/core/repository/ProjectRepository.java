package com.congpv.springboot_base_project.core.repository;

import com.congpv.springboot_base_project.core.entity.Project;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAllByIsDeletedFalse(Pageable pageable);

    Optional<Project> findByIdAndIsDeletedFalse(Long id);
}
