package com.congpv.springboot_base_project.shared.dto;

import java.time.LocalDateTime;

import com.congpv.springboot_base_project.core.entity.TaskStatus;
import com.congpv.springboot_base_project.shared.enums.TaskPriority;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskRequestDto(
        @NotBlank(message = "Title cannot be empty")
        String title,
        String description,
        @NotNull(message = "Status cannot be null")
        TaskStatus status,
        Long assigneeId,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
        LocalDateTime startDate,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
        LocalDateTime dueDate,
        Integer estimateHours,
        TaskPriority priority
) {
}
