package com.congpv.springboot_base_project.shared.dto.task;

public record TaskCreatedEvent(
        Long taskId,
        String taskName,
        String assigneeEmail
) {
}
