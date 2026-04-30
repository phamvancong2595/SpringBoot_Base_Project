package com.congpv.springboot_base_project.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EMAIL_EXCHANGE = "jira.email.exchange";
    public static final String EMAIL_QUEUE = "jira.email.queue";
    public static final String EMAIL_ROUTING_KEY = "jira.email.routingKey";

    public static final String DLX_EXCHANGE = "jira.email.dlx";
    public static final String DLQ_QUEUE = "jira.email.dlq";
    public static final String DL_ROUTING_KEY = "jira.email.deadLetter";

    public static final String EXPORT_TASK_EXCHANGE = "export.task.exchange";
    public static final String EXPORT_TASK_QUEUE = "jira.task.queue";
    public static final String EXPORT_TASK_ROUTING_KEY = "jira.task.routingKey";

    @Bean
    public TopicExchange emailExchange() {
        return new TopicExchange(EMAIL_EXCHANGE);
    }

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(EMAIL_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DL_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(emailQueue()).to(emailExchange()).with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DLX_EXCHANGE);
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ_QUEUE);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DL_ROUTING_KEY);
    }

    @Bean
    public TopicExchange taskExportExchange() {
        return new TopicExchange(EXPORT_TASK_EXCHANGE);
    }

    @Bean
    public Queue taskExportQueue() {
        return new Queue(EXPORT_TASK_QUEUE);
    }

    @Bean
    public Binding taskExportBinding() {
        return BindingBuilder.bind(taskExportQueue()).to(taskExportExchange()).with(EXPORT_TASK_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}