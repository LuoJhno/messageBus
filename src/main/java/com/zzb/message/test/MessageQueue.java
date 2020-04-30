package com.zzb.message.test;

import com.zzb.message.bus.cores.MessageResultQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class MessageQueue {
    public final static String QUEUE_NAME = "message_queue";

    @Bean(name = QUEUE_NAME)
    public MessageResultQueue MESSAGE_QUEUE() {
        return new MessageResultQueue(QUEUE_NAME);
    }
}
