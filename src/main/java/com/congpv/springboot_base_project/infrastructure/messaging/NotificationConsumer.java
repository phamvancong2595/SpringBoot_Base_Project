package com.congpv.springboot_base_project.infrastructure.messaging;

import com.congpv.springboot_base_project.core.service.EmailService;
import com.congpv.springboot_base_project.shared.dto.task.TaskCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = "task-notification-topic", groupId = "minijira-notification-group")
    public void listenTaskCreated(TaskCreatedEvent event) {
        log.info("📩 Received message create task: {}", event.taskName());

        try {
            emailService.sendNewTaskNotification(event.assigneeEmail(), "You has new task!", "Do it !: " + event.taskName());
            log.info("✅ Send email succeed for: {}", event.assigneeEmail());

        } catch (Exception e) {
            log.error("❌ Send email error: {}.", e.getMessage());
        }
    }
}
