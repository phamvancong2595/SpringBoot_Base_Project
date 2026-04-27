package com.congpv.springboot_base_project.core.service.impl;

import ch.qos.logback.core.status.Status;
import com.congpv.springboot_base_project.core.entity.TaskStatus;
import com.congpv.springboot_base_project.core.service.TaskStatusService;
import com.congpv.springboot_base_project.infrastructure.repository.TaskStatusRepository;
import com.congpv.springboot_base_project.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskStatusServiceImpl implements TaskStatusService {
    private final TaskStatusRepository taskStatusRepository;

    @Override
    public TaskStatus findById(Long id) {
        return taskStatusRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("TaskStatus", "Id", id));
    }
}
