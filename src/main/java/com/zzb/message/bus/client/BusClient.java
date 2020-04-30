package com.zzb.message.bus.client;

import com.alibaba.fastjson.JSONObject;
import com.zzb.message.bus.advice.ExceptionAdvice;
import com.zzb.message.bus.annotation.BusParam;
import com.zzb.message.bus.cores.MetaMethod;
import com.zzb.message.bus.cores.MetaMethodArray;
import com.zzb.message.bus.listener.HandleSubscribeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@Service
public class BusClient implements IBusClient {

    @Autowired(required = false)
    private HandleSubscribeListener handleSubscribeListener;

//    @Autowired
//    private DefaultSender sender;

//    @Autowired
//    private RabbitMQProperties rabbitMQProperties;

    @Resource
    private ExceptionAdvice exceptionAdvice;

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
        AtomicInteger count = new AtomicInteger(0);
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
        //sender.send(message, rabbitMQProperties.getTopicKey());
    }

    @Override
    public void createQueueAndPostMQMessage(Object message, String queueName, String bindingKey) {
        // sender.createQueueAndSend(message, queueName, bindingKey);
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
            // 简单的类型匹配
            if (!metaMethod.isComplex()) {
                if (messageParams == null) {
                    resultObject = method.invoke(instance);
                } else {
                    resultObject = method.invoke(instance, messageParams);
                }
            } else {// 复杂匹配
                JSONObject jsonObject = (JSONObject) messageParams[0];
                Map<String, Object> aliaDataMap = this.getParamData(metaMethod, jsonObject);

                Object[] paramValues = new Object[aliaDataMap.size()];
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    BusParam busParam = AnnotationUtils.findAnnotation(parameters[i], BusParam.class);
                    paramValues[i] = aliaDataMap.get(busParam.name());
                }
                resultObject = method.invoke(instance, paramValues);
                messageParams = paramValues;
            }
            if (handleSubscribeListener != null) {
                handleSubscribeListener.onSuccess(metaMethod, messageParams, resultObject);
            }
        } catch (Exception e) {
            continueExecute = false;
            // 执行事件Hook
            if (handleSubscribeListener != null) {
                continueExecute = handleSubscribeListener.onError(e);
            }
            //进行统一异常处理
            if (exceptionAdvice != null) {
                exceptionAdvice.handleException(method, new RuntimeException(e));
            }
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
        // 复杂匹配参数
        if (metaMethod.isComplex() && messageParams.length == 1) {
            return adaptBusParamType(metaMethod, messageParams[0]);
        } else {
            // 按照类型来匹配参数
            if (metaMethod.getParamCount() != messageParams.length) {
                return false;
            } else {
                return this.adaptParamType(metaMethod, messageParams);
            }
        }
    }

    private boolean adaptParamType(MetaMethod metaMethod, Object... messages) {
        Type[] types = metaMethod.getMethod().getGenericParameterTypes();
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
//            // TODO 泛型验证 此方法暂时找不到合适的方法来代替
//            if (messages[i] instanceof BaseBusGenericMessage) {
//                Type[] actualGenericType = ((ParameterizedType) types[i]).getActualTypeArguments();
//                if (!((BaseBusGenericMessage) messages[i]).getMessage().getClass().equals(actualGenericType[0])) {
//                    return false;
//                }
//            }

        }
        return true;

    }

    private boolean adaptBusParamType(MetaMethod metaMethod, Object message) {
        if (!message.getClass().equals(JSONObject.class)) {
            return false;
        }
        Map<String, Object> messageJsonMap = (Map<String, Object>) message;
        Type[] types = metaMethod.getMethod().getGenericParameterTypes();
        if (types.length != messageJsonMap.size()) {
            return false;
        }
        Parameter[] parameters = metaMethod.getMethod().getParameters();
        Map<String, Parameter> busParamMap = new HashMap<>();
        for (Parameter parameter : parameters) {
            BusParam busParam = AnnotationUtils.findAnnotation(parameter, BusParam.class);
            // 复杂类型匹配的每个参数都需要用到BusParam注解
            if (busParam == null) {
                return false;
            }
            busParamMap.put(busParam.name(), parameter);
        }
        for (Map.Entry<String, Object> entry : messageJsonMap.entrySet()) {
            // 如果不存在参数别名
            if (!busParamMap.containsKey(entry.getKey())) {
                return false;
            }
            // 类型不匹配 且不为子类
            if (!busParamMap.get(entry.getKey()).getType().equals(entry.getValue().getClass())) {
                if (entry.getValue().getClass().isAssignableFrom(busParamMap.get(entry.getKey()).getType())) {
                    return false;
                }
            }
            busParamMap.remove(entry.getKey());
        }
        return busParamMap.isEmpty();

    }

    private Map<String, Object> getParamData(MetaMethod metaMethod, JSONObject message) {

        Parameter[] parameters = metaMethod.getMethod().getParameters();
        Map<String, Parameter> busParamMap = new HashMap<>();
        Map<String, Object> aliaDataMap = new HashMap<>();
        for (Parameter parameter : parameters) {
            BusParam busParam = AnnotationUtils.findAnnotation(parameter, BusParam.class);
            busParamMap.put(busParam.name(), parameter);
        }

        for (Map.Entry<String, Object> entry : ((Map<String, Object>) message).entrySet()) {
            //存在的时候，将数据转换为对应的数据类型，并存放到map中
            aliaDataMap.put(entry.getKey(), entry.getValue());
            busParamMap.remove(entry.getKey());
        }
        return aliaDataMap;
    }

}
