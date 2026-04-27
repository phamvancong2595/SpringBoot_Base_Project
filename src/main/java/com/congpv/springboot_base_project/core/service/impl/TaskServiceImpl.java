package com.congpv.springboot_base_project.core.service.impl;

import com.congpv.springboot_base_project.core.entity.TaskStatus;
import com.congpv.springboot_base_project.core.service.ProjectService;
import com.congpv.springboot_base_project.core.service.TaskStatusService;
import com.congpv.springboot_base_project.core.service.UserService;
import com.congpv.springboot_base_project.shared.dto.PageResponse;
import com.congpv.springboot_base_project.shared.dto.TaskRequestDto;
import com.congpv.springboot_base_project.shared.dto.TaskResponseDto;
import com.congpv.springboot_base_project.core.entity.Project;
import com.congpv.springboot_base_project.core.entity.Task;
import com.congpv.springboot_base_project.core.entity.User;
import com.congpv.springboot_base_project.shared.enums.TaskPriority;
import com.congpv.springboot_base_project.shared.exception.ResourceNotFoundException;
import com.congpv.springboot_base_project.infrastructure.repository.ProjectRepository;
import com.congpv.springboot_base_project.infrastructure.repository.TaskRepository;
import com.congpv.springboot_base_project.infrastructure.repository.UserRepository;
import com.congpv.springboot_base_project.core.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskStatusService taskStatusService;
    private final ProjectService projectService;
    private final UserService userService;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public TaskResponseDto createTask(Long projectId, TaskRequestDto request, String reporterUsername) {
        Project project = projectService.findById(projectId);
        User reporter = userService.getUserByName(reporterUsername);

        User assignee = null;
        if (request.assigneeId() != null) {
            assignee = userService.findById(request.assigneeId());
        }
        TaskStatus status = taskStatusService.findById(request.statusId());

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .status(status)
                .project(project)
                .reporter(reporter)
                .assignee(assignee)
                .dueDate(request.dueDate())
                .startDate(request.startDate())
                .estimateHours(request.estimateHours())
                .priority(TaskPriority.valueOf(request.priority()))
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToDto(savedTask);
    }

    @Override
    @Cacheable(value = "tasks", key = "#projectId + '-' + #taskId")
    public TaskResponseDto getTaskById(Long taskId, Long projectId) {
        Task task = taskRepository.findByIdAndProjectIdAndIsDeletedFalse(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
        return mapToDto(task);
    }

    @Override
    @Cacheable(value = "tasks", key = "#projectId + '-' + #pageNo + '-' + #pageSize")
    public PageResponse<TaskResponseDto> getTasksByProject(Long projectId, int pageNo, int pageSize) {
        if (!projectService.existById(projectId)) {
            throw new ResourceNotFoundException("Project", "id", projectId);
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Task> taskPage = taskRepository.findByProjectIdAndIsDeletedFalse(projectId, pageable);
        List<TaskResponseDto> content = taskPage.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return PageResponse.<TaskResponseDto>builder()
                .content(content)
                .page(taskPage.getNumber())
                .size(taskPage.getSize())
                .totalElements(taskPage.getTotalElements())
                .totalPages(taskPage.getTotalPages())
                .isLast(taskPage.isLast())
                .build();
    }

    @Override
    @CacheEvict(value = "tasks", key = "#projectId + '-' + #taskId")
    @Transactional
    public TaskResponseDto updateTask(Long projectId, Long taskId, TaskRequestDto request) {
        TaskStatus status = taskStatusService.findById(request.statusId());
        Task task = taskRepository.findByIdAndProjectIdAndIsDeletedFalse(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(status);
        task.setStartDate(request.startDate());
        task.setDueDate(request.dueDate());
        task.setEstimateHours(request.estimateHours());

        if (request.assigneeId() != null) {
            User assignee = userService.findById(request.assigneeId());
            task.setAssignee(assignee);
        } else {
            task.setAssignee(null);
        }

        Task updatedTask = taskRepository.save(task);
        return mapToDto(updatedTask);
    }

    @Override
    @CacheEvict(value = "tasks", key = "#projectId + '-' + #taskId")
    @Transactional
    public void deleteTask(Long projectId, Long taskId) {
        Task task = taskRepository.findByIdAndProjectIdAndIsDeletedFalse(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
        task.setDeleted(true);
        taskRepository.save(task);
    }

    @Override
    public Task findTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
    }

    private TaskResponseDto mapToDto(Task task) {
        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().getCode())
                .projectId(task.getProject().getId())
                .reporterUsername(task.getReporter().getUsername())
                .assigneeUsername(task.getAssignee() != null ? task.getAssignee().getUsername() : null)
                .startDate(task.getStartDate())
                .dueDate(task.getDueDate())
                .estimateHours(task.getEstimateHours())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
