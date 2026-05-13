package com.congpv.springboot_base_project.infrastructure.messaging;

import com.congpv.springboot_base_project.core.service.EmailService;
import com.congpv.springboot_base_project.shared.dto.task.TaskCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.BackOff;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {
    private final EmailService emailService;

    @RetryableTopic(
            attempts = "3",
            backOff = @BackOff(delay = 2000, multiplier = 2.0),
            autoCreateTopics = "true",
            dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(topics = "task-notification-topic", groupId = "minijira-notification-group")
    public void listenTaskCreated(TaskCreatedEvent event, Acknowledgment acknowledgment) {
        log.info("📩 Received message create task: {}", event.taskName());

        emailService.sendNewTaskNotification(event.assigneeEmail(), "You have a new task!", "Do it!: " + event.taskName());

        log.info("✅ Send email succeed for: {}", event.assigneeEmail());
        acknowledgment.acknowledge();
    }
    @DltHandler
    public void processDltMessage(TaskCreatedEvent event,
                                  @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage,
                                  Acknowledgment acknowledgment) {
        log.error("💀 DEAD LETTER QUEUE : {}. Message: {}", event.taskName(), errorMessage);
        acknowledgment.acknowledge();
    }
}