package org.bazaar.giza.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.queue.transaction.name}")
    private String transactionNotificationQueue;

    @Value("${rabbitmq.exchange.transaction.name}")
    private String transactionNotificationExchange;

    @Value("${rabbitmq.binding.transaction.name}")
    private String transactionNotificationRoutingKey;

    @Bean
    public Queue transactionNotificationQueue() {
        return new Queue(transactionNotificationQueue);
    }

    @Bean
    public TopicExchange emailExchange() {
        return new TopicExchange(transactionNotificationExchange);
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(transactionNotificationQueue())
                .to(emailExchange())
                .with(transactionNotificationRoutingKey);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Register the JavaTimeModule to handle Java 8 date/time types
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper()); // Pass the objectMapper with JavaTimeModule
    }
}
