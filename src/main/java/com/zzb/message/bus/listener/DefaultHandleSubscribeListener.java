package com.zzb.message.bus.listener;


import com.zzb.message.bus.cores.MessageResultQueue;
import com.zzb.message.bus.cores.MetaMethod;
import com.zzb.message.bus.cores.MetaResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * when handle subscribe complete,bus will execute handleSubscribeLister
 *
 * @author fuzhengjun
 */
@Component
public class DefaultHandleSubscribeListener implements HandleSubscribeListener {
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * execute subscribe is success,callback this methods.
     *
     * @param metaMethod   execute subscribe's com.zzb.message
     * @param resultObject execute Subscribe's result
     *                     参照rabbitmq的消息处理
     */
    // todo
    public void onSuccess(MetaMethod metaMethod, Object[] messageParams, Object resultObject) {
        MessageResultQueue queue = (MessageResultQueue) applicationContext.getBean(metaMethod.getQueueName());
        MetaResult metaResult = new MetaResult(metaMethod, queue, messageParams, resultObject, UUID.randomUUID().toString());
        queue.addResult(metaResult);
    }

    /**
     * execute subscribe happen exception @{e},callback onError
     *
     * @param t exception
     */
    public boolean onError(Throwable t) {
        return false;
    }
}
