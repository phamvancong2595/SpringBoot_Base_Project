package com.congpv.springboot_base_project.shared.dto;

import com.congpv.springboot_base_project.shared.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Long projectId;
    private String reporterUsername;
    private String assigneeUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
