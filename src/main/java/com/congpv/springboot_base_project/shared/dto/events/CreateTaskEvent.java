package com.congpv.springboot_base_project.shared.dto.events;

public record CreateTaskEvent(String assigneeName, String taskTitle, String assigneeEmail) {
}
