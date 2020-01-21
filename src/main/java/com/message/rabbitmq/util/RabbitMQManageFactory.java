package com.message.rabbitmq.util;

import com.message.rabbitmq.entity.RabbitMQProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;

@Slf4j
@Component
public class RabbitMQManageFactory {
    @Resource
    AmqpAdmin amqpAdmin;

    @Resource
    private RabbitMQProperties rabbitMQProperties;

    /**
     * 创建Exchange
     *
     * @param exchangeName 名称
     * @param exchangeType 类型
     * @return 交换机实例
     */
    public AbstractExchange createExchange(String exchangeName, String exchangeType, boolean durable, boolean autoDelete) {
        AbstractExchange exchange = getExchange(exchangeName, exchangeType, durable, autoDelete);
        amqpAdmin.declareExchange(exchange);
        log.info("zzbMessage:rabbitMQ ----------> 已创建并添加{} Exchange.", exchangeName);
        return exchange;
    }


    /**
     * 添加Exchange
     *
     * @param exchange 交换机实例
     */
    public void addExchange(AbstractExchange exchange) {
        amqpAdmin.declareExchange(exchange);
        log.info("zzbMessage:rabbitMQ ----------> 已添加{} Exchange.", exchange.getName());
    }

    public Queue createQueue(String queueName, String bindingKey) {
        Queue queue = QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", rabbitMQProperties.getExchangeName())
                .withArgument("x-dead-letter-routing-key", bindingKey)
                .build();
        log.info("zzbMessage:rabbitMQ ----------> 已创建{} Queue.", queueName);
        return queue;
    }


    /**
     * 添加一个指定的Queue
     *
     * @param queue 队列
     * @return queueName
     */
    public String addQueue(Queue queue) {
        String result = amqpAdmin.declareQueue(queue);
        log.info("zzbMessage:rabbitMQ ----------> 已添加{} Queue.", queue.getName());
        return result;
    }


    /**
     * 绑定一个队列到一个匹配型交换机使用一个bindingKey
     *
     * @param exchange   交换机
     * @param queue      队列
     * @param bindingKey key
     */
    public void addBinding(AbstractExchange exchange, Queue queue, String bindingKey) {
        Binding binding = this.getBinding(exchange, queue, bindingKey);
        if (binding != null) {
            amqpAdmin.declareBinding(binding);
            log.info("zzbMessage:rabbitMQ ----------> 已添加交换机{}与队列{}的绑定.", exchange.getName(), queue.getName());
        } else {
            log.info("zzbMessage:rabbitMQ ----------> 绑定队列{}到交换机{}失败.", queue.getName(), exchange.getName());
        }
    }

    /**
     * 获取binding
     *
     * @param exchange   交换机
     * @param queue      队列
     * @param bindingKey key
     * @return binding
     */
    public Binding getBinding(AbstractExchange exchange, Queue queue, String bindingKey) {
        if (exchange instanceof TopicExchange) {
            return BindingBuilder.bind(queue).to((TopicExchange) exchange).with(bindingKey);
        }
        if (exchange instanceof FanoutExchange) {
            return BindingBuilder.bind(queue).to((FanoutExchange) exchange);
        }
        if (exchange instanceof DirectExchange) {
            return BindingBuilder.bind(queue).to((DirectExchange) exchange).with(bindingKey);
        }
        return null;
    }

    /**
     * 获取交换机
     *
     * @param exchangeType 类型
     * @param exchangeName 名称
     * @param durable      持久化
     * @param autoDelete   是否删除
     * @return 交换机
     */
    public AbstractExchange getExchange(String exchangeType,
                                        String exchangeName,
                                        boolean durable,
                                        boolean autoDelete) {
        AbstractExchange obj = null;
        try {
            Class<?> clazz = Class.forName(exchangeType);
            Constructor<?> constructor = clazz.getConstructor(String.class, boolean.class, boolean.class);
            obj = (AbstractExchange) constructor.newInstance(exchangeName, durable, autoDelete);
        } catch (Exception e) {
            log.info("zzbMessage:rabbitMQ ----------> 创建类型为{}的交换机{}失败.", exchangeType, exchangeName);
        }
        return obj;
    }

}
