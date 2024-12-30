package com.bazaar.inventory.config;

//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

//    @Value("${rabbitmq.exchange.notification}")
//    private String notificationExchange;
//
//    @Value("${rabbitmq.routing.transaction}")
//    private String transactionRoutingKey;
//
//    @Value("${rabbitmq.routing.cart}")
//    private String cartRoutingKey;
//
//    @Bean
//    public TopicExchange notificationExchange() {
//        return new TopicExchange(notificationExchange, true, false); // Durable
//    }
//
//    @Bean
//    public MessageConverter messageConverter() {
//        return new Jackson2JsonMessageConverter(); // JSON Serialization
//    }
}
