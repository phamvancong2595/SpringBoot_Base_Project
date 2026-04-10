package com.congpv.springboot_base_project.infrastructure.repository;

import com.congpv.springboot_base_project.core.entity.Task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @EntityGraph(attributePaths = { "tags" })
    Page<Task> findByProjectIdAndIsDeletedFalse(Long projectId, Pageable pageable);

    @EntityGraph(attributePaths = { "tags" })
    Optional<Task> findByIdAndProjectIdAndIsDeletedFalse(Long id, Long projectId);

    @Query("SELECT t FROM Task t WHERE t.status NOT IN ('DONE') AND t.dueDate < :now")
    List<Task> findOverdueTasks(@Param("now") LocalDateTime now);
}
