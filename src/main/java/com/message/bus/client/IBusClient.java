package com.message.bus.client;

import java.lang.reflect.InvocationTargetException;

/**
 * Bus客户端接口
 *
 * @author fuzhengjun
 */
public interface IBusClient {

    void post();

    void post(Object... messageParams);

    void submit(Object... messageParams);

    void postMQMessage(Object message);

    void createQueueAndPostMQMessage(Object message, String queueName, String bindingKey);


    /**
     * 解决关于list和set的泛型问题，不推荐用这个方法；推荐将带有此类属性的参数封装为对象后再调用上面的方法
     *
     * @param genericTypes  泛型列表，和@BusSubscribe指定的genericType对应
     * @param messageParams 数据
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    // void post(Class[] genericTypes, Object... messageParams) throws InvocationTargetException, IllegalAccessException;
}
