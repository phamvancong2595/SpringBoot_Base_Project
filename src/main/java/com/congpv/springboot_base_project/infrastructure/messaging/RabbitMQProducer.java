package com.congpv.springboot_base_project.infrastructure.messaging;

import com.congpv.springboot_base_project.infrastructure.config.RabbitMQConfig;
import com.congpv.springboot_base_project.shared.dto.events.ExportTaskEvent;
import com.congpv.springboot_base_project.shared.dto.events.CreateTaskEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMQProducer {
    private final RabbitTemplate rabbitTemplate;

    public void publishTaskCreated(CreateTaskEvent event) {
        log.info("Sending task event to RabbitMQ: {}", event);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EMAIL_EXCHANGE,
                RabbitMQConfig.EMAIL_ROUTING_KEY,
                event
        );
        log.info("Task event sent successfully!");
    }
    public void publishTaskExport(ExportTaskEvent event) {
        log.info("Sending export task event to RabbitMQ: {}", event);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXPORT_TASK_EXCHANGE,
                RabbitMQConfig.EXPORT_TASK_ROUTING_KEY,
                event
        );
        log.info("Task export event sent successfully!");
    }

}
