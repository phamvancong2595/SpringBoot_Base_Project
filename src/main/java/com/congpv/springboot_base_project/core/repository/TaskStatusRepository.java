package com.congpv.springboot_base_project.core.repository;

import com.congpv.springboot_base_project.core.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
}
