package com.zzb.message.bus.cores;

import com.zzb.message.bus.annotation.BusSubscribe;
import lombok.Data;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Queue;

/**
 * 元数据
 *
 * @author fuzhengjun
 */
@Data
public class MetaMethod {

    private Object instance;

    private Method method;

    private boolean isComplex;

    private int paramCount;

    private Class<?>[] paramTypes;

    private int weight;

    private String queueName;


    public MetaMethod(Object instance, Method method, boolean isComplex,String queueName) {
        this.instance = instance;
        this.method = method;
        this.paramCount = method.getParameterCount();
        this.paramTypes = method.getParameterTypes();
        this.weight = Objects.requireNonNull(AnnotationUtils.findAnnotation(method, BusSubscribe.class)).weight();
        this.isComplex = isComplex;
        this.queueName = queueName;

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
