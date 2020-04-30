package com.zzb.message.rabbitmq.cores;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDetail {

    private Object message;
    private String uuid;

    public MessageDetail() {
    }
}
