package com.message.bus.entity;

import com.message.bus.annotation.BusSubscribe;
import lombok.Data;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * 元数据
 *
 * @author fuzhengjun
 */
@Data
public class MetaMethod {

    private Object instance;

    private Method method;

    private int paramCount;

    private Class<?>[] paramTypes;

    private int weight;


    public MetaMethod(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
        this.paramCount = method.getParameterCount();
        this.paramTypes = method.getParameterTypes();
        this.weight = AnnotationUtils.findAnnotation(method, BusSubscribe.class).weight();
    }

    /**
     * 方法是否允许访问
     */
    public boolean allowAccess() {
        return method.isAccessible();
    }

    /**
     * 是否打印日志
     */
    public boolean printLog() {
        BusSubscribe subscribe = AnnotationUtils.findAnnotation(method, BusSubscribe.class);
        return subscribe != null && subscribe.debug();
    }
}
