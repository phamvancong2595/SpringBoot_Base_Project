package com.congpv.springboot_base_project.core.service;

import com.congpv.springboot_base_project.core.entity.Task;
import com.congpv.springboot_base_project.shared.dto.PageResponse;
import com.congpv.springboot_base_project.shared.dto.TaskRequestDto;
import com.congpv.springboot_base_project.shared.dto.TaskResponseDto;

public interface TaskService {
    TaskResponseDto createTask(Long projectId, TaskRequestDto request, String reporterUsername);

    TaskResponseDto getTaskById(Long projectId, Long taskId);

    PageResponse<TaskResponseDto> getTasksByProject(Long projectId, int pageNo, int pageSize);

    TaskResponseDto updateTask(Long projectId, Long taskId, TaskRequestDto request);

    void deleteTask(Long projectId, Long taskId);

    Task findTaskById(Long id);

    void assignTaskOfMemberToManager(Long managerId, Long memberId, Long projectId);
}
