package com.congpv.springboot_base_project.infrastructure.repository;

import com.congpv.springboot_base_project.core.entity.Task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByProjectIdAndIsDeletedFalse(Long projectId, Pageable pageable);

    Optional<Task> findByIdAndProjectIdAndIsDeletedFalse(Long id, Long projectId);
}
