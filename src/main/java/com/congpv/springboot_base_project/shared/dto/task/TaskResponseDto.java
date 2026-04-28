package com.congpv.springboot_base_project.shared.dto.task;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskResponseDto(
        Long id,
        String title,
        String description,
        String status,
        Long projectId,
        String priorityId,
        String reporterUsername,
        String assigneeUsername,
        LocalDateTime startDate,
        LocalDateTime dueDate,
        Integer estimateHours,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
