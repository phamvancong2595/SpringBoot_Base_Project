package com.congpv.springboot_base_project.shared.dto.events;

public record TaskEvent(String assigneeName, String taskTitle, String assigneeEmail) {
}
