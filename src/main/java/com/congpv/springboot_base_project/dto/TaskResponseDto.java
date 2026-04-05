package com.congpv.springboot_base_project.dto;

import com.congpv.springboot_base_project.enums.TaskStatus;
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
