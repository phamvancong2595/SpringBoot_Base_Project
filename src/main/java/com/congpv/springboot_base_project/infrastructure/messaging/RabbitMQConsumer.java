package com.congpv.springboot_base_project.infrastructure.messaging;

import com.congpv.springboot_base_project.core.service.EmailService;
import com.congpv.springboot_base_project.core.service.TaskExportService;
import com.congpv.springboot_base_project.infrastructure.config.RabbitMQConfig;
import com.congpv.springboot_base_project.shared.dto.events.CreateTaskEvent;
import com.congpv.springboot_base_project.shared.dto.events.ExportTaskEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQConsumer {
    private final EmailService emailService;
    private final TaskExportService taskExportService;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void handleTaskCreated(CreateTaskEvent event) {
        try {
            emailService.sendNewTaskNotification(event.assigneeEmail(), event.taskTitle(), event.assigneeName());
        } catch (Exception e) {
            log.error("Mail server down! Sending to DLQ...", e.getMessage(), e);
            throw new org.springframework.amqp.AmqpRejectAndDontRequeueException(e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.EXPORT_TASK_QUEUE)
    public void handleExportRequest(ExportTaskEvent event) {
        log.info("Receive export event for Project {}", event.projectId());

        File generatedFile = null;
        try {
            generatedFile = taskExportService.generateTasksCsv(event.projectId());

            emailService.sendEmailWithAttachment(
                  "congpv24@gmail.com",
                    "Export file tasks",
                    "Export file attached",
                    generatedFile
            );

        } catch (Exception e) {
            log.error("Process failed", e);
        } finally {
            if (generatedFile != null && generatedFile.exists()) {
                boolean deleted = generatedFile.delete();
                if (deleted) log.info("Remove temp file: {}", generatedFile.getName());
            }
        }
    }
}
