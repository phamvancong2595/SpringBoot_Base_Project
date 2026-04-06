package com.congpv.springboot_base_project.repository;

import com.congpv.springboot_base_project.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectIdAndIsDeletedFalse(Long projectId);

    Optional<Task> findByIdAndProjectIdAndIsDeletedFalse(Long id, Long projectId);
}
