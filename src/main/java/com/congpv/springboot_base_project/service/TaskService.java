package com.congpv.springboot_base_project.service;

import com.congpv.springboot_base_project.dto.TaskRequestDto;
import com.congpv.springboot_base_project.dto.TaskResponseDto;

import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(Long projectId, TaskRequestDto request, String reporterUsername);
    TaskResponseDto getTaskById(Long projectId, Long taskId);
    List<TaskResponseDto> getTasksByProject(Long projectId);
    TaskResponseDto updateTask(Long projectId, Long taskId, TaskRequestDto request);
    void deleteTask(Long projectId, Long taskId);
}
