package com.congpv.springboot_base_project.core.service;

import com.congpv.springboot_base_project.core.entity.Task;
import com.congpv.springboot_base_project.shared.dto.common.PageResponse;
import com.congpv.springboot_base_project.shared.dto.task.TaskRequestDto;
import com.congpv.springboot_base_project.shared.dto.task.TaskResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(Long projectId, TaskRequestDto request, String reporterUsername);

    TaskResponseDto getTaskById(Long projectId, Long taskId);

    PageResponse<TaskResponseDto> getTasksByProject(Long projectId, int pageNo, int pageSize);

    TaskResponseDto updateTask(Long projectId, Long taskId, TaskRequestDto request);

    void deleteTask(Long projectId, Long taskId);

    Task findTaskById(Long id);

    void assignTaskOfMemberToManager(Long managerId, Long memberId, Long projectId);

    List<TaskResponseDto> getOverdueTasks(LocalDate today);
}
