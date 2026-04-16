package com.congpv.springboot_base_project.shared.dto;

import com.congpv.springboot_base_project.shared.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
@Builder
public record TaskResponseDto (
     Long id,
     String title,
     String description,
     TaskStatus status,
     Long projectId,
     String reporterUsername,
     String assigneeUsername,
     LocalDateTime startDate,
     LocalDateTime dueDate,
     Integer estimateHours,
     LocalDateTime createdAt,
     LocalDateTime updatedAt
     )
{
}
