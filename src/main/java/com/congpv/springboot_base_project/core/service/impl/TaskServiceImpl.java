package com.congpv.springboot_base_project.core.service.impl;

import com.congpv.springboot_base_project.shared.dto.PageResponse;
import com.congpv.springboot_base_project.shared.dto.ProjectResponseDto;
import com.congpv.springboot_base_project.shared.dto.TaskRequestDto;
import com.congpv.springboot_base_project.shared.dto.TaskResponseDto;
import com.congpv.springboot_base_project.core.entity.Project;
import com.congpv.springboot_base_project.core.entity.Task;
import com.congpv.springboot_base_project.core.entity.User;
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
                                        .orElseThrow(() -> new ResourceNotFoundException("User", "id",
                                                        request.getAssigneeId()));
                }

                Task task = Task.builder()
                                .title(request.getTitle())
                                .description(request.getDescription())
                                .status(request.getStatus())
                                .project(project)
                                .reporter(reporter)
                                .assignee(assignee)
                                .dueDate(request.getDueDate())
                                .startDate(request.getStartDate())
                                .estimateHours(request.getEstimateHours())
                                .build();

                Task savedTask = taskRepository.save(task);
                return mapToDto(savedTask);
        }

        @Override
        @Transactional(readOnly = true)
        @Cacheable(value = "tasks", key = "#projectId + '-' + #taskId")
        public TaskResponseDto getTaskById(Long projectId, Long taskId) {
                Task task = taskRepository.findByIdAndProjectIdAndIsDeletedFalse(taskId, projectId)
                                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
                return mapToDto(task);
        }

        @Override
        @Transactional(readOnly = true)
        @Cacheable(value = "tasks", key = "#projectId + '-' + #pageNo + '-' + #pageSize")
        public PageResponse<TaskResponseDto> getTasksByProject(Long projectId, int pageNo, int pageSize) {
                if (!projectRepository.existsById(projectId)) {
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
        public TaskResponseDto updateTask(Long projectId, Long taskId, TaskRequestDto request) {
                Task task = taskRepository.findByIdAndProjectIdAndIsDeletedFalse(taskId, projectId)
                                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

                task.setTitle(request.getTitle());
                task.setDescription(request.getDescription());
                task.setStatus(request.getStatus());
                task.setStartDate(request.getStartDate());
                task.setDueDate(request.getDueDate());
                task.setEstimateHours(request.getEstimateHours());

                if (request.getAssigneeId() != null) {
                        User assignee = userRepository.findById(request.getAssigneeId())
                                        .orElseThrow(() -> new ResourceNotFoundException("User", "id",
                                                        request.getAssigneeId()));
                        task.setAssignee(assignee);
                } else {
                        task.setAssignee(null);
                }

                Task updatedTask = taskRepository.save(task);
                return mapToDto(updatedTask);
        }

        @Override
        @CacheEvict(value = "tasks", key = "#projectId + '-' + #taskId")
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
                                .startDate(task.getStartDate())
                                .dueDate(task.getDueDate())
                                .estimateHours(task.getEstimateHours())
                                .createdAt(task.getCreatedAt())
                                .updatedAt(task.getUpdatedAt())
                                .build();
        }
}
