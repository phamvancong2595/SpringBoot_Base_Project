package com.congpv.springboot_base_project.service.impl;

import com.congpv.springboot_base_project.dto.TaskRequestDto;
import com.congpv.springboot_base_project.dto.TaskResponseDto;
import com.congpv.springboot_base_project.entity.Project;
import com.congpv.springboot_base_project.entity.Task;
import com.congpv.springboot_base_project.entity.User;
import com.congpv.springboot_base_project.exception.ResourceNotFoundException;
import com.congpv.springboot_base_project.repository.ProjectRepository;
import com.congpv.springboot_base_project.repository.TaskRepository;
import com.congpv.springboot_base_project.repository.UserRepository;
import com.congpv.springboot_base_project.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public TaskResponseDto createTask(Long projectId, TaskRequestDto request, String reporterUsername) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        User reporter = userRepository.findByUsername(reporterUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", reporterUsername));

        User assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getAssigneeId()));
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .project(project)
                .reporter(reporter)
                .assignee(assignee)
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToDto(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponseDto getTaskById(Long projectId, Long taskId) {
        Task task = taskRepository.findByIdAndProjectId(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
        return mapToDto(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project", "id", projectId);
        }
        return taskRepository.findByProjectIdAndIsDeletedFalse(projectId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponseDto updateTask(Long projectId, Long taskId, TaskRequestDto request) {
        Task task = taskRepository.findByIdAndProjectIdAndIsDeletedFalse(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getAssigneeId()));
            task.setAssignee(assignee);
        } else {
            task.setAssignee(null);
        }

        Task updatedTask = taskRepository.save(task);
        return mapToDto(updatedTask);
    }

    @Override
    public void deleteTask(Long projectId, Long taskId) {
        Task task = taskRepository.findByIdAndProjectIdAndIsDeletedFalse(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
        task.setDeleted(true);
        taskRepository.save(task);
    }

    private TaskResponseDto mapToDto(Task task) {
        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .projectId(task.getProject().getId())
                .reporterUsername(task.getReporter().getUsername())
                .assigneeUsername(task.getAssignee() != null ? task.getAssignee().getUsername() : null)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
