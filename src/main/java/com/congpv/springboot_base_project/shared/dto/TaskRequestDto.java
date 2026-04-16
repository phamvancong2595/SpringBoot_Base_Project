package com.congpv.springboot_base_project.shared.dto;

import java.time.LocalDateTime;

import com.congpv.springboot_base_project.shared.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskRequestDto (
    @NotBlank(message = "Title cannot be empty")
     String title,
     String description,
    @NotNull(message = "Status cannot be null")
     TaskStatus status,
     Long assigneeId,
     LocalDateTime startDate,
     LocalDateTime dueDate,
     Integer estimateHours)
{
}
