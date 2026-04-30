package com.congpv.springboot_base_project.core.repository;

import com.congpv.springboot_base_project.core.entity.Task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @EntityGraph(attributePaths = { "tags", "assignee", "reporter", "status"})
    Page<Task> findByProjectIdAndIsDeletedFalse(Long projectId, Pageable pageable);

    @EntityGraph(attributePaths = { "tags", "assignee", "reporter", "status"})
    Optional<Task> findByIdAndProjectIdAndIsDeletedFalse(Long id, Long projectId);

    @Query("SELECT t FROM Task t " +
            "JOIN FETCH t.status " +
            "JOIN FETCH t.project " +
            "JOIN FETCH t.reporter " +
            "LEFT JOIN FETCH t.assignee " +
            "WHERE t.status.code NOT IN ('DONE') AND t.dueDate < :now")
    List<Task> findOverdueTasks(@Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE Task t SET t.assignee.id =:managerId WHERE t.project.id = :projectId AND t.assignee.id = :userId")
    void updateTaskToManager(@Param("managerId") Long managerId, @Param("userId")Long memberId,@Param("projectId") Long projectId);
}
