package org.bazaar.giza.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.notification}")
    private String notificationExchange;

    @Value("${rabbitmq.routing.transaction}")
    private String transactionRoutingKey;

    @Value("${rabbitmq.routing.cart}")
    private String cartRoutingKey;

    // Queues
    @Value("${rabbitmq.queue.transaction}")
    private String transactionQueue;

    @Value("${rabbitmq.queue.cart}")
    private String cartQueue;

    @Value("${rabbitmq.queue.dlq}")
    private String deadLetterQueue;

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(notificationExchange, true, false); // Durable, non-auto-delete
    }

    @Bean
    public Queue transactionQueue() {
        return QueueBuilder.durable(transactionQueue)
                .withArgument("x-dead-letter-exchange", notificationExchange) // DLX
                .withArgument("x-dead-letter-routing-key", "dlq.routing.key") // DLQ Routing Key
                .build();
    }

    @Bean
    public Queue cartQueue() {
        return QueueBuilder.durable(cartQueue)
                .withArgument("x-dead-letter-exchange", notificationExchange) // DLX
                .withArgument("x-dead-letter-routing-key", "dlq.routing.key") // DLQ Routing Key
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(deadLetterQueue, true); // DLQ for retries
    }

    @Bean
    public Binding transactionBinding() {
        return BindingBuilder.bind(transactionQueue())
                .to(notificationExchange())
                .with(transactionRoutingKey);
    }

    @Bean
    public Binding cartBinding() {
        return BindingBuilder.bind(cartQueue())
                .to(notificationExchange())
                .with(cartRoutingKey);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(notificationExchange())
                .with("dlq.routing.key");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Support Java Time (Instant)
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
