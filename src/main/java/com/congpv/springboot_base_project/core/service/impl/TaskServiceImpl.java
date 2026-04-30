package com.congpv.springboot_base_project.core.service.impl;

import com.congpv.springboot_base_project.core.entity.TaskStatus;
import com.congpv.springboot_base_project.core.service.*;
import com.congpv.springboot_base_project.infrastructure.messaging.RabbitMQProducer;
import com.congpv.springboot_base_project.shared.dto.common.PageResponse;
import com.congpv.springboot_base_project.shared.dto.events.CreateTaskEvent;
import com.congpv.springboot_base_project.shared.dto.task.TaskRequestDto;
import com.congpv.springboot_base_project.shared.dto.task.TaskResponseDto;
import com.congpv.springboot_base_project.core.entity.Project;
import com.congpv.springboot_base_project.core.entity.Task;
import com.congpv.springboot_base_project.core.entity.User;
import com.congpv.springboot_base_project.shared.enums.TaskPriority;
import com.congpv.springboot_base_project.shared.exception.ResourceNotFoundException;
import com.congpv.springboot_base_project.core.repository.TaskRepository;
import com.congpv.springboot_base_project.shared.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private final TaskMapper taskMapper;
    private final RabbitMQProducer rabbitMQProducer;

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "task_pagination", allEntries = true),
            @CacheEvict(value = "task_overdue", allEntries = true)
    })
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
        rabbitMQProducer.publishTaskCreated(new CreateTaskEvent(task.getAssignee().getUsername(),
                task.getTitle(), "congpv24@gmail.com"));
        return taskMapper.mapToDto(savedTask);
    }

    @Override
    @Cacheable(value = "task_details", key = "#taskId")
    public TaskResponseDto getTaskById(Long taskId, Long projectId) {
        Task task = taskRepository.findByIdAndProjectIdAndIsDeletedFalse(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
        return taskMapper.mapToDto(task);
    }

    @Override
    @Cacheable(value = "task_pagination", key = "#pageNo + '-' + #pageSize")
    public PageResponse<TaskResponseDto> getTasksByProject(Long projectId, int pageNo, int pageSize) {
        if (!projectService.existById(projectId)) {
            throw new ResourceNotFoundException("Project", "id", projectId);
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Task> taskPage = taskRepository.findByProjectIdAndIsDeletedFalse(projectId, pageable);
        List<TaskResponseDto> content = taskPage.getContent().stream()
                .map(taskMapper::mapToDto)
                .collect(Collectors.toList());
        return PageResponse.<TaskResponseDto>builder()
                .content(content)
                .pageNo(taskPage.getNumber())
                .pageSize(taskPage.getSize())
                .totalElements(taskPage.getTotalElements())
                .totalPages(taskPage.getTotalPages())
                .last(taskPage.isLast())
                .build();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "task_details", key = "#taskId"),
            @CacheEvict(value = "task_pagination", allEntries = true),
            @CacheEvict(value = "task_overdue", allEntries = true)
    })
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
        return taskMapper.mapToDto(updatedTask);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "task_details", key = "#taskId"),
            @CacheEvict(value = "task_pagination", allEntries = true),
            @CacheEvict(value = "task_overdue", allEntries = true)
    })
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

    @Override
    @Caching(evict = {
            @CacheEvict(value = "task_details", key = "#taskId"),
            @CacheEvict(value = "task_pagination", allEntries = true),
            @CacheEvict(value = "task_overdue", allEntries = true)
    })
    public void assignTaskOfMemberToManager(Long managerId, Long memberId, Long projectId) {
        taskRepository.updateTaskToManager(managerId, memberId, projectId);
    }

    @Override
    @Cacheable(value = "task_overdue", key = "#today.atStartOfDay()")
    public List<TaskResponseDto> getOverdueTasks(LocalDate today) {
        return taskRepository.findOverdueTasks(today.atStartOfDay())
                .stream().map(taskMapper::mapToDto).collect(Collectors.toList());
    }

}
