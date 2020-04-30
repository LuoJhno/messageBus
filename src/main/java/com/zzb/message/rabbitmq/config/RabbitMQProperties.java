package com.zzb.message.rabbitmq.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class RabbitMQProperties {
    //交换机类型
    @Value("${rabbitmq.exchange.type}")
    private String exchangeType;
    //交换机名称
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    //是否持久化（交换机和队列）
    @Value("${rabbitmq.durable}")
    private boolean durable;
    //是否自动删除（交换机）
    @Value("${rabbitmq.exchange.autoDelete}")
    private boolean autoDelete;
    //队列名称
    @Value("${rabbitmq.queue.name}")
    private String queueName;
    //topic类型交换机routingKey
    @Value("${rabbitmq.queue.routingKey}")
    private String topicKey;
}
