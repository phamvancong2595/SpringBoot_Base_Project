package com.congpv.springboot_base_project.infrastructure.messaging;

import com.congpv.springboot_base_project.core.service.EmailService;
import com.congpv.springboot_base_project.infrastructure.config.RabbitMQConfig;
import com.congpv.springboot_base_project.shared.dto.events.TaskEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = "jira.email.queue")
    public void handleTaskCreated(TaskEvent event) {
        emailService.sendNewTaskNotification(event.assigneeEmail(), event.taskTitle(), event.assigneeName());
    }
}
