package com.congpv.springboot_base_project.service;

import com.congpv.springboot_base_project.dto.PageResponse;
import com.congpv.springboot_base_project.dto.TaskRequestDto;
import com.congpv.springboot_base_project.dto.TaskResponseDto;

public interface TaskService {
    TaskResponseDto createTask(Long projectId, TaskRequestDto request, String reporterUsername);

    TaskResponseDto getTaskById(Long projectId, Long taskId);

    PageResponse<TaskResponseDto> getTasksByProject(Long projectId, int pageNo, int pageSize);

    TaskResponseDto updateTask(Long projectId, Long taskId, TaskRequestDto request);

    void deleteTask(Long projectId, Long taskId);
}
