package com.congpv.springboot_base_project.dto;

import com.congpv.springboot_base_project.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskRequestDto {
    @NotBlank(message = "Title cannot be empty")
    private String title;
    
    private String description;
    
    @NotNull(message = "Status cannot be null")
    private TaskStatus status;

    private Long assigneeId; // Có thể null
}
