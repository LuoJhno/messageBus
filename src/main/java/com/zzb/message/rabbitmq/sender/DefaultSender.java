package com.zzb.message.rabbitmq.sender;

import com.zzb.message.rabbitmq.cores.MessageDetail;
import com.zzb.message.rabbitmq.config.RabbitMQProperties;
import com.zzb.message.rabbitmq.cores.SendStatusMessage;
import com.zzb.message.rabbitmq.util.FastJsonUtil;
import com.zzb.message.rabbitmq.util.RabbitMQManageFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AbstractExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class DefaultSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitMQProperties rabbitMQProperties;
    @Autowired
    private RabbitMQManageFactory rabbitMQManageFactory;
//    @Autowired
//    private AbstractExchange defaultExchange;

    static RetrySendCache cache = new RetrySendCache();

    public SendStatusMessage send(Object message, String bindingKeyString) {
        try {
            cache.setSenderInfo(this, bindingKeyString);
            String uuid = UUID.randomUUID().toString();
            cache.put(uuid, message);
            MessageDetail messageDetail = new MessageDetail(message, uuid);
            rabbitTemplate.convertAndSend(
                    rabbitMQProperties.getExchangeName(),
                    bindingKeyString,
                    FastJsonUtil.objectToString(messageDetail),
                    new CorrelationData(uuid));
        } catch (AmqpException e) {
            e.printStackTrace();
            return new SendStatusMessage(false, "");
        }

        return new SendStatusMessage(true, "");

    }

//    public SendStatusMessage createQueueAndSend(Object message, String queueName, String bindingKey) {
//        Queue queue = rabbitMQManageFactory.createQueue(queueName, bindingKey);
//        rabbitMQManageFactory.addQueue(queue);
//        rabbitMQManageFactory.addBinding(defaultExchange, queue, bindingKey);
//        return this.send(message, bindingKey);
//    }
}
