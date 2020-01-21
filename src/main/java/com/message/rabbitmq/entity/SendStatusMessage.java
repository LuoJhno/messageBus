package com.message.rabbitmq.entity;

import lombok.Data;

@Data
public class SendStatusMessage {

    private boolean flag;

    private String message;

    public SendStatusMessage(boolean flag, String message) {
        this.flag = flag;
        this.message = message;
    }
}
