package com.congpv.springboot_base_project.shared.dto;

import java.time.LocalDateTime;

import com.congpv.springboot_base_project.core.entity.TaskStatus;
import com.congpv.springboot_base_project.shared.enums.TaskPriority;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record TaskRequestDto(
        @NotBlank(message = "Title cannot be empty")
        String title,
        String description,
        @NotNull(message = "Status id cannot be null")
        Long statusId,
        Long assigneeId,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
        LocalDateTime startDate,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
        LocalDateTime dueDate,
        @Positive(message = "estimate hour must greater than 0")
        Integer estimateHours,
        @NotBlank(message = "Priority cannot be empty")
        @Pattern(regexp = "LOW|MEDIUM|HIGH|URGENT", message = "Priority must be LOW, MEDIUM, HIGH, or URGENT")
        String priority
) {
}
