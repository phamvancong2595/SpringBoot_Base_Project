package com.congpv.springboot_base_project.infrastructure.messaging;

import com.congpv.springboot_base_project.infrastructure.config.RabbitMQConfig;
import com.congpv.springboot_base_project.shared.dto.events.TaskEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMQProducer {
    private final RabbitTemplate rabbitTemplate;

    public void publishTaskCreated(TaskEvent event) {
        log.info("Sending task event to RabbitMQ: {}", event);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                event
        );
        log.info("Task event sent successfully!");
    }

}
