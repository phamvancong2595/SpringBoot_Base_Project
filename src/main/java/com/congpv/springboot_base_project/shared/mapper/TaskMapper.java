package com.congpv.springboot_base_project.shared.mapper;

import com.congpv.springboot_base_project.core.entity.Task;
import com.congpv.springboot_base_project.shared.dto.task.TaskResponseDto;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public TaskResponseDto mapToDto(Task task) {
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
