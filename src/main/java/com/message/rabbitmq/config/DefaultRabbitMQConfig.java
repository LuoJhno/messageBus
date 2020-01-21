package com.message.rabbitmq.config;

import com.message.rabbitmq.entity.RabbitMQProperties;
import com.message.rabbitmq.util.RabbitMQManageFactory;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AbstractExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class DefaultRabbitMQConfig {

    public RabbitMQProperties rabbitMQProperties;
    public RabbitMQManageFactory rabbitMQManageFactory;

    @SuppressWarnings("static-access")
    @Bean
    public AbstractExchange defaultExchange() {
        return rabbitMQManageFactory.getExchange(rabbitMQProperties.getExchangeType(),
                rabbitMQProperties.getExchangeName(),
                rabbitMQProperties.isDurable(),
                rabbitMQProperties.isAutoDelete());
    }


    @Bean
    public Queue defaultQueue() {
        return new Queue(rabbitMQProperties.getQueueName(), rabbitMQProperties.isDurable());
    }


    @Bean
    public Binding defaultBinding(AbstractExchange defaultExchange, Queue defaultQueue) {
        if (defaultExchange != null) {
            return rabbitMQManageFactory.getBinding(defaultExchange, defaultQueue, rabbitMQProperties.getTopicKey());
        }
        return null;
    }

}
