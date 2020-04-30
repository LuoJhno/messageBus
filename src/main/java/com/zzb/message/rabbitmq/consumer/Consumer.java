package com.zzb.message.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.zzb.message.rabbitmq.util.MessageIDCache;
import org.springframework.amqp.core.Message;

import java.util.Map;

public interface Consumer {

    Map<String, String> map = MessageIDCache.getMap();

    public void consume(Message s, Channel channel);
}
