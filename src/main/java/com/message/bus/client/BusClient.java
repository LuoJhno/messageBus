package com.message.bus.client;

import com.message.bus.entity.MetaMethod;
import com.message.bus.entity.MetaMethodArray;
import com.message.bus.entity.BaseBusGenericMessage;
import com.message.bus.listener.HandleSubscribeListener;
import com.message.rabbitmq.entity.RabbitMQProperties;
import com.message.rabbitmq.send.DefaultSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Bus客户端的实现
 *
 * @author fuzhengjun
 */
@Slf4j
public class BusClient implements IBusClient {

    @Autowired(required = false)
    private HandleSubscribeListener handleSubscribeListener;

    @Autowired
    private DefaultSender sender;

    @Autowired
    private RabbitMQProperties rabbitMQProperties;

//    @Autowired
//    private ExceptionAdvice exceptionAdvice;

    // 主要用于解决背压问题
    private final static Executor executor
            = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5));


    @Async
    @Override
    public void post() {
        post((Object) null);
    }


    @Async
    @Override
    public void post(Object... messageParams) {
        List<MetaMethod> subscribeMethods = this.getSubscribeMethods(messageParams);
        AtomicInteger count = new AtomicInteger();
        subscribeMethods.forEach(metaMethod -> {
            if (executeSubscribe(metaMethod, messageParams)) {
                count.getAndIncrement();
            }
        });
        log.info("zzbMessage:bus ----------> Bus has successfully post {} times data.", count.intValue());

    }

    @Async
    @Override
    public void submit(Object... messageParams) {
        List<MetaMethod> subscribeMethods = this.getSubscribeMethods(messageParams);
        AtomicInteger count = new AtomicInteger();
        subscribeMethods.forEach(metaMethod -> {
            count.getAndIncrement();
            executor.execute(() -> executeSubscribe(metaMethod, messageParams));
        });
        log.info("zzbMessage:bus ----------> Bus has successfully post {} times data.", count.intValue());
    }

    @Override
    public void postMQMessage(Object message) {
        sender.send(message, rabbitMQProperties.getTopicKey());
    }

    @Override
    public void createQueueAndPostMQMessage(Object message, String queueName, String bindingKey) {
        sender.createQueueAndSend(message, queueName, bindingKey);
    }

    private List<MetaMethod> getSubscribeMethods(Object... messageParams) {
        List<MetaMethod> subscribeMethods = new ArrayList<>();
        if (MetaMethodArray.getMetaMethods() != null) {
            for (MetaMethod metaMethod : MetaMethodArray.getMetaMethods()) {
                if (this.adaptMethodParams(metaMethod, messageParams)) {
                    subscribeMethods.add(metaMethod);
                }
            }
        }
        if (subscribeMethods.size() <= 0) {
            log.info("zzbMessage:bus ----------> No method be subscribed by {}.", messageParams);
        }
        return subscribeMethods;
    }


    /**
     * execute Subscribe with metaMethod and Message
     *
     * @param metaMethod    元方法
     * @param messageParams 可变参数
     * @return a bool result,it express whether continue execute
     */
    private boolean executeSubscribe(MetaMethod metaMethod, Object... messageParams) {
        Method method = metaMethod.getMethod();
        Object instance = metaMethod.getInstance();
        Object resultObject;
        boolean continueExecute = true;
        try {
            if (messageParams == null) {
                resultObject = method.invoke(instance);
            } else {
                resultObject = method.invoke(instance, messageParams);
            }
            if (handleSubscribeListener != null) {
                handleSubscribeListener.onSuccess(metaMethod, resultObject);
            }
        } catch (Exception e) {
            continueExecute = false;
            // 执行事件Hook
            if (handleSubscribeListener != null) {
                continueExecute = handleSubscribeListener.onError(e);
            }
            // 进行统一异常处理
//            if (exceptionAdvice != null) {
//                exceptionAdvice.handleException(method, new RuntimeException(e));
//            }
        }
        return continueExecute;
    }


    /**
     * 校验泛型参数
     *
     * @param metaMethod    元方法
     * @param messageParams 参数
     * @return 是否匹配
     */
    private boolean adaptMethodParams(MetaMethod metaMethod, Object... messageParams) {
        if (metaMethod == null) {
            return false;
        }
        if (messageParams == null) {
            return metaMethod.getParamCount() == 0;
        }
        if (metaMethod.getParamCount() != messageParams.length) {
            return false;
        } else {
            return this.adaptParamType(metaMethod, messageParams);
        }
    }

    private boolean adaptParamType(MetaMethod metaMethod, Object... messages) {
        Type[] types = metaMethod.getMethod().getGenericParameterTypes();
        if (null == types) {
            return messages == null;
        }
        if (types.length != messages.length) {
            return false;
        }
        for (int i = 0; i < metaMethod.getParamTypes().length; i++) {
            // 一般验证
            if (!messages[i].getClass().equals(metaMethod.getParamTypes()[i])) {
                // 判断是否为子类
                if (!metaMethod.getParamTypes()[i].isAssignableFrom(messages[i].getClass())) {
                    return false;
                }
            }
            // TODO 泛型验证 此方法暂时找不到合适的方法来代替
            if (messages[i] instanceof BaseBusGenericMessage) {
                Type[] actualGenericType = ((ParameterizedType) types[i]).getActualTypeArguments();
                if (!((BaseBusGenericMessage) messages[i]).getMessage().getClass().equals(actualGenericType[0])) {
                    return false;
                }
            }

        }
        return true;
    }


}
