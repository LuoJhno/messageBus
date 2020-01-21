package com.message.rabbitmq.entity;

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
