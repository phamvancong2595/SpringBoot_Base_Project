package com.congpv.springboot_base_project.core.service;

import ch.qos.logback.core.status.Status;
import com.congpv.springboot_base_project.core.entity.TaskStatus;

public interface TaskStatusService {
    TaskStatus findById(Long id);
}
