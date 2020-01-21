package com.message.bus.entity;

import lombok.Data;

/**
 * @author fuzhengjun
 */
@Data
public class BaseBusGenericMessage<T> {
    private T message;

    private BaseBusGenericMessage() {
    }

    public BaseBusGenericMessage(T message) {
        this.message = message;
    }

}
