package com.congpv.springboot_base_project.infrastructure.messaging;

import com.congpv.springboot_base_project.core.service.EmailService;
import com.congpv.springboot_base_project.infrastructure.config.RabbitMQConfig;
import com.congpv.springboot_base_project.shared.dto.events.TaskEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleTaskCreated(TaskEvent event) {
        try {
            emailService.sendNewTaskNotification(event.assigneeEmail(), event.taskTitle(), event.assigneeName());
        } catch (Exception e) {
            log.error("Mail server down! Sending to DLQ...", e.getMessage(),e);
            throw new org.springframework.amqp.AmqpRejectAndDontRequeueException(e);
        }
    }
}
